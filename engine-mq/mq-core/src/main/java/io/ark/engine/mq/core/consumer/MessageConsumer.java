package io.ark.engine.mq.core.consumer;

import io.ark.engine.mq.core.message.MqMessage;
import io.ark.framework.applicaiton.event.DomainEventHandler;

/**
 * @author Noah Zhou
 * @description: 消费端 SPI
 * 框架自动扫描所有 Bean 并按 topic()/tag() 路由
 */
public interface MessageConsumer extends DomainEventHandler<MqMessage> {
    /** 消费逻辑，框架保证幂等拦截和重试后才调用此方法 */
    void consume(MqMessage message);
    String topic();
    default String tag() { return "*"; }

    // handle 委托给 consume，子类只需实现 consume
    @Override
    default void handle(MqMessage event) {
        consume(event);
    }

    @Override
    default Class<MqMessage> supportedEventType() {
        return MqMessage.class;
    }
}
