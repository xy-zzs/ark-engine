package io.ark.framework.exception;

/**
 * @Description: 业务异常基类
 *
 * <p>所有模块的业务异常必须继承此类，而非直接使用 {@link RuntimeException}。
 * 这样 {@code GlobalExceptionHandler} 可以统一捕获处理，无需为每个模块单独添加 catch。
 *
 * <p>各模块定义自己的子类：
 * <pre>{@code
 * public class UserException extends ArkException {
 *     public UserException(IErrorCode errorCode) {
 *         super(errorCode);
 *     }
 *     public UserException(IErrorCode errorCode, String detail) {
 *         super(errorCode, detail);
 *     }
 * }
 * }</pre>
 *
 * <p>使用方式：
 * <pre>{@code
 * throw new UserException(UserErrorCode.USER_NOT_FOUND);
 * throw new UserException(UserErrorCode.USER_NOT_FOUND, "userId=" + id.value());
 * }</pre>
 * @Author: Noah Zhou
 */
public class ArkException extends RuntimeException {

    /**
     * 错误码，携带 code 和 messageKey
     */
    private final IErrorCode errorCode;

    /**
     * 可选的补充细节信息（不对外展示，用于日志记录）
     */
    private final String detail;

    public ArkException(IErrorCode errorCode) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.detail = null;
    }

    public ArkException(IErrorCode errorCode, String detail) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.detail = detail;
    }

    public ArkException(IErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessageKey(), cause);
        this.errorCode = errorCode;
        this.detail = null;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDetail() {
        return detail;
    }
}
