package io.ark.engine.web.core.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @Description: MessageSource 静态持有器
 *
 * <p>由 WebAutoConfiguration 在启动时注入，
 * 供不方便注入 Bean 的场景（静态工具类等）使用。
 *
 * <p>正常 Bean 场景直接注入 MessageSource 即可，不需要用此类。
 * @Author: Noah Zhou
 */
public class MessageSourceHolder {
    private static MessageSource messageSource;

    private MessageSourceHolder() {}

    public static void setMessageSource(MessageSource ms) {
        MessageSourceHolder.messageSource = ms;
    }

    /**
     * 根据当前请求的 locale 翻译消息键
     *
     * @param key  消息键，如 "user.not.found"
     * @param args 占位符参数，对应消息里的 {0} {1}
     * @return 翻译后的文本，找不到 key 时直接返回 key 本身（降级处理）
     */
    public static String getMessage(String key, Object... args) {
        if (messageSource == null) return key;
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            // 找不到 key 时不抛异常，返回 key 本身，避免翻译缺失导致接口报错
            return key;
        }
    }
}
