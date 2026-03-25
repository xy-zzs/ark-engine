package io.ark.framework.applicaiton.event;

import io.ark.framework.domain.DomainEvent;

/**
 * @Description: 领域事件发布接口（outbound port）。
 *
 * 业务模块可在 infrastructure 层覆盖实现。
 * 禁止在 domain 层或 application 层直接调用。
 *
 * @Author: Noah Zhou
 */
public interface DomainEventPublisher {

    /**
     * 发布单个领域事件
     *
     * @param event 领域事件，不可为 null
     */
    void publish(DomainEvent event);

    /**
     * 批量发布领域事件（聚合根收集的事件列表）
     */
    default void publishAll(Iterable<? extends DomainEvent> events) {
        events.forEach(this::publish);
    }
}
