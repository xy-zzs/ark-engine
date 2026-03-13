package io.ark.engine.framework.web.domain;

/**
 * @Description: 统一响应体
 * @Author: Noah Zhou
 */
public record Result<T>(String code,
                        String message,
                        T data
) {
    public static <T> Result<T> ok(T data) {
        return new Result<>("200", "success", data);
    }

    public static <T> Result<T> fail(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }
}
