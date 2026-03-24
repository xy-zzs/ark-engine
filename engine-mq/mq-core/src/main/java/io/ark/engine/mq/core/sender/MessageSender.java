package io.ark.engine.mq.core.sender;

import io.ark.engine.mq.core.message.MqMessage;

/**
 * @author Noah Zhou
 * @description:
 */
public interface MessageSender {
    void send(MqMessage message);
    default void sendAsync(MqMessage message, SendCallback callback) {
        try {
            send(message);
            callback.onSuccess(message.messageId());
        } catch (Exception e) {
            callback.onException(message.messageId(), e);
        }
    }
}
