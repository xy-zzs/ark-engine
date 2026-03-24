package io.ark.engine.mq.core.consumer;

import io.ark.engine.mq.core.message.MqMessage;

/**
 * @author Noah Zhou
 * @description:
 */
public class IdempotentInterceptor implements ConsumeInterceptor{

    private final IdempotentStore store;
    private final long            expireSeconds;

    public IdempotentInterceptor(IdempotentStore store, long expireSeconds) {
        this.store         = store;
        this.expireSeconds = expireSeconds;
    }

    @Override
    public boolean preConsume(MqMessage message) {
        boolean first = store.tryMark(message.messageId(), expireSeconds);
        if (!first) {
            // 重复消息直接短路
            return false;
        }
        return true;
    }

    @Override
    public void afterConsume(MqMessage message, Throwable error) {
        if (error != null) {
            // 消费失败，回滚幂等标记，允许重试
            store.remove(message.messageId());
        }
    }

    @Override
    public int order() { return -100; } // 最先执行
}
