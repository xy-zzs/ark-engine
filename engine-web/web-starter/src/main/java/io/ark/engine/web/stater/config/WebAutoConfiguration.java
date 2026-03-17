package io.ark.engine.web.stater.config;

import io.ark.engine.web.stater.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @Description:
 * @Author: Noah Zhou
 */
@AutoConfiguration
@Import(GlobalExceptionHandler.class)
public class WebAutoConfiguration {
}
