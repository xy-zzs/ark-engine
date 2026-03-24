package io.ark.engine.core.config;

import io.ark.engine.core.config.properties.EventBusProperties;
import io.ark.engine.core.event.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

/**
 * @author Noah Zhou
 * @description:
 */
@AutoConfiguration
@EnableConfigurationProperties(EventBusProperties.class)
public class EventBusAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EventRouter eventRouter(EventBusProperties properties) {
        return new DefaultEventRouter(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventBus eventBus(ApplicationEventPublisher springPublisher,
                             EventRouter router,
                             // Spring 自动将 Optional<X> 注入为 empty（若无该Bean）
                             Optional<RemoteEventBridge> remoteBridge) {
        return new DefaultEventBus(springPublisher, router, remoteBridge);
    }
}
