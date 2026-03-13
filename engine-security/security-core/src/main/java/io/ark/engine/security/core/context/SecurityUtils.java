package io.ark.engine.security.core.context;

import io.ark.engine.security.core.execption.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Description:
 *  业务代码统一通过此工具类获取当前用户
 *  禁止在业务层直接操作 SecurityContextHolder
 * @Author: Noah Zhou
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {
//    private SecurityUtils(){}

    public static SecurityUser getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof SecurityUser){
            throw new UnauthorizedException("当前请求未认证");
        }
        return (SecurityUser) authentication.getPrincipal();
    }

    public static String getCurrentUserId(){
        return getCurrentUser().getUserId();
    }
    public static boolean hasPermission(String permission){
        return getCurrentUser().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(permission));
    }

    /**
     * 微服务内部调用场景
     * 网关已验证Token，下游服务从Header取用户信息
     */
    public static String getUserIdFromHeader(HttpServletRequest request) {
        return request.getHeader("X-User-Id");
    }

    public static String getPermissionsFromHeader(HttpServletRequest request) {
        return request.getHeader("X-User-Permissions");
    }
}
