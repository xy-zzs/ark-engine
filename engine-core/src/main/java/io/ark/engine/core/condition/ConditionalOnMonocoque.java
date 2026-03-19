package io.ark.engine.core.condition;

import io.ark.engine.core.enums.DeploymentMode;

import java.lang.annotation.*;

/**
 * @Description:
 * 仅在单体部署模式下生效的条件注解
 * 等价于 @ConditionalOnDeploymentMode(DeploymentMode.MONOCOQUE)
 * @Author: Noah Zhou
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnDeploymentMode(DeploymentMode.MONOCOQUE)
public @interface ConditionalOnMonocoque {
}
