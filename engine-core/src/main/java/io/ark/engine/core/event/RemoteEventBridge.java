package io.ark.engine.core.event;

import io.ark.framework.domain.DomainEvent;

/**
 * @author Noah Zhou
 * @description: 远程投递桥接 SPI
 * engine-core 定义接口，mq-starter 提供实现
 * engine-core 本身不依赖任何 MQ
 */
public interface RemoteEventBridge {
    void send(DomainEvent event);
}
