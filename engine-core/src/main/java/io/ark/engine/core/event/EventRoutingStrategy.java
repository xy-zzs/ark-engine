package io.ark.engine.core.event;

/**
 * @author Noah Zhou
 * @description:
 */
public enum EventRoutingStrategy {
    /** 仅本地 Spring 事件总线 */
    LOCAL,
    /** 仅远程 MQ */
    REMOTE,
    /** 本地 + 远程同时投递 */
    BOTH
}
