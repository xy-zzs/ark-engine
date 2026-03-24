package io.ark.engine.mq.core.message;

/**
 * @author Noah Zhou
 * @description:
 */
public record MqMessage(
        String messageId,   // 即 DomainEvent.eventId，用于幂等
        String topic,
        String tag,
        String eventType,   // 用于消费端反序列化到正确的事件类
        String body,        // JSON 序列化的 DomainEvent
        long   timestamp,
        int    retryCount   // 当前重试次数
) {
    public MqMessage withRetry() {
        return new MqMessage(messageId, topic, tag, eventType, body,
                timestamp, retryCount + 1);
    }
}
