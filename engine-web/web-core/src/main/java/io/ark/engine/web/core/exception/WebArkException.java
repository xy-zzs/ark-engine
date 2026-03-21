package io.ark.engine.web.core.exception;

import io.ark.framework.exception.IErrorCode;
import lombok.Getter;

/**
 * @Description: Ark 框架业务异常基类
 * <p>各模块异常继承此类，携带结构化错误码：
 * <pre>
 * // user-domain
 * public class UserException extends ArkException {
 *     public UserException(UserErrorCode errorCode) {
 *         super(errorCode);
 *     }
 * }
 *
 * // 使用
 * throw new UserException(UserErrorCode.USER_NOT_FOUND);
 * </pre>
 * @Author: Noah Zhou
 */
@Getter
public class WebArkException extends RuntimeException{

    private final int code;
    /**
     * i18n 消息键，由 GlobalExceptionHandler 负责翻译
     * 存原始 key 而非翻译结果，原因：异常构造时可能还没有 locale 上下文
     */
    private final String messageKey;

    /**
     * 支持消息占位符参数，如 "user.not.found.with.name" = "用户 {0} 不存在"
     * throw new UserException(UserErrorCode.USER_NOT_FOUND, username)
     */
    private final Object[] args;

    public WebArkException(IErrorCode errorCode, Object... args) {
        // super message 存 key，方便日志直接看到 key
        super(errorCode.getMessageKey());
        this.code = errorCode.getCode();
        this.messageKey = errorCode.getMessageKey();
        this.args = args;
    }

    public WebArkException(int code, String messageKey, Object... args) {
        super(messageKey);
        this.code = code;
        this.messageKey = messageKey;
        this.args = args;
    }

    public static WebArkException of(IErrorCode errorCode, Object... args) {
        return new WebArkException(errorCode, args);
    }
}
