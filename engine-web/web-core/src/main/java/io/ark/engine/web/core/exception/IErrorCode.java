package io.ark.engine.web.core.exception;

/**
 * @Description: 错误码规范接口
 * <p>各模块定义自己的错误码枚举实现此接口：
 * <pre>
 * public enum UserErrorCode implements IErrorCode {
 *     USER_NOT_FOUND(404_001, "用户不存在"),
 *     USERNAME_DUPLICATE(400_001, "用户名已存在");
 * }
 * </pre>
 * @Author: Noah Zhou
 */
public interface IErrorCode {
    int getCode();
    String getMessage();
}
