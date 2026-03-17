package io.ark.engine.security.core.filter;

import io.ark.engine.security.core.context.SecurityUser;
import io.ark.engine.security.core.token.JwtProvider;
import io.ark.engine.security.core.token.TokenClaims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * @Description: 从Header解析Token→SecurityContext
 * @Author: Noah Zhou
 */
@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        try {
            if (StringUtils.hasText(token)){
                TokenClaims tokenClaims = jwtProvider.parseToken(token);
                List<SimpleGrantedAuthority> authorities = tokenClaims.permissions().stream().map(SimpleGrantedAuthority::new).toList();

                SecurityUser securityUser = new SecurityUser(tokenClaims.userId(), tokenClaims.username(), authorities);

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(securityUser, null, authorities));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Token无效：清空上下文，继续走（后续鉴权会拦截）
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request,response);
    }

    private String extractToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            System.out.println("Token未携带");
            return null;
        }
        if(StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")){
            return authorization.substring(7);
        }
        System.out.println("Token格式错误");
        return null;
    }
}
