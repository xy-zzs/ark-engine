package io.ark.engine.framework.web.domain;

/**
 * @Description: 错误码规范（各模块自己实现）
 * @Author: Noah Zhou
 */
public interface ErrorCode {
    String getCode();
    String getMessage();
}
