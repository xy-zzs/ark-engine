package io.ark.engine.mq.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ark.engine.core.event.RemoteEventBridge;
import io.ark.engine.mq.core.consumer.ConsumeInterceptor;
import io.ark.engine.mq.core.consumer.IdempotentInterceptor;
import io.ark.engine.mq.core.consumer.IdempotentStore;
import io.ark.engine.mq.core.consumer.MessageConsumer;
import io.ark.engine.mq.core.sender.MessageSender;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author Noah Zhou
 * @description:
 */
@AutoConfiguration
@ConditionalOnBean(MessageSender.class)
public class MqAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TopicResolver topicResolver() {
        return TopicResolver.defaults();
    }

    /** 注册后 engine-core 的 DefaultEventBus 会自动注入 RemoteEventBridge */
    @Bean
    @ConditionalOnMissingBean(RemoteEventBridge.class)
    public RemoteEventBridge remoteEventBridge(MessageSender sender,
                                               ObjectMapper mapper,
                                               TopicResolver resolver) {
        return new MqRemoteEventBridge(sender, mapper, resolver);
    }

    /** 幂等拦截器：仅在存在 IdempotentStore Bean 时才注册 */
    @Bean
    @ConditionalOnBean(IdempotentStore.class)
    @ConditionalOnMissingBean(IdempotentInterceptor.class)
    public IdempotentInterceptor idempotentInterceptor(IdempotentStore store) {
        return new IdempotentInterceptor(store, 86400L); // 默认24h
    }

    @Bean
    @ConditionalOnMissingBean
    public ConsumerRegistry consumerRegistry(List<MessageConsumer> consumers,
                                             List<ConsumeInterceptor> interceptors) {
        return new ConsumerRegistry(consumers, interceptors);
    }
}
