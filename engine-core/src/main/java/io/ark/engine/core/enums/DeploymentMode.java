package io.ark.engine.core.enums;

/**
 * @Description:
 * @Author: Noah Zhou
 */
public enum DeploymentMode {
    /** 单体部署，模块间直接 Spring Bean 调用 */
    MONOCOQUE,

    /** 分布式部署，模块间通过 RPC / HTTP / MQ 通信 */
    DISTRIBUTED
}
