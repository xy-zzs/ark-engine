package io.ark.engine.mq.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Noah Zhou
 * @description:
 */
@ConfigurationProperties(prefix = "ark.mq.kafka")
public class KafkaMqProperties {
    /** 是否自动创建 topic，生产环境建议关闭 */
    private boolean autoCreateTopics = true;

    /** 自动创建时的默认分区数 */
    private int defaultPartitions = 3;

    /** 自动创建时的默认副本数 */
    private short defaultReplicationFactor = 1;

    /** 消费者组 ID */
    private String groupId = "ark-default-group";

    /** 发送失败告警：连续失败超过此次数打印 ERROR 日志 */
    private int alertThreshold = 3;

    /** 需要自动创建的 topic 列表（配置驱动），为空则根据 MessageConsumer 自动收集 */
    private List<TopicConfig> topics = new ArrayList<>();

    public static class TopicConfig {
        private String name;
        private int    partitions      = -1; // -1 表示使用 defaultPartitions
        private short  replicationFactor = -1;

        // getters & setters
        public String getName()                    { return name; }
        public void   setName(String name)         { this.name = name; }
        public int    getPartitions()              { return partitions; }
        public void   setPartitions(int p)         { this.partitions = p; }
        public short  getReplicationFactor()       { return replicationFactor; }
        public void   setReplicationFactor(short r){ this.replicationFactor = r; }
    }

    // getters & setters
    public boolean        isAutoCreateTopics()                           { return autoCreateTopics; }
    public void           setAutoCreateTopics(boolean b)                 { this.autoCreateTopics = b; }
    public int            getDefaultPartitions()                         { return defaultPartitions; }
    public void           setDefaultPartitions(int p)                    { this.defaultPartitions = p; }
    public short          getDefaultReplicationFactor()                  { return defaultReplicationFactor; }
    public void           setDefaultReplicationFactor(short r)           { this.defaultReplicationFactor = r; }
    public String         getGroupId()                                   { return groupId; }
    public void           setGroupId(String g)                           { this.groupId = g; }
    public int            getAlertThreshold()                            { return alertThreshold; }
    public void           setAlertThreshold(int t)                       { this.alertThreshold = t; }
    public List<TopicConfig> getTopics()                                 { return topics; }
    public void           setTopics(List<TopicConfig> topics)            { this.topics = topics; }
}
