package io.ark.engine.core.id;

/**
 * @Description: 雪花 ID 生成器
 *
 * <p>生成 64 位 Long 类型的全局唯一 ID，结构如下：
 * <pre>
 * | 1位符号位 | 41位时间戳（ms） | 10位机器ID | 12位序列号 |
 * </pre>
 *
 * <p>理论 QPS 上限：单机 4096 * 1000 = 4,096,000/s，满足绝大多数场景。
 *
 * <p>注册方式：由 engine-core 的 AutoConfiguration 以 Bean 形式注册，
 * workerId 通过配置项 {@code ark.snowflake.worker-id} 注入（默认 1）。
 * 多实例部署时务必配置不同的 workerId，否则可能产生重复 ID。
 * @Author: Noah Zhou
 */
public class SnowflakeIdGenerator {

    // ---- 各部分位数 ----
    private static final long WORKER_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;

    // ---- 最大值 ----
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);   // 1023
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);      // 4095

    // ---- 左移位数 ----
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;             // 12
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS; // 22

    /** 起始纪元（2024-01-01 00:00:00 UTC），减小生成值，延长可用年限 */
    private static final long EPOCH = 1704067200000L;

    private final long workerId;
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public SnowflakeIdGenerator(long workerId) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException(
                    "workerId must be between 0 and " + MAX_WORKER_ID);
        }
        this.workerId = workerId;
    }

    /**
     * 生成下一个唯一 ID（线程安全）
     *
     * @return 雪花 ID
     */
    public synchronized long nextId() {
        long currentMs = System.currentTimeMillis();

        if (currentMs < lastTimestamp) {
            throw new IllegalStateException(
                    "Clock moved backwards. Refusing to generate id for %d ms"
                            .formatted(lastTimestamp - currentMs));
        }

        if (currentMs == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // 当前毫秒序列号已满，等到下一毫秒
                currentMs = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentMs;

        return ((currentMs - EPOCH) << TIMESTAMP_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    private long waitNextMillis(long lastTimestamp) {
        long ms = System.currentTimeMillis();
        while (ms <= lastTimestamp) {
            ms = System.currentTimeMillis();
        }
        return ms;
    }
}
