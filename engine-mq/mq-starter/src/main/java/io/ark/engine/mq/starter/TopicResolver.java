package io.ark.engine.mq.starter;

import io.ark.framework.domain.DomainEvent;

/**
 * @author Noah Zhou
 * @description: topic 解析策略 SPI，可自定义替换
 */
@FunctionalInterface
public interface TopicResolver {

    String resolve(DomainEvent event);

    /** 默认：取 eventType 作为 topic */
    static TopicResolver defaults() {
        return DomainEvent::getEventType;
    }
}
