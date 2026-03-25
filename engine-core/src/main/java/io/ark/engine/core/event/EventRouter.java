package io.ark.engine.core.event;

import io.ark.framework.domain.DomainEvent;

/**
 * @description:
 * 路由决策 SPI — 可按事件类型返回不同策略
 * 默认实现：统一走配置文件中的全局策略
 * @author Noah Zhou
 */
@FunctionalInterface
public interface EventRouter {
    EventRoutingStrategy route(DomainEvent event);
}
