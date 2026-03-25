package io.ark.framework.domain;


import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * @Description: 领域事件基类
 *
 * <p>领域事件表达"领域内某件重要的事情已经发生"，是聚合根状态变更的副产物。
 *
 * <p>生命周期：
 * <ol>
 *   <li>聚合根领域方法执行时，调用 {@link AggregateRoot#registerEvent} 收集事件</li>
 *   <li>infrastructure 层 Repository 在持久化成功后，统一取出并发布</li>
 *   <li>发布后事件列表清空，防止重复发布</li>
 * </ol>
 *
 * <p>命名约定：使用过去时，如 {@code UserRegisteredEvent}、{@code OrderPaidEvent}
 * @Author: Noah Zhou
 */
public abstract class DomainEvent {

    private final String  eventId;
    private final String  eventType;
    /** 路由 tag，供 MQ 过滤使用，默认等于 eventType */
    private final String  tag;
    /**
     * 事件发送时间，创建时自动赋值
     */
    private final Instant occurredAt;

    /**
     * 触发此事件的聚合根 ID（字符串化，便于跨类型通用）
     */
    private final String aggregateId;

    protected DomainEvent(String aggregateId) {
        this.eventId     = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.occurredAt  = Instant.now();
        this.eventType   = this.getClass().getSimpleName();
        this.tag         = this.eventType;
    }

    protected DomainEvent(String aggregateId, String tag) {
        this.eventId     = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.occurredAt  = Instant.now();
        this.eventType   = this.getClass().getSimpleName();
        this.tag         = tag;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getAggregateId() {
        return aggregateId;
    }
    public String  getEventId()     { return eventId; }
    public String  getEventType()   { return eventType; }
    public String  getTag()         { return tag; }
}
