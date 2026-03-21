package io.ark.engine.mq.core.exception;

/**
 * @author Noah Zhou
 * @description: MQ 发送异常，由具体实现层抛出，业务层统一捕获此类型。
 */
public class MqSendException extends RuntimeException{
    public MqSendException(String message) {
        super(message);
    }

    public MqSendException(String message, Throwable cause) {
        super(message, cause);
    }

}
