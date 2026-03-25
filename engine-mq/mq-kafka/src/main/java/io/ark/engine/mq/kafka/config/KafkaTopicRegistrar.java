package io.ark.engine.mq.kafka.config;

import io.ark.engine.mq.core.consumer.MessageConsumer;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Noah Zhou
 * @description:
 * 按需注册 NewTopic Bean，Spring Kafka 会在启动时自动调用 AdminClient 创建。
 * 优先级：ark.mq.kafka.topics 显式配置 > MessageConsumer.topic() 自动收集
 */
public class KafkaTopicRegistrar {

    private static final Logger log = LoggerFactory.getLogger(KafkaTopicRegistrar.class);

    private final KafkaMqProperties        properties;
    private final List<MessageConsumer>    consumers;
    private final BeanDefinitionRegistry registry;

    public KafkaTopicRegistrar(KafkaMqProperties properties,
                               List<MessageConsumer> consumers,
                               BeanDefinitionRegistry registry) {
        this.properties = properties;
        this.consumers  = consumers;
        this.registry   = registry;
    }

    public void registerTopics() {
        if (!properties.isAutoCreateTopics()) {
            log.info("[ark-mq-kafka] autoCreateTopics=false, skip topic creation");
            return;
        }

        // 收集显式配置的 topic 名
        Set<String> explicitNames = new HashSet<>();
        for (KafkaMqProperties.TopicConfig tc : properties.getTopics()) {
            explicitNames.add(tc.getName());
            registerNewTopicBean(
                    tc.getName(),
                    tc.getPartitions()       < 0 ? properties.getDefaultPartitions()       : tc.getPartitions(),
                    tc.getReplicationFactor() < 0 ? properties.getDefaultReplicationFactor(): tc.getReplicationFactor()
            );
        }

        // 从 MessageConsumer Bean 中自动收集补充
        for (MessageConsumer consumer : consumers) {
            String topic = consumer.topic();
            if (!explicitNames.contains(topic)) {
                explicitNames.add(topic); // 防重
                registerNewTopicBean(topic,
                        properties.getDefaultPartitions(),
                        properties.getDefaultReplicationFactor());
            }
        }
    }

    private void registerNewTopicBean(String topic, int partitions, short replication) {
        String beanName = "kafkaTopic_" + topic;
        if (registry.containsBeanDefinition(beanName)) {
            return;
        }
        NewTopic newTopic = TopicBuilder.name(topic)
                .partitions(partitions)
                .replicas(replication)
                .build();

        RootBeanDefinition bd = new RootBeanDefinition(NewTopic.class, () -> newTopic);
        registry.registerBeanDefinition(beanName, bd);
        log.info("[ark-mq-kafka] Registered NewTopic: {} (partitions={}, replicas={})",
                topic, partitions, replication);
    }
}
