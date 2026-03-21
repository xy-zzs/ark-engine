package io.ark.framework.exception;

/**
 * @Description: 框架级全局错误码
 *
 * <p>适用于所有模块通用的错误场景，不属于任何具体业务模块。
 * 各业务模块的错误码（如 UserErrorCode、AuthErrorCode）应自行定义，
 * 段位约定：
 * <ul>
 *   <li>0        - 成功</li>
 *   <li>1 ~ 999  - 全局/框架错误（本类）</li>
 *   <li>1000~1999 - user 模块</li>
 *   <li>2000~2999 - auth 模块</li>
 *   <li>3000~3999 - permission 模块</li>
 * </ul>
 *
 * @Author: Noah Zhou
 */
public enum GlobalErrorCode implements IErrorCode{
    // ─── 成功 ──────────────────────────────────────────────────────────────
    SUCCESS(200,             "global.success"),

    // ─── 客户端错误 4xx ────────────────────────────────────────────────────
    BAD_REQUEST(400,         "global.bad.request"),
    UNAUTHORIZED(401,        "global.unauthorized"),
    FORBIDDEN(403,           "global.forbidden"),
    NOT_FOUND(404,           "global.not.found"),
    METHOD_NOT_ALLOWED(405,  "global.method.not.allowed"),

    // ─── 业务错误 —— 统一用 4200 段，避免和 HTTP 状态码混淆 ────────────────
    BIZ_ERROR(4200,          "global.biz.error"),

    // ─── 服务端错误 5xx ────────────────────────────────────────────────────
    INTERNAL_ERROR(500,      "global.internal.error");

    private final int code;
    private final String messageKey;

    GlobalErrorCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    @Override
    public int getCode() {return code;}

    @Override
    public String getMessageKey() {return messageKey;}
}
