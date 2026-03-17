package io.ark.engine.web.core.exception;

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
public class ArkException extends RuntimeException{

    private final int code;

    public ArkException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public ArkException(int code, String  message) {
        super(message);
        this.code = code;
    }

    /** 快速抛出通用业务异常（无需定义具体异常类） */
    public static ArkException of(int code, String message) {
        return new ArkException(code, message);
    }
    public static ArkException of(IErrorCode errorCode) {
        return new ArkException(errorCode);
    }
}
