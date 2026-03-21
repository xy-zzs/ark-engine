package io.ark.engine.core.i18n;

import io.ark.framework.i18n.MessageResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @Description: 基于 Spring MessageSource 的 MessageResolver 实现
 *
 * <p>Locale 优先从 {@link LocaleContextHolder} 获取（由 Spring MVC 的
 * LocaleResolver 在请求进入时设置，对应 Accept-Language 请求头），
 * 非 Web 线程回退到 JVM 默认 Locale。
 *
 * <p>由 {@code EngineCoreAutoConfiguration} 注册为 Bean，业务模块直接注入
 * {@link MessageResolver} 接口即可，无需感知 Spring 实现细节
 * @Author: Noah Zhou
 */
public class MessageSourceResolver implements MessageResolver {

    private final MessageSource messageSource;

    public MessageSourceResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String resolve(String key) {
        return resolve(key, LocaleContextHolder.getLocale());
    }

    @Override
    public String resolve(String key, Object... args) {
        return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
    }

    @Override
    public String resolve(String key, Locale locale) {
        return messageSource.getMessage(key, null, key, locale);
    }
}
