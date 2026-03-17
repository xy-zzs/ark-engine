package io.ark.engine.security.starter.config;

import io.ark.engine.security.core.execption.SecurityAccessDeniedHandler;
import io.ark.engine.security.core.execption.SecurityAuthEntryPoint;
import io.ark.engine.security.core.filter.TokenAuthFilter;
import io.ark.engine.security.core.token.JwtProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

/**
 * @Description: 自动装配入口
 * @Author: Noah Zhou
 */
@AutoConfiguration
@EnableMethodSecurity
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnClass(SecurityFilterChain.class)
public class SecurityAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public JwtProvider jwtProvider(SecurityProperties securityProperties){
        return new JwtProvider(securityProperties.getJwt());
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenAuthFilter tokenAuthFilter(JwtProvider jwtProvider){
        return new TokenAuthFilter(jwtProvider);
    }

    /**
     * 默认 FilterChain
     * 业务方在自己模块中注册 SecurityFilterChain Bean 即可完全覆盖
     */
    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain defaultFilterChain(HttpSecurity httpSecurity,
                                                  TokenAuthFilter tokenAuthFilter,
                                                  SecurityProperties securityProperties,
                                                  SecurityAuthEntryPoint authEntryPoint,
                                                  SecurityAccessDeniedHandler accessDeniedHandler) throws Exception {
        // 白名单路径转数组
        String[] ignoreUrls = securityProperties.getIgnoreUrls().toArray(String[]::new);
        System.out.println("ignoreUrls:"+ Arrays.toString(ignoreUrls));
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(ignoreUrls).permitAll()
                                                                                .anyRequest().authenticated())
                .addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityAuthEntryPoint authEntryPoint(){
        return new SecurityAuthEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityAccessDeniedHandler accessDeniedHandler(){
        return new SecurityAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        // 使用BCrypt加密算法，这是Spring Security推荐的密码编码器
        return new BCryptPasswordEncoder();
    }
}