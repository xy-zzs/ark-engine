package io.ark.engine.security.starter.config;

import io.ark.engine.security.core.AuthService;
import io.ark.engine.security.sas.OAuth2AuthService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: Noah Zhou
 */
@Configuration
@ConditionalOnProperty(
        prefix = "ark.auth",
        name = "type",
        havingValue = "oauth2"
)
public class OAuth2AutoConfiguration {

    @Bean
    public AuthService authService() {
        return new OAuth2AuthService();
    }
}