package io.ark.framework.applicaiton.event.impl;

import io.ark.framework.applicaiton.event.DomainEventPublisher;
import io.ark.framework.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Noah Zhou
 */
@Component
@ConditionalOnMissingBean(DomainEventPublisher.class) // 有 MQ 实现时自动退位
@RequiredArgsConstructor
public class ApplicationEventDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }
}



