package io.ark.engine.security.starter.config;

import io.ark.engine.security.core.AuthService;
import io.ark.engine.security.satoken.SaTokenAuthService;
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
        havingValue = "satoken",
        matchIfMissing = true
)
public class SaTokenAutoConfiguration {
    @Bean
    public AuthService authService() {
        return new SaTokenAuthService();
    }
}
