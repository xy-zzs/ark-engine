package io.ark.framework.applicaiton.event;

import io.ark.framework.domain.DomainEvent;

/**
 * @Description: 领域事件处理器接口
 * @Author: Noah Zhou
 */
public interface DomainEventHandler<E extends DomainEvent>{

    void handle(E event);

    /**
     * 返回此 Handler 感兴趣的事件类型，用于事件总线路由
     */
    Class<E> supportedEventType();
}
