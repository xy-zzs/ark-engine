package io.ark.engine.core.event;

import io.ark.engine.core.config.properties.EventBusProperties;
import io.ark.framework.domain.DomainEvent;

/**
 * @author Noah Zhou
 * @description: 全局统一策略路由，可被业务自定义 Bean 覆盖
 */
public class DefaultEventRouter implements EventRouter{
    private final EventBusProperties properties;

    public DefaultEventRouter(EventBusProperties properties) {
        this.properties = properties;
    }

    @Override
    public EventRoutingStrategy route(DomainEvent event) {
        return properties.getDefaultStrategy();
    }
}
