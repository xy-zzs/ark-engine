package io.ark.engine.mq.core.consumer;

import io.ark.engine.mq.core.message.MqMessage;

/**
 * @author Noah Zhou
 * @description: 消费拦截器 SPI
 * 内置实现：IdempotentInterceptor / RetryInterceptor
 * 业务可自定义扩展
 */
public interface ConsumeInterceptor {
    /**
     * @return true  → 继续执行后续拦截器和消费逻辑
     *         false → 短路，不执行消费（幂等命中时使用）
     */
    boolean preConsume(MqMessage message);
    default void afterConsume(MqMessage message, Throwable error) {}
    default int order() { return 0; }

}
