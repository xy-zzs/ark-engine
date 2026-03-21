package io.ark.framework.exception;

/**
 * @Description: 业务异常，全层通用，可在 domain / application / infrastructure 任意层抛出。
 *
 * <p>持有 {@link IErrorCode}，由 engine-web 的 {@code GlobalExceptionHandler}
 * 统一捕获，解析 messageKey 为 i18n 文案后返回标准响应体。
 *
 * <p>使用方式：
 * <pre>{@code
 * // 直接使用全局错误码
 * throw new BizException(ArkException.NOT_FOUND);
 *
 * // 使用模块错误码
 * throw new BizException(UserErrorCode.USER_NOT_FOUND);
 *
 * // 携带日志细节（不对外暴露，仅记录日志）
 * throw new BizException(UserErrorCode.USER_NOT_FOUND, "userId=" + id.value());
 * }</pre>
 * @Author: Noah Zhou
 */
public class BizException extends RuntimeException {

  private final IErrorCode errorCode;

  /** 补充细节，仅供日志记录，不返回给前端 */
  private final String detail;

  public BizException(IErrorCode errorCode) {
    super(errorCode.getMessageKey());
    this.errorCode = errorCode;
    this.detail = null;
  }

  public BizException(IErrorCode errorCode, String detail) {
    super(errorCode.getMessageKey());
    this.errorCode = errorCode;
    this.detail = detail;
  }

  public BizException(IErrorCode errorCode, Throwable cause) {
    super(errorCode.getMessageKey(), cause);
    this.errorCode = errorCode;
    this.detail = null;
  }

  public IErrorCode getErrorCode() { return errorCode; }
  public String getDetail() { return detail; }
}
