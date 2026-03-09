package io.ark.engine.security.sas;

import io.ark.engine.security.core.AuthService;
import io.ark.engine.security.core.LoginResult;

/**
 * @Description:
 * @Author: Noah Zhou
 */
public class OAuth2AuthService implements AuthService {
    @Override
    public LoginResult login(String username, String password) {
        return null;
    }

    @Override
    public void logout() {

    }

    @Override
    public String getUserId() {
        return "";
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
