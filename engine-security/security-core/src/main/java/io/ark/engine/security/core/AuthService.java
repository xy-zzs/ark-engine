package io.ark.engine.security.core;

/**
 * @Description: 认证接口类
 * @Author: Noah Zhou
 */
public interface AuthService {

    LoginResult login(String username, String password);

    void logout();

    String getUserId();

    boolean hasRole(String role);

    boolean hasPermission(String permission);
}
