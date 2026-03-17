package io.ark.engine.web.core.result;

import io.ark.engine.web.core.exception.GlobalErrorCode;
import io.ark.engine.web.core.exception.IErrorCode;
import lombok.Getter;

/**
 * @Description:  统一响应体
 *
 * <p>所有接口返回此结构：
 * <pre>
 * {
 *   "code": 200,
 *   "message": "成功",
 *   "data": { ... }
 * }
 * </pre>
 * @Author: Noah Zhou
 */
@Getter
public class Result<T> {
    private int code;
    private String message;
    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ─── 成功 ──────────────────────────────────────────────────────────────

    public static <T> Result<T> ok(T data) {
        return new Result<>(GlobalErrorCode.SUCCESS.getCode(),
                GlobalErrorCode.SUCCESS.getMessage(), data);
    }

    public static Result<Void> ok() {
        return new Result<>(GlobalErrorCode.SUCCESS.getCode(),
                GlobalErrorCode.SUCCESS.getMessage(), null);
    }

    // ─── 失败 ──────────────────────────────────────────────────────────────

    public static <T> Result<T> fail(IErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public boolean isSuccess() {
        return this.code == GlobalErrorCode.SUCCESS.getCode();
    }
}
