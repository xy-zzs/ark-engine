package io.ark.framework.exception;

/**
 * @Description: web 内置的通用错误码
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
