package io.ark.engine.mq.core.consumer;

import io.ark.engine.mq.core.message.MqMessage;

/**
 * @author Noah Zhou
 * @description: 消费端 SPI
 * 框架自动扫描所有 Bean 并按 topic()/tag() 路由
 */
public interface MessageConsumer {
    /** 消费逻辑，框架保证幂等拦截和重试后才调用此方法 */
    void consume(MqMessage message);
    String topic();
    default String tag() { return "*"; }
}
