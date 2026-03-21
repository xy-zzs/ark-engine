package io.ark.framework.domain;

/**
 * @Description: 值对象标记接口
 *
 * <p>值对象的核心语义：
 * <ul>
 *   <li>无唯一标识，以属性值整体表达含义</li>
 *   <li>不可变（immutable），状态变更通过返回新实例实现</li>
 *   <li>可替换性：两个属性值完全相同的值对象在业务上等价</li>
 * </ul>
 *
 * <p>实现约定：
 * <ul>
 *   <li>优先使用 {@code record} 实现，天然不可变且自动生成 equals/hashCode</li>
 *   <li>若使用普通类实现，必须手动保证不可变性并覆盖 equals/hashCode</li>
 * </ul>
 *
 * <p>示例：
 * <pre>{@code
 * public record Username(String value) implements ValueObject {
 *     public Username {
 *         if (value == null || value.isBlank()) throw new IllegalArgumentException("...");
 *     }
 * }
 * }</pre>
 * @Author: Noah Zhou
 */
public interface ValueObject {
}
