package io.ark.engine.core.i18n;

import io.ark.framework.i18n.MessageResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @Description: MessageSource 静态持有器，供无法注入 Bean 的场景使用
 *
 * <p>适用场景：工具类、枚举、静态方法中需要获取 i18n 文案时。
 * 能注入 Bean 的场景优先使用 {@link MessageResolver}，更易测试。
 *
 * <p>由 {@code WebAutoConfiguration} 在 MessageSource Bean 初始化后调用
 * {@link #setMessageSource} 完成注入，应用启动后即可使用。
 * @Author: Noah Zhou
 */
public class MessageSourceHolder {

    private static MessageSource messageSource;

    private MessageSourceHolder() {
    }

    /**
     * 仅由 AutoConfiguration 调用，业务代码禁止调用
     */
    public static void setMessageSource(MessageSource ms) {
        messageSource = ms;
    }

    /**
     * 使用当前请求 Locale 解析消息
     */
    public static String getMessage(String key) {
        return getMessage(key, null, LocaleContextHolder.getLocale());
    }

    /**
     * 带参数解析（支持占位符 {0} {1}）
     */
    public static String getMessage(String key, Object[] args) {
        return getMessage(key, args, LocaleContextHolder.getLocale());
    }

    /**
     * 指定 Locale 解析
     */
    public static String getMessage(String key, Object[] args, Locale locale) {
        if (messageSource == null) {
            return key;
        }
        return messageSource.getMessage(key, args, key, locale);
    }
}
