package io.ark.engine.mq.core.message;

import io.ark.framework.domain.DomainEvent;

/**
 * @author Noah Zhou
 * @description:
 */

public abstract class MqMessage extends DomainEvent {
    String messageId;   // 即 DomainEvent.eventId，用于幂等
    String topic;
    String tag;
    String eventType;   // 用于消费端反序列化到正确的事件类
    String body;        // JSON 序列化的 DomainEvent
    DomainEvent payload;  // 组合
    long timestamp;
    int retryCount;   // 当前重试次数

    public MqMessage(String topic, String tag) {
        super(topic, tag);
    }


    public String getMessageId() {
        return messageId;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    public String getBody() {
        return body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public MqMessage(String messageId, String topic, String tag, String eventType, String body, long timestamp) {
        super(messageId);
        this.messageId   = messageId;
        this.topic       = topic;
        this.tag         = tag;
        this.eventType   = eventType;
        this.body        = body;
        this.timestamp   = timestamp;
        this.retryCount  = 0;
    }
}
