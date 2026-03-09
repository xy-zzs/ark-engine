package io.ark.engine.security.satoken;

import cn.dev33.satoken.stp.StpUtil;
import io.ark.engine.security.core.AuthService;
import io.ark.engine.security.core.LoginResult;

/**
 * @Description: Sa-token认证实现
 * @Author: Noah Zhou
 */
public class SaTokenAuthService implements AuthService {
    @Override
    public LoginResult login(String username, String password) {
        StpUtil.login(username);
        StpUtil.getTokenValue();
        return new LoginResult();
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public String getUserId() {
        return StpUtil.getLoginIdAsString();
    }

    @Override
    public boolean hasRole(String role) {
        return false;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }
}
