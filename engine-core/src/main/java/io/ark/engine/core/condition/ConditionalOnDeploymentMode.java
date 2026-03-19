package io.ark.engine.core.condition;

import io.ark.engine.core.enums.DeploymentMode;

/**
 * @Description: 根据部署模式进行条件装配
 * <p>使用示例：
 * <pre>
 * {@code
 * @Bean
 * @ConditionalOnDeploymentMode(DeploymentMode.MONOCOQUE)
 * public UserCredentialGateway localGateway(...) { ... }
 *
 * @Bean
 * @ConditionalOnDeploymentMode(DeploymentMode.DISTRIBUTED)
 * public UserCredentialGateway dubboGateway(...) { ... }
 * }
 * </pre>
 *
 * <p>对应配置项：
 * <pre>
 * ark:
 *   deployment:
 *     mode: MONOCOQUE   # 或 DISTRIBUTED
 * </pre>
 * @Author: Noah Zhou
 */
public @interface ConditionalOnDeploymentMode {
    DeploymentMode value();
}
