package io.ark.framework.i18n;

import java.util.Locale;

/**
 * @Description: 消息解析接口
 *
 * <p>定义在 ark-framework，使 domain 层在需要时可通过依赖注入获取国际化文案，
 * 而不必感知 Spring MessageSource 的存在。
 *
 * <p>实现由 engine-core 的 {@code MessageSourceResolver} 提供，
 * 基于 Spring {@code MessageSource} + {@code LocaleContextHolder}。
 *
 * <p>domain 层通常不直接调用此接口（异常文案交给 web 层解析），
 * 主要供 application 层或 infrastructure 层在非 Web 上下文中获取文案使用
 * @Author: Noah Zhou
 */
public interface MessageResolver {

    /**
     * 根据 key 解析消息，使用当前请求的 Locale
     *
     * @param key i18n key，对应 messages*.properties 中的键
     * @return 解析后的文案，找不到时返回 key 本身
     */
    String resolve(String key);

    /**
     * 根据 key + 参数解析消息（支持占位符 {0} {1}）
     *
     * @param key  i18n key
     * @param args 占位符参数
     * @return 解析后的文案
     */
    String resolve(String key, Object... args);

    /**
     * 指定 Locale 解析消息
     *
     * @param key    i18n key
     * @param locale 目标语言
     * @return 解析后的文案
     */
    String resolve(String key, Locale locale);
}
