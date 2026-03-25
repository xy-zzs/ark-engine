package io.ark.engine.mq.core.consumer;

/**
 * @author Noah Zhou
 * @description: 重试策略 SPI
 */
public interface RetryPolicy {
    /** 是否应该重试 */
    boolean shouldRetry(int retryCount, Throwable cause);
    /** 下次重试延迟（毫秒），支持退避策略 */
    long nextDelayMs(int retryCount);

    static RetryPolicy fixed(int maxRetries, long delayMs) {
        return new RetryPolicy() {
            @Override
            public boolean shouldRetry(int count, Throwable cause) { return count < maxRetries; }
            @Override
            public long nextDelayMs(int count) { return delayMs; }
        };
    }

    static RetryPolicy exponential(int maxRetries, long baseDelayMs) {
        return new RetryPolicy() {
            @Override
            public boolean shouldRetry(int count, Throwable cause) { return count < maxRetries; }
            @Override
            public long nextDelayMs(int count) { return baseDelayMs * (1L << count); }
        };
    }
}
