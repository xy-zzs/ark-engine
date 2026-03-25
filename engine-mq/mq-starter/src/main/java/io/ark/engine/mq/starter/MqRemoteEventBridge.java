package io.ark.engine.mq.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ark.engine.core.event.RemoteEventBridge;
import io.ark.engine.mq.core.message.MqMessage;
import io.ark.engine.mq.core.sender.MessageSender;
import io.ark.framework.domain.DomainEvent;

import java.time.Instant;

/**
 * @author Noah Zhou
 * @description:
 */
public class MqRemoteEventBridge implements RemoteEventBridge {

    private final MessageSender sender;
    private final ObjectMapper objectMapper;
    private final TopicResolver  topicResolver;

    public MqRemoteEventBridge(MessageSender sender,
                               ObjectMapper objectMapper,
                               TopicResolver topicResolver) {
        this.sender        = sender;
        this.objectMapper  = objectMapper;
        this.topicResolver = topicResolver;
    }

    @Override
    public void send(DomainEvent event) {
        try {
            MqMessage msg = new MqMessage(
                    event.getEventId(),
                    topicResolver.resolve(event),
                    event.getTag(),
                    event.getEventType(),
                    objectMapper.writeValueAsString(event),
                    Instant.now().toEpochMilli(),
                    0
            );
            sender.send(msg);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send event to MQ: " + event.getEventId(), e);
        }
    }

}
