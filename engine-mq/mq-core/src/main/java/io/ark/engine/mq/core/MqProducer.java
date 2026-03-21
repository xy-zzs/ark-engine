package io.ark.engine.mq.core;

import java.util.concurrent.CompletableFuture;

/**
 * @author Noah Zhou
 * @description: MQ 消息发送抽象接口
 * <p>业务层统一依赖此接口，不直接使用 KafkaTemplate / RocketMQTemplate 等，
 * 切换 MQ 实现时业务代码零改动。
 *
 * <p>提供同步和异步两种发送方式：
 * <ul>
 *   <li>{@link #syncSend}  - 同步发送，等待 Broker 确认，可靠性高</li>
 *   <li>{@link #asyncSend} - 异步发送，不阻塞主流程，吞吐量高</li>
 * </ul>
 *
 * <p>消息 key 用于 Partition 路由（Kafka）或顺序消费（RocketMQ），
 * 业务上需要保证同一实体消息有序时传入实体ID（如 userId）。
 */
public interface MqProducer {

    /**
     * 同步发送，等待 Broker ACK 后返回。
     *
     * @param message 消息对象，topic/tag 已在消息内携带
     * @param key     消息路由 key，相同 key 路由到同一 Partition，可为 null
     */
    void syncSend(MqMessage message, String key);

    /**
     * 异步发送，不阻塞主流程。
     *
     * @param message 消息对象
     * @param key     消息路由 key，可为 null
     * @return Future，可用于监听发送结果
     */
    CompletableFuture<Void> asyncSend(MqMessage message, String key);
}
