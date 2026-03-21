package io.ark.framework.domain;


import java.time.LocalDateTime;

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

    /**
     * 事件发送时间，创建时自动赋值
     */
    private final LocalDateTime occurredAt;

    /**
     * 触发此事件的聚合根 ID（字符串化，便于跨类型通用）
     */
    private final String aggregateId;

    protected DomainEvent(String aggregateId) {
        this.aggregateId = aggregateId;
        this.occurredAt = LocalDateTime.now();
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public String getAggregateId() {
        return aggregateId;
    }
}
