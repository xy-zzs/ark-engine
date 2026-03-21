package io.ark.engine.core.context;

/**
 * @Description: 当前登录用户上下文载体
 *
 * <p>由 engine-security 的 Token 过滤器解析 JWT 后填充，
 * 业务代码通过 {@link LoginUserContext#get()} 获取，无需从请求头手动解析。
 *
 * <p>只存放鉴权必要的轻量信息，不存放完整用户实体，避免频繁查库。
 * @Author: Noah Zhou
 */
public class LoginUser {

    /** 用户 ID */
    private final Long userId;

    /** 用户名（登录名） */
    private final String username;

    /**
     * 租户 ID，多租户场景使用；单租户项目可忽略
     */
    private final String tenantId;

    public LoginUser(Long userId, String username, String tenantId) {
        this.userId = userId;
        this.username = username;
        this.tenantId = tenantId;
    }

    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getTenantId() { return tenantId; }
}
