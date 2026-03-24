package io.ark.engine.mq.kafka.consumer;

import io.ark.engine.mq.core.consumer.MessageConsumer;

import java.util.List;

/**
 * @author Noah Zhou
 * @description:
 * 收集所有 MessageConsumer 的 topic，提供给 @KafkaListener SpEL 使用。
 * Bean 名固定为 kafkaTopicProvider，与 SpEL 表达式对应。
 */
public class KafkaTopicProvider {

    private final String[] topicArray;

    public KafkaTopicProvider(List<MessageConsumer> consumers) {
        this.topicArray = consumers.stream()
                .map(MessageConsumer::topic)
                .distinct()
                .toArray(String[]::new);
    }

    /** @KafkaListener topics SpEL 调用此方法 */
    public String[] topics() {
        return topicArray;
    }
}
