package io.ark.engine.mq.kafka.consumer;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * @author Noah Zhou
 * @description:
 * 构建 arkKafkaListenerContainerFactory：
 *   - 手动 ACK 模式
 *   - 内置重试（FixedBackOff）+ 死信队列兜底
 */
public class KafkaContainerFactoryConfigurer {

    /**
     * @param consumerFactory  Spring Kafka 自动配置的 ConsumerFactory
     * @param kafkaTemplate    用于死信投递
     * @param maxRetries       本地重试次数，超出后投死信
     * @param retryIntervalMs  本地重试间隔
     */
    public static ConcurrentKafkaListenerContainerFactory<String, String> build(
            ConsumerFactory<String, String> consumerFactory,
            KafkaTemplate<String, String> kafkaTemplate,
            int                             maxRetries,
            long                            retryIntervalMs) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        // 手动提交 offset，与 KafkaConsumerRouter 中的 ack.acknowledge() 配合
        factory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        // 重试策略：本地 N 次后投死信（topic 名 = 原 topic + ".DLT"）
        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate);

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(recoverer,
                        new FixedBackOff(retryIntervalMs, maxRetries));

        // 以下异常不重试，直接投死信（数据格式问题重试无意义）
        errorHandler.addNotRetryableExceptions(
                IllegalArgumentException.class,
                ClassCastException.class
        );

        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}
