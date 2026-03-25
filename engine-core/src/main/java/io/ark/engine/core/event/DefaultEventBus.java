package io.ark.engine.core.event;

import io.ark.framework.domain.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

/**
 * @author Noah Zhou
 * @description:
 */
public class DefaultEventBus implements EventBus{

    private final ApplicationEventPublisher springPublisher;
    private final EventRouter               router;
    /** 可选注入，未引入 mq-starter 时为 empty */
    private final Optional<RemoteEventBridge> remoteBridge;

    public DefaultEventBus(ApplicationEventPublisher springPublisher,
                           EventRouter router,
                           Optional<RemoteEventBridge> remoteBridge) {
        this.springPublisher = springPublisher;
        this.router          = router;
        this.remoteBridge    = remoteBridge;
    }

    @Override
    public void publish(DomainEvent event) {
        doPublish(event);
    }

    @Override
    public void publishAfterCommit(DomainEvent event) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            doPublish(event);
                        }
                    }
            );
        } else {
            // 无事务环境直接发布
            doPublish(event);
        }
    }

    private void doPublish(DomainEvent event) {
        EventRoutingStrategy strategy = router.route(event);
        switch (strategy) {
            case LOCAL  -> publishLocal(event);
            case REMOTE -> publishRemote(event);
            case BOTH   -> { publishLocal(event); publishRemote(event); }
        }
    }

    private void publishLocal(DomainEvent event) {
        springPublisher.publishEvent(event);
    }

    private void publishRemote(DomainEvent event) {
        remoteBridge.orElseThrow(() ->
                new IllegalStateException(
                        "EventRoutingStrategy is REMOTE/BOTH but no RemoteEventBridge found. " +
                                "Please include mq-starter dependency."
                )
        ).send(event);
    }
}
