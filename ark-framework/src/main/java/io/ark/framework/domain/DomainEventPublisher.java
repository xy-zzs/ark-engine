package io.ark.framework.domain;

/**
 * @Description: 领域事件发布接口（outbound port）。
 *
 * <p>定义在 ark-framework（纯 Java），由 engine-core 提供基于
 * Spring {@code ApplicationEventPublisher} 的默认实现。
 * 业务模块如需自定义（如直接发 MQ），可在 infrastructure 层覆盖实现。
 *
 * <p>调用方：仅 infrastructure 层的 Repository 实现类在持久化成功后调用，
 * 禁止在 domain 层或 application 层直接调用。
 * @Author: Noah Zhou
 */
public interface DomainEventPublisher {

    /**
     * 发布单个领域事件
     *
     * @param event 领域事件，不可为 null
     */
    void publish(DomainEvent event);
}
