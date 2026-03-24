package io.ark.engine.core.event;

import io.ark.framework.domain.DomainEvent;

/**
 * @description: 事件总线门面 — 业务代码唯一依赖点
 *
 * 路由策略由 EventRouter 决定：
 *   LOCAL  → 直接走 Spring ApplicationEvent
 *   REMOTE → 委托给 RemoteEventBridge（由 mq-starter 注入）
 *   BOTH   → 同时投递
 *
 * @author Noah Zhou
 */
public interface EventBus {
    void publish(DomainEvent event);
    /** 事务提交后再发布，防止事务回滚后事件已投出 */
    void publishAfterCommit(DomainEvent event);
}
