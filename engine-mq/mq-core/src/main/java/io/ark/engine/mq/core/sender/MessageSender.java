package io.ark.engine.mq.core.sender;

import io.ark.engine.mq.core.message.MqMessage;
import io.ark.framework.applicaiton.event.DomainEventPublisher;

/**
 * @author Noah Zhou
 * @description:
 */
public interface MessageSender extends DomainEventPublisher {
    void send(MqMessage message);
    default void sendAsync(MqMessage message, SendCallback callback) {
        try {
            send(message);
            callback.onSuccess(message.getMessageId());
        } catch (Exception e) {
            callback.onException(message.getMessageId(), e);
        }
    }
}
