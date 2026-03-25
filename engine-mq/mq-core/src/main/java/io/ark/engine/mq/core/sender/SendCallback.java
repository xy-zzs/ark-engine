package io.ark.engine.mq.core.sender;

/**
 * @author Noah Zhou
 * @description:
 */
public interface SendCallback {
    void onSuccess(String messageId);
    void onException(String messageId, Throwable cause);
}
