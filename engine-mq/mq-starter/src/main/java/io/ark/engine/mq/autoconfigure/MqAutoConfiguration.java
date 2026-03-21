package io.ark.engine.mq.autoconfigure;

import io.ark.engine.mq.core.MqProducer;
import io.ark.engine.mq.kafka.KafkaMqProducer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
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

}
