package io.ark.engine.security.core.execption;

/**
 * @Description:
 * 未认证异常（401）
 * 框架级异常，业务层直接使用，不需要自定义
 * @Author: Noah Zhou
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
