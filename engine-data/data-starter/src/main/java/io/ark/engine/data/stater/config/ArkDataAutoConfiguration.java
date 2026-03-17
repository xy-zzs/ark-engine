package io.ark.engine.data.stater.config;

import io.ark.engine.data.stater.properties.ArkDataProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 自动装配总入口
 *
 * <p>激活规则：
 * <ul>
 *   <li>data-mp   — classpath 存在 MP 的 BaseMapper 即激活（引入 data-starter 默认生效）</li>
 *   <li>data-jooq — classpath 存在 DSLContext 且 ark.data.jooq.enabled=true</li>
 *   <li>data-jpa  — classpath 存在 JpaRepository 且 ark.data.jpa.enabled=true</li>
 * </ul>
 *
 * <p>data-mp 的插件（分页、乐观锁等）由 MpAutoConfiguration 自身的 @AutoConfiguration 独立注册，
 * 此处不重复 Import，避免双重装配。
 * @Author: Noah Zhou
 */
@AutoConfiguration
@EnableConfigurationProperties(ArkDataProperties.class)
public class ArkDataAutoConfiguration {

    /**
     * MP 模块：classpath 有 BaseMapper 即视为已引入 data-mp，自动生效。
     * data-mp 的 MpAutoConfiguration 已通过自己的 SPI 文件注册，
     * 此处 @Configuration 仅用于挂载日志或未来扩展的 MP 级 Bean。
     */
    @Configuration
    @ConditionalOnClass(name = "com.baomidou.mybatisplus.core.mapper.BaseMapper")
    static class MpCondition {
        // data-mp 的核心 Bean（MybatisPlusInterceptor、ArkMetaObjectHandler）
        // 由 data-mp 模块自身的 MpAutoConfiguration 注册，此处无需重复声明
    }

    /**
     * jOOQ 模块：需要同时满足两个条件
     * 1. classpath 存在 DSLContext（即引入了 data-jooq 依赖）
     * 2. ark.data.jooq.enabled=true（显式开启，防止误激活）
     */
    @Configuration
    @ConditionalOnClass(name = "org.jooq.DSLContext")
    @ConditionalOnProperty(prefix = "ark.data.jooq", name = "enabled", havingValue = "true")
//    @Import(JooqPlaceholderConfiguration.class)
    static class JooqCondition {}

    /**
     * JPA 模块：同上，双重条件保护
     */
    @Configuration
    @ConditionalOnClass(name = "org.springframework.data.jpa.repository.JpaRepository")
    @ConditionalOnProperty(prefix = "ark.data.jpa", name = "enabled", havingValue = "true")
//    @Import(JpaPlaceholderConfiguration.class)
    static class JpaCondition {}
}
