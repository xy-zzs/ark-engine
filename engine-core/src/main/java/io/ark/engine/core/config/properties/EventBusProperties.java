package io.ark.engine.core.config.properties;

import io.ark.engine.core.event.EventRoutingStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Noah Zhou
 * @description:
 */
@ConfigurationProperties(prefix = "ark.event")
public class EventBusProperties {
    /** 全局默认路由策略 */
    private EventRoutingStrategy defaultStrategy = EventRoutingStrategy.LOCAL;

    public EventRoutingStrategy getDefaultStrategy() { return defaultStrategy; }
    public void setDefaultStrategy(EventRoutingStrategy s) { this.defaultStrategy = s; }
}
