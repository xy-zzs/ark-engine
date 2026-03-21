package io.ark.engine.core.config;

import io.ark.engine.core.event.SpringDomainEventPublisher;
import io.ark.engine.core.i18n.MessageSourceResolver;
import io.ark.engine.core.id.SnowflakeIdGenerator;
import io.ark.framework.i18n.MessageResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

/**
 * @Description:  * engine-core 自动配置类。
 * 注册 DomainEventPublisher 和 SnowflakeIdGenerator 两个基础 Bean。
 * @Author: Noah Zhou
 */
@AutoConfiguration
public class EngineCoreAutoConfiguration {
    /**
     * 多实例部署时通过 ark.snowflake.worker-id 区分节点，默认 1
     */
    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator(@Value("${ark.snowflake.worker-id:1}") long workerId) {
        return new SnowflakeIdGenerator(workerId);
    }

    @Bean
    public SpringDomainEventPublisher domainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new SpringDomainEventPublisher(applicationEventPublisher);
    }

    /**
     * MessageResolver Bean：业务层注入此接口获取 i18n 文案。
     * MessageSource 由 WebAutoConfiguration 初始化，通过参数注入。
     */
    @Bean
    public MessageResolver messageResolver(MessageSource messageSource) {
        return new MessageSourceResolver(messageSource);
    }
}
