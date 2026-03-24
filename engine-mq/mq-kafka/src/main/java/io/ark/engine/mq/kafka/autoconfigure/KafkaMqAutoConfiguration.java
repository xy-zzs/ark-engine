package io.ark.engine.mq.kafka.autoconfigure;

import io.ark.engine.mq.core.consumer.MessageConsumer;
import io.ark.engine.mq.core.sender.MessageSender;
import io.ark.engine.mq.kafka.config.KafkaMqProperties;
import io.ark.engine.mq.kafka.config.KafkaTopicRegistrar;
import io.ark.engine.mq.kafka.consumer.KafkaConsumerRouter;
import io.ark.engine.mq.kafka.consumer.KafkaContainerFactoryConfigurer;
import io.ark.engine.mq.kafka.consumer.KafkaTopicProvider;
import io.ark.engine.mq.kafka.sender.KafkaMessageSender;
import io.ark.engine.mq.starter.ConsumerRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

/**
 * @author Noah Zhou
 * @description:
 */
@AutoConfiguration
@EnableConfigurationProperties(KafkaMqProperties.class)
public class KafkaMqAutoConfiguration {

    // ── 发送端 ────────────────────────────────────────────────────

    @Bean
    @ConditionalOnMissingBean(MessageSender.class)
    public MessageSender kafkaMessageSender(KafkaTemplate<String, String> kafkaTemplate,
                                            KafkaMqProperties properties) {
        return new KafkaMessageSender(kafkaTemplate, properties);
    }

    // ── 消费端 ────────────────────────────────────────────────────

    @Bean
    public KafkaTopicProvider kafkaTopicProvider(List<MessageConsumer> consumers) {
        return new KafkaTopicProvider(consumers);
    }

    /**
     * 注册 NewTopic Bean，驱动 Kafka AdminClient 自动建 topic
     * 需要拿到 BeanDefinitionRegistry，所以实现 BeanDefinitionRegistryPostProcessor
     * 这里通过 @Bean 方法触发，在 ApplicationContext refresh 阶段执行
     */
    @Bean
    public KafkaTopicRegistrar kafkaTopicRegistrar(KafkaMqProperties properties,
                                                   List<MessageConsumer> consumers,
                                                   BeanDefinitionRegistry registry) {
        KafkaTopicRegistrar registrar = new KafkaTopicRegistrar(properties, consumers, registry);
        registrar.registerTopics();
        return registrar;
    }

    @Bean("arkKafkaListenerContainerFactory")
    @ConditionalOnMissingBean(name = "arkKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> arkKafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            KafkaTemplate<String, String>   kafkaTemplate,
            KafkaMqProperties               properties) {
        return KafkaContainerFactoryConfigurer.build(
                consumerFactory,
                kafkaTemplate,
                3,      // 本地重试 3 次后投死信，后续可提取到 properties
                1000L
        );
    }

    @Bean
    public KafkaConsumerRouter kafkaConsumerRouter(ConsumerRegistry consumerRegistry) {
        return new KafkaConsumerRouter(consumerRegistry);
    }
}
