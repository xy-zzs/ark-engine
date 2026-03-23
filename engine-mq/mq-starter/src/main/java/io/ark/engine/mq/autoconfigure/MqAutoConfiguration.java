package io.ark.engine.mq.autoconfigure;

import io.ark.engine.mq.core.MqProducer;
import io.ark.engine.mq.kafka.KafkaMqProducer;
import io.ark.engine.mq.kafka.event.MqDomainEventPublisher;
import io.ark.framework.domain.DomainEventPublisher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author Noah Zhou
 * @description:
 * <p>{@code @ConditionalOnClass} 保证只有 classpath 存在
 * {@code KafkaTemplate} 时才激活 Kafka 实现。
 * 后续新增 RocketMQ 实现时，添加同级的 {@code @ConditionalOnClass(RocketMQTemplate.class)}
 * 配置类即可，无需修改此类。

 */
@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
public class MqAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MqProducer.class)
    public MqProducer mqProducer(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaMqProducer(kafkaTemplate);
    }

    /**
     * 覆盖 engine-core 默认的 SpringDomainEventPublisher。
     *
     * <p>微服务模式下领域事件必须走 MQ 才能跨服务传递，
     * {@code @Primary} 保证 Repository 注入此实现而非默认实现。
     */
    @Bean
    @Primary
    public DomainEventPublisher domainEventPublisher(MqProducer mqProducer) {
        return new MqDomainEventPublisher(mqProducer);
    }

}
