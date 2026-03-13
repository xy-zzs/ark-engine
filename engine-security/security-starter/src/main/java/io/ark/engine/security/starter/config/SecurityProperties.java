package io.ark.engine.security.starter.config;

import io.ark.engine.security.core.token.JwtProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 认证配置定义
 * @Author: Noah Zhou
 */
@Data
@ConfigurationProperties(prefix = "ark.security")
public class SecurityProperties {
    @NestedConfigurationProperty
    private JwtProperties jwt =  new JwtProperties();

    private List<String> ignoreUrls = new ArrayList<>(List.of(
            "/api/auth/login",
            "/api/auth/refresh",
            "/actuator/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    ));
}
