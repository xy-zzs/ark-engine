package io.ark.engine.mq.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.ark.engine.mq.core.MqMessage;
import io.ark.engine.mq.core.MqProducer;
import io.ark.engine.mq.core.exception.MqSendException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * @author Noah Zhou
 * @description:
 * <p>消息对象序列化为 JSON 字符串后发送，
 * {@code MqMessage} 的 headers 映射为 Kafka Record Headers，
 * 便于 Consumer 侧读取 traceId 等链路信息，无需反序列化 payload。
 *
 * <p>tag 通过 Kafka Header（key = "tag"）传递，
 * Consumer 侧通过 Header 过滤，语义与 RocketMQ tag 对齐，
 * 便于后续切换 MQ 时 Consumer 侧改动最小。
 */
@Slf4j
public class KafkaMqProducer implements MqProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaMqProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Override
    public void syncSend(MqMessage message, String key) {
        try {
            ProducerRecord<String, String> record = buildRecord(message, key);
            // get() 阻塞等待 Broker ACK
            kafkaTemplate.send(record).get();
            log.debug("MQ syncSend success, topic={}, messageId={}",
                    message.getTopic(), message.getMessageId());
        } catch (Exception e) {
            log.error("MQ syncSend failed, topic={}, messageId={}",
                    message.getTopic(), message.getMessageId(), e);
            throw new MqSendException(
                    "Kafka syncSend failed, topic=" + message.getTopic(), e);
        }
    }

    @Override
    public CompletableFuture<Void> asyncSend(MqMessage message, String key) {
        try {
            ProducerRecord<String, String> record = buildRecord(message, key);
            CompletableFuture<Void> future = new CompletableFuture<>();
            kafkaTemplate.send(record).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("MQ asyncSend failed, topic={}, messageId={}",
                            message.getTopic(), message.getMessageId(), ex);
                    future.completeExceptionally(
                            new MqSendException("Kafka asyncSend failed", ex));
                } else {
                    log.debug("MQ asyncSend success, topic={}, messageId={}",
                            message.getTopic(), message.getMessageId());
                    future.complete(null);
                }
            });
            return future;
        } catch (Exception e) {
            throw new MqSendException(
                    "Kafka asyncSend failed, topic=" + message.getTopic(), e);
        }
    }

    // ----------------------------------------------------------------
    // 私有辅助
    // ----------------------------------------------------------------

    private ProducerRecord<String, String> buildRecord(
            MqMessage message, String key) throws Exception {
        String payload = objectMapper.writeValueAsString(message);
        ProducerRecord<String, String> record =
                new ProducerRecord<>(message.getTopic(), key, payload);
        // 携带 tag header
        if (message.getTag() != null) {
            record.headers().add(new RecordHeader(
                    "tag", message.getTag().getBytes(StandardCharsets.UTF_8)));
        }
        // 携带 messageId header（Consumer 侧幂等去重用）
        record.headers().add(new RecordHeader(
                "messageId", message.getMessageId().getBytes(StandardCharsets.UTF_8)));
        // 携带业务自定义 headers
        message.getHeaders().forEach((k, v) ->
                record.headers().add(new RecordHeader(
                        k, v.getBytes(StandardCharsets.UTF_8))));
        return record;
    }
}
