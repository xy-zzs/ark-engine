package io.ark.engine.mq.core.consumer;

import io.ark.engine.mq.core.message.MqMessage;

import java.util.List;

/**
 * @author Noah Zhou
 * @description: 拦截器链执行器，框架内部使用
 */
public class ConsumeContext {

    private final List<ConsumeInterceptor> interceptors;
    private final MessageConsumer          target;

    public ConsumeContext(List<ConsumeInterceptor> interceptors, MessageConsumer target) {
        // 按 order() 排序
        this.interceptors = interceptors.stream()
                .sorted(java.util.Comparator.comparingInt(ConsumeInterceptor::order))
                .toList();
        this.target = target;
    }

    public void execute(MqMessage message) {
        Throwable error = null;
        for (ConsumeInterceptor interceptor : interceptors) {
            if (!interceptor.preConsume(message)) {
                return; // 短路（幂等命中）
            }
        }
        try {
            target.consume(message);
        } catch (Throwable t) {
            error = t;
            throw t;
        } finally {
            Throwable finalError = error;
            for (ConsumeInterceptor interceptor : interceptors) {
                interceptor.afterConsume(message, finalError);
            }
        }
    }
}
