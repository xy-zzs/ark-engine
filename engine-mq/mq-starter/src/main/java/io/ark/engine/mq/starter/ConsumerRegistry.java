package io.ark.engine.mq.starter;

import io.ark.engine.mq.core.consumer.ConsumeContext;
import io.ark.engine.mq.core.consumer.ConsumeInterceptor;
import io.ark.engine.mq.core.consumer.MessageConsumer;
import io.ark.engine.mq.core.message.MqMessage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Noah Zhou
 * @description:
 * 扫描所有 MessageConsumer Bean，按 topic#tag 建立路由表
 * MQ 具体实现（Kafka/RocketMQ）收到消息后调用 dispatch()
 */
public class ConsumerRegistry {

    /** key: "topic#tag"，value: 带拦截器链的执行上下文 */
    private final Map<String, ConsumeContext> routeTable;

    public ConsumerRegistry(List<MessageConsumer> consumers,
                            List<ConsumeInterceptor> interceptors) {
        this.routeTable = consumers.stream().collect(
                Collectors.toMap(
                        c -> routeKey(c.topic(), c.tag()),
                        c -> new ConsumeContext(interceptors, c)
                )
        );
    }

    public void dispatch(MqMessage message) {
        // 先精确匹配 topic#tag，再尝试 topic#*
        ConsumeContext ctx = routeTable.get(routeKey(message.topic(), message.tag()));
        if (ctx == null) {
            ctx = routeTable.get(routeKey(message.topic(), "*"));
        }
        if (ctx != null) {
            ctx.execute(message);
        }
        // 未找到消费者：框架可发出警告日志，也可注册死信处理器
    }

    private static String routeKey(String topic, String tag) {
        return topic + "#" + tag;
    }
}
