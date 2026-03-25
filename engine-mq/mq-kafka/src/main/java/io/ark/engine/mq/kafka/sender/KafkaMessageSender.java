package io.ark.engine.mq.kafka.sender;

import io.ark.engine.mq.core.message.MqMessage;
import io.ark.engine.mq.core.sender.MessageSender;
import io.ark.engine.mq.core.sender.SendCallback;
import io.ark.engine.mq.kafka.config.KafkaMqProperties;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Noah Zhou
 * @description:
 */
public class KafkaMessageSender implements MessageSender {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageSender.class);

    // Header key 约定，消费端解析时使用
    public static final String HEADER_MESSAGE_ID  = "ark-message-id";
    public static final String HEADER_EVENT_TYPE  = "ark-event-type";
    public static final String HEADER_TAG         = "ark-tag";
    public static final String HEADER_RETRY_COUNT = "ark-retry-count";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaMqProperties properties;

    /** 连续失败计数，用于告警阈值判断 */
    private final AtomicInteger consecutiveFailures = new AtomicInteger(0);

    public KafkaMessageSender(KafkaTemplate<String, String> kafkaTemplate,
                              KafkaMqProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.properties    = properties;
    }

    /**
     * 同步发送：等待 broker ack，适合事务场景
     */
    @Override
    public void send(MqMessage message) {
        try {
            ProducerRecord<String, String> record = buildRecord(message);
            // get() 会阻塞直到 broker 确认，超时由 spring.kafka.producer.properties 控制
            kafkaTemplate.send(record).get();
            consecutiveFailures.set(0);
            log.debug("[ark-mq-kafka] Sent message: topic={} messageId={}",
                    message.topic(), message.messageId());
        } catch (Exception e) {
            handleSendFailure(message.messageId(), e);
            throw new RuntimeException(
                    "[ark-mq-kafka] Failed to send message: " + message.messageId(), e);
        }
    }

    /**
     * 异步发送：非阻塞，结果通过 callback 回调
     */
    @Override
    public void sendAsync(MqMessage message, SendCallback callback) {
        ProducerRecord<String, String> record = buildRecord(message);
        kafkaTemplate.send(record).whenComplete((result, ex) -> {
            if (ex == null) {
                consecutiveFailures.set(0);
                logSuccess(result);
                callback.onSuccess(message.messageId());
            } else {
                handleSendFailure(message.messageId(), ex);
                callback.onException(message.messageId(), ex);
            }
        });
    }

    private ProducerRecord<String, String> buildRecord(MqMessage message) {
        // key 使用 aggregateId 语义的 messageId，保证同一聚合的事件落同一 partition
        ProducerRecord<String, String> record =
                new ProducerRecord<>(message.topic(), message.messageId(), message.body());

        // 将框架元数据写入 Kafka Header，消费端无需解析 body 即可路由
        record.headers()
                .add(new RecordHeader(HEADER_MESSAGE_ID,
                        message.messageId().getBytes(StandardCharsets.UTF_8)))
                .add(new RecordHeader(HEADER_EVENT_TYPE,
                        message.eventType().getBytes(StandardCharsets.UTF_8)))
                .add(new RecordHeader(HEADER_TAG,
                        message.tag().getBytes(StandardCharsets.UTF_8)))
                .add(new RecordHeader(HEADER_RETRY_COUNT,
                        String.valueOf(message.retryCount()).getBytes(StandardCharsets.UTF_8)));

        return record;
    }

    private void handleSendFailure(String messageId, Throwable e) {
        int failures = consecutiveFailures.incrementAndGet();
        if (failures >= properties.getAlertThreshold()) {
            // 超过告警阈值升级为 ERROR，触发监控系统告警
            log.error("[ark-mq-kafka] ALERT: {} consecutive send failures! " +
                    "Latest messageId={}, cause={}", failures, messageId, e.getMessage());
        } else {
            log.warn("[ark-mq-kafka] Send failed (consecutive={}): messageId={}, cause={}",
                    failures, messageId, e.getMessage());
        }
    }

    private void logSuccess(SendResult<String, String> result) {
        if (log.isDebugEnabled()) {
            log.debug("[ark-mq-kafka] Async send success: topic={} partition={} offset={}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        }
    }
}
