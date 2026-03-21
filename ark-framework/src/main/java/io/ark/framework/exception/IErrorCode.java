package io.ark.framework.exception;

/**
 * @Description: 错误码接口，所有业务模块的错误码枚举必须实现此接口。
 *
 * <p>设计约定：
 * <ul>
 *   <li>{@code code} 建议按模块划分段位，如 user 模块用 1000~1999，auth 模块用 2000~2999</li>
 *   <li>{@code messageKey} 对应各模块 i18n 资源文件中的 key，由 engine-web 层解析为实际文案</li>
 * </ul>
 *
 * <p>示例：
 * <pre>{@code
 * public enum UserErrorCode implements IErrorCode {
 *
 *     USER_NOT_FOUND(1001, "user.error.notFound"),
 *     USERNAME_DUPLICATE(1002, "user.error.usernameDuplicate");
 *
 *     private final int code;
 *     private final String messageKey;
 *
 *     UserErrorCode(int code, String messageKey) {
 *         this.code = code;
 *         this.messageKey = messageKey;
 *     }
 *
 *     @Override public int getCode() { return code; }
 *     @Override public String getMessageKey() { return messageKey; }
 * }
 * }</pre>
 * @Author: Noah Zhou
 */
public interface IErrorCode {

    /**
     * 错误码，全局唯一
     */
    int getCode();

    /**
     * i18n 消息 key，对应 messages*.properties 中的键
     */
    String getMessageKey();
}
