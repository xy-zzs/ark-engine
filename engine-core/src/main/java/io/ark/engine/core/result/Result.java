package io.ark.engine.core.result;

/**
 * @Description: 统一 HTTP 响应体
 *
 * <p>所有 REST 接口的返回值必须包装为 {@code Result<T>}，
 * 由 engine-web 的 {@code GlobalExceptionHandler} 在异常时构造失败响应，
 * 正常响应由 Controller 或切面构造。
 *
 * <p>成功响应：{@code Result.ok(data)}
 * 失败响应：{@code Result.fail(errorCode, message)}
 *
 * @param <T> 业务数据类型
 * @Author: Noah Zhou
 */
public class Result<T> {

    /** 成功状态码约定为 0 */
    private static final int SUCCESS_CODE = 0;

    private final int code;
    private final String message;
    private final T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ---- 工厂方法 ----

    public static <T> Result<T> ok() {
        return new Result<>(SUCCESS_CODE, "success", null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(SUCCESS_CODE, "success", data);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    // ---- Getter ----

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }

    public boolean isSuccess() { return this.code == SUCCESS_CODE; }
}
