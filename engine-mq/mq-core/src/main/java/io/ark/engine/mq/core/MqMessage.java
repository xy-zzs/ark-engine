package io.ark.engine.mq.core;

import io.ark.framework.domain.DomainEvent;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Noah Zhou
 * @description: MQ 消息基类。
 *
 * <p>所有业务消息继承此类，统一携带消息元数据：
 * <ul>
 *   <li>{@code messageId} - 全局唯一ID，用于幂等消费去重</li>
 *   <li>{@code topic}     - 目标 Topic</li>
 *   <li>{@code tag}       - 消息标签，用于 Consumer 侧过滤（RocketMQ 原生支持，Kafka 用 header 模拟）</li>
 *   <li>{@code sendTime}  - 发送时间</li>
 *   <li>{@code headers}   - 扩展头，用于传递 traceId 等链路信息</li>
 * </ul>
 */
public abstract class MqMessage extends DomainEvent {

    private final String messageId;
    private final String topic;
    private final String tag;
    private final LocalDateTime sendTime;
    private final Map<String, String> headers;

    protected MqMessage(String topic, String tag) {
        super( null);
        this.messageId = UUID.randomUUID().toString().replace("-", "");
        this.topic = topic;
        this.tag = tag;
        this.sendTime = LocalDateTime.now();
        this.headers = new HashMap<>();
    }

    public String getMessageId() { return messageId; }
    public String getTopic() { return topic; }
    public String getTag() { return tag; }
    public LocalDateTime getSendTime() { return sendTime; }
    public Map<String, String> getHeaders() { return headers; }

    public MqMessage addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }
}
