package io.ark.engine.web.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Description: web 内置的通用错误码
 * @Author: Noah Zhou
 */
@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements IErrorCode{
    // ─── 成功 ──────────────────────────────────────────────────────────────
    SUCCESS(200, "成功"),

    // ─── 客户端错误 4xx ────────────────────────────────────────────────────
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或 token 已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    // ─── 业务错误 —— 统一用 4200 段，避免和 HTTP 状态码混淆 ────────────────
    BIZ_ERROR(4200, "业务异常"),

    // ─── 服务端错误 5xx ────────────────────────────────────────────────────
    INTERNAL_ERROR(500, "服务器内部错误");

    private final int code;
    private final String message;
}
