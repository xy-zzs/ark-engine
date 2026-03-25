package io.ark.engine.mq.kafka.consumer;

import io.ark.engine.mq.core.message.MqMessage;
import io.ark.engine.mq.kafka.sender.KafkaMessageSender;
import io.ark.engine.mq.starter.ConsumerRegistry;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author Noah Zhou
 * @description: Kafka 消费统一入口
 * topics 通过 SpEL 从配置读取，支持运行时多 topic 订阅：
 *   ark.mq.kafka.listen-topics=TopicA,TopicB,OrderPlacedEvent
 *
 * 收到消息后重建 MqMessage 交由 ConsumerRegistry 路由到对应 MessageConsumer。
 */
@Component
public class KafkaConsumerRouter {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerRouter.class);

    private final ConsumerRegistry consumerRegistry;

    public KafkaConsumerRouter(ConsumerRegistry consumerRegistry) {
        this.consumerRegistry = consumerRegistry;
    }

    @KafkaListener(
            // SpEL 读取配置，逗号分隔的 topic 列表，Spring Kafka 自动拆分
            topics         = "#{@kafkaTopicProvider.topics()}",
            groupId        = "${ark.mq.kafka.group-id:ark-default-group}",
            // 手动 ack，确保消费成功后再提交 offset
            containerFactory = "arkKafkaListenerContainerFactory"
    )
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment ack) {
        MqMessage message = null;
        try {
            message = buildMqMessage(record);
            log.debug("[ark-mq-kafka] Received: topic={} partition={} offset={} messageId={}",
                    record.topic(), record.partition(), record.offset(), message.messageId());

            consumerRegistry.dispatch(message);

            // 消费成功，手动提交 offset
            ack.acknowledge();

        } catch (Exception e) {
            log.error("[ark-mq-kafka] Consume failed: topic={} messageId={} cause={}",
                    record.topic(),
                    message != null ? message.messageId() : "unknown",
                    e.getMessage(), e);
            // 不 ack：让 Kafka 重新投递，配合 RetryPolicy 实现框架层重试
            // 如需死信处理，在 ContainerFactory 配置 DeadLetterPublishingRecoverer
        }
    }

    private MqMessage buildMqMessage(ConsumerRecord<String, String> record) {
        String messageId  = readHeader(record, KafkaMessageSender.HEADER_MESSAGE_ID,
                record.key()); // 降级用 key
        String eventType  = readHeader(record, KafkaMessageSender.HEADER_EVENT_TYPE, "");
        String tag        = readHeader(record, KafkaMessageSender.HEADER_TAG, "");
        int    retryCount = Integer.parseInt(
                readHeader(record, KafkaMessageSender.HEADER_RETRY_COUNT, "0"));

        return new MqMessage(
                messageId,
                record.topic(),
                tag,
                eventType,
                record.value(),
                record.timestamp(),
                retryCount
        );
    }

    private String readHeader(ConsumerRecord<?, ?> record, String key, String defaultVal) {
        Header header = record.headers().lastHeader(key);
        if (header == null) return defaultVal;
        return new String(header.value(), StandardCharsets.UTF_8);
    }
}
