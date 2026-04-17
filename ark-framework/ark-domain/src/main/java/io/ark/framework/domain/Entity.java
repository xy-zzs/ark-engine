package io.ark.framework.domain;

import java.util.Objects;

/**
 * @Description: 实体基类
 *
 * <p>实体的核心语义：
 *
 * <ul>
 *   <li>有唯一标识（{@code id}），标识相同则视为同一实体，与属性值无关
 *   <li>有生命周期，状态可随时间变化
 *   <li>内置乐观锁版本号 {@code version}，配合 infrastructure 层 MP 的 {@code @Version} 使用
 * </ul>
 *
 * <p>泛型参数 {@code <ID>} 由子类指定，推荐使用值对象封装原始类型：
 *
 * <pre>{@code
 * // 推荐：使用值对象
 * public class User extends AggregateRoot<UserId> { ... }
 *
 * // 不推荐：直接使用原始类型
 * public class User extends AggregateRoot<Long> { ... }
 * }</pre>
 *
 * <p>equals/hashCode 仅基于 {@code id} 比较，符合 DDD 实体语义。
 *
 * @param <ID> 标识类型，推荐为值对象 @Author: Noah Zhou
 */
public abstract class Entity<ID> {
  /** 唯一标识 */
  private ID id;

  /**
   * 乐观锁版本号。
   *
   * <p>domain 层持有此字段用于感知版本值， 实际的乐观锁 SQL 拼接由 infrastructure 层 PO 上的 MP {@code @Version} 注解完成。 新建实体时为
   * null，从数据库加载后由 infrastructure 层赋值。
   */
  private Integer version;

  protected Entity() {}

  protected Entity(ID id) {
    this.id = id;
  }

  public ID getId() {
    return id;
  }

  /** 仅供 infrastructure 层 Repository 实现在重建实体时调用，业务代码禁止调用 */
  public void setId(ID id) {
    this.id = id;
  }

  public Integer getVersion() {
    return version;
  }

  /** 仅供 infrastructure 层在从 PO 重建实体时赋值，业务代码禁止调用 */
  protected void setVersion(Integer version) {
    this.version = version;
  }

  /** 实体相等性：仅比较 id，与属性值无关 */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Entity<?> entity = (Entity<?>) o;
    return Objects.equals(id, entity.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
