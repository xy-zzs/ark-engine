package io.ark.engine.security.core.context;

import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @Description: SecurityContext中存的用户信息
 * @Author: Noah Zhou
 */
@AllArgsConstructor
public class SecurityUser implements UserDetails{

    private final String userId;
    private final String username;
//    private final String clientType;
    private final List<? extends GrantedAuthority> authorities;


    public String getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
