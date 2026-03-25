package io.ark.engine.mq.core.consumer;

/**
 * @author Noah Zhou
 * @description: 幂等存储 SPI
 * 实现：Redis（mq-starter 提供）/ DB / 内存
 */
public interface IdempotentStore {
    /**
     * 尝试标记消息已消费
     * @return true  → 首次消费，可以继续
     *         false → 重复消息，应跳过
     */
    boolean tryMark(String messageId, long expireSeconds);
    void    remove(String messageId); // 消费失败时回滚标记
}
