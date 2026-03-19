package io.ark.engine.core.condition;

import io.ark.engine.core.enums.DeploymentMode;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @Description:
 * @Author: Noah Zhou
 */
public class OnDeploymentModeCondition implements Condition {
    static final String PROPERTY_KEY = "ark.deployment.mode";
    static final String DEFAULT_MODE = "MONOCOQUE";
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 读取注解上声明的期望模式
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnDeploymentMode.class.getName());
        if (attributes == null) {
            return false;
        }

        DeploymentMode expected = (DeploymentMode) attributes.get("value");

        // 从 Environment 读取当前配置的部署模式，默认单体
        String configured = context.getEnvironment()
                .getProperty(PROPERTY_KEY, DEFAULT_MODE);

        try {
            DeploymentMode actual = DeploymentMode.valueOf(configured.toUpperCase());
            return expected == actual;
        } catch (IllegalArgumentException e) {
            // 配置值非法，降级为单体模式
            return expected == DeploymentMode.MONOCOQUE;
        }
    }
}
