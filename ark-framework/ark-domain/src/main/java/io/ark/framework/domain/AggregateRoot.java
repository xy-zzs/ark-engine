package io.ark.framework.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 聚合根基类
 * <p>聚合根是聚合的唯一入口，外部只能通过聚合根操作聚合内的其他对象。
 * 相比普通 {@link Entity}，聚合根额外具备：
 * <ul>
 *   <li>领域事件收集与清除能力</li>
 *   <li>是事务一致性边界的代表</li>
 * </ul>
 *
 * <p>领域事件使用模式：
 * <pre>{@code
 * public class User extends AggregateRoot<UserId> {
 *
 *     public void register() {
 *         // ... 业务逻辑
 *         registerEvent(new UserRegisteredEvent(this.getId().value().toString()));
 *     }
 * }
 * }</pre>
 *
 * <p>infrastructure 层 Repository 实现在 {@code persist()} 成功后调用：
 * <pre>{@code
 * domainEventPublisher.publish(event);  // 逐个发布
 * aggregateRoot.clearEvents();           // 发布后清空
 * }</pre>
 *
 * @param <ID> 标识类型，推荐为值对象
 * @Author: Noah Zhou
 */
public abstract class AggregateRoot<ID> extends  Entity<ID> {

    /**
     * 待发布的领域事件列表。
     * 使用 transient 避免序列化时意外传播未发布事件。
     */
    private transient final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot() {
    }

    protected AggregateRoot(ID id) {
        super(id);
    }

    /**
     * 在领域方法中调用，将事件收集到列表，等待 infrastructure 层统一发布。
     *
     * @param event 领域事件，不可为 null
     */
    protected void registerEvent(DomainEvent event) {
        if (event != null) {
            this.domainEvents.add(event);
        }
    }

    /**
     * 获取待发布事件列表（只读视图），供 infrastructure 层 Repository 读取后发布。
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * 事件发布完成后由 infrastructure 层调用清空，防止重复发布。
     */
    public void clearEvents() {
        this.domainEvents.clear();
    }
}
