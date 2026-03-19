package io.ark.engine.core.condition;

import io.ark.engine.core.enums.DeploymentMode;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: Noah Zhou
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnDeploymentMode(DeploymentMode.DISTRIBUTED)
public @interface ConditionalOnDistributed {
}
