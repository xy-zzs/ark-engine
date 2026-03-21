package io.ark.engine.web.stater.config;

import io.ark.engine.web.core.handler.GlobalExceptionHandler;
import io.ark.engine.web.core.i18n.MessageSourceHolder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * @Description:
 * @Author: Noah Zhou
 */
@AutoConfiguration
@Import(GlobalExceptionHandler.class)
public class WebAutoConfiguration {

    /**
     * MessageSource：扫描所有模块的 i18n/messages 文件
     * 启动模块放 messages.properties，各业务模块也可放自己的消息文件，
     * 通过 addBasenames 叠加，互不覆盖
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.addBasenames("i18n/messages");   // 对应 resources/i18n/messages*.properties
        source.setDefaultEncoding("UTF-8");
        source.setUseCodeAsDefaultMessage(true); // key 找不到时返回 key 本身，不抛异常
        // 注入静态持有器，供非 Bean 场景使用
        MessageSourceHolder.setMessageSource(source);
        return source;
    }

    /**
     * LocaleResolver：从 Accept-Language 请求头解析 locale
     * 前端请求时携带 Accept-Language: en 即切换为英文
     * 默认 locale 为中文
     */
    @Bean
    @ConditionalOnMissingBean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return resolver;
    }
}
