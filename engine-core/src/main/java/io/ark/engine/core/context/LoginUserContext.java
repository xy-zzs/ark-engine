package io.ark.engine.core.context;

/**
 * @Description: 登录用户上下文，基于 ThreadLocal 实现请求级别的用户信息传递
 * <p>生命周期：
 * <ol>
 *   <li>engine-security 的 Token 过滤器在请求进入时调用 {@link #set}</li>
 *   <li>业务代码在任意位置调用 {@link #get} 获取当前用户</li>
 *   <li>过滤器在请求结束时（finally 块）调用 {@link #clear} 防止内存泄漏</li>
 * </ol>
 *
 * <p>注意：异步线程无法直接继承父线程的 ThreadLocal，
 * 如需跨线程传递，使用 Spring 的 {@code TaskDecorator} 或手动传参。
 * @Author: Noah Zhou
 */
public class LoginUserContext {

    private static final ThreadLocal<LoginUser> CONTEXT = new ThreadLocal<>();

    private LoginUserContext() {
        // 工具类，禁止实例化
    }

    public static void set(LoginUser loginUser) {
        CONTEXT.set(loginUser);
    }

    /**
     * 获取当前登录用户，未登录时返回 null。
     * 需要强制登录的场景应在 application 层主动判空并抛出异常。
     */
    public static LoginUser get() {
        return CONTEXT.get();
    }

    /**
     * 必须在请求结束后调用，防止线程池复用场景下的内存泄漏
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
