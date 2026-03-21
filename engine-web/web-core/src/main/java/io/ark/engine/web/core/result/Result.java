package io.ark.engine.web.core.result;

import io.ark.engine.web.core.exception.WebErrorCode;
import io.ark.framework.exception.IErrorCode;
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
        return new Result<>(WebErrorCode.SUCCESS.getCode(),
                WebErrorCode.SUCCESS.getMessageKey(), data);
    }

    public static Result<Void> ok() {
        return new Result<>(WebErrorCode.SUCCESS.getCode(),
                WebErrorCode.SUCCESS.getMessageKey(), null);
    }

    // ─── 失败 ──────────────────────────────────────────────────────────────

    public static <T> Result<T> fail(IErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessageKey(), null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public boolean isSuccess() {
        return this.code == WebErrorCode.SUCCESS.getCode();
    }
}
