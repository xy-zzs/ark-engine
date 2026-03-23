package io.ark.engine.mq.kafka.event;

import io.ark.engine.mq.core.MqMessage;
import io.ark.engine.mq.core.MqProducer;
import io.ark.framework.domain.DomainEvent;
import io.ark.framework.domain.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Noah Zhou
 * @description:
 * <p>将 auth-domain 的领域事件转换为 auth-api 中定义的 MQ 消息对象，
 * 通过 {@link MqProducer} 发布，不感知底层是 Kafka 还是 RocketMQ。
 *
 * <p>新增领域事件类型时，在 {@link #doPublish} 中添加对应的
 * instanceof 分支和消息转换逻辑即可，调用方零改动。
 *
 * <p>消息 key 使用 userId，保证同一用户的消息路由到同一 Partition，
 * Consumer 侧按序消费，避免登录时序错乱。

 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MqDomainEventPublisher implements DomainEventPublisher {

    private final MqProducer mqProducer;

    @Override
    public void publish(DomainEvent event) {
        try {
            doPublish(event);
        } catch (Exception e) {
            log.error("Failed to publish domain event, type={}, aggregateId={}",
                    event.getClass().getSimpleName(), event.getAggregateId(), e);
            // 发布失败不抛出，不影响登录主流程
            // 依赖幂等消费 + 后续补偿机制保证最终一致性
        }
    }

    private void doPublish(DomainEvent event) {

    }
}
