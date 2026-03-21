package io.ark.engine.core.event;

import io.ark.framework.domain.DomainEvent;
import io.ark.framework.domain.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @Description: 基于 Spring ApplicationEventPublisher 的领域事件发布实现。
 *
 * <p>将 {@link DomainEvent} 直接作为 Spring 事件发布，
 * 同进程内的监听器（{@code @EventListener}）可直接接收，
 * 适用于单体部署模式。
 *
 * <p>微服务模式下如需发布到 MQ，各业务模块可在 infrastructure 层
 * 提供自定义实现覆盖此 Bean，本类作为默认兜底。
 *
 * <p>注册方式：由 engine-core 的 AutoConfiguration 注册为 Bean，
 * 业务模块无需手动声明。
 * @Author: Noah Zhou
 */
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 发布领域事件。
     * 同步发布，监听器在同一线程内执行。
     * 若需异步，监听器侧加 {@code @Async} 即可，发布方无需改动。
     */
    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
