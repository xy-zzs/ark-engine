package io.ark.engine.security.core.token;

import java.util.Set;

/**
 * @Description: Claims封装（标准字段定义）,全局统一标准
 * @Author: Noah Zhou
 */
public record TokenClaims(String userId,
                          String username,
                          Set<String> permissions,
                          String clientType,          // WEB / APP
                          String tokenType           // ACCESS / REFRESH
) {
    // 标准字段 key，防止各处魔法字符串
    public static final String USER_ID      = "uid";
    public static final String USERNAME     = "username";
    public static final String PERMISSIONS  = "perms";
    public static final String CLIENT_TYPE  = "clientType";
    public static final String TOKEN_TYPE   = "tokenType";

    // 复制当前claims，替换tokenType（生成RefreshToken时用）
    public TokenClaims withTokenType(String newTokenType) {
        return new TokenClaims(
                this.userId, this.username, this.permissions,
                this.clientType, newTokenType);
    }
}
