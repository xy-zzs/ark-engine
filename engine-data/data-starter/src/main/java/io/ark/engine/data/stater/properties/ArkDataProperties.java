package io.ark.engine.data.stater.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @Description: ark.data.* 配置属性
 * @Author: Noah Zhou
 */
@Data
@ConfigurationProperties(prefix = "ark.data")
public class ArkDataProperties {
    @NestedConfigurationProperty
    private OrmProperties jooq = new OrmProperties();

    @NestedConfigurationProperty
    private OrmProperties jpa = new OrmProperties();

    @Data
    public static class OrmProperties {
        /**
         * 是否启用该 ORM 模块。
         */
        private boolean enabled = false;
    }
}
