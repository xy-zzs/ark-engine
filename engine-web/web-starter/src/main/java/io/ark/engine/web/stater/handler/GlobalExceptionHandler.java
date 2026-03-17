package io.ark.engine.web.stater.handler;

import io.ark.engine.web.core.exception.ArkException;
import io.ark.engine.web.core.exception.GlobalErrorCode;
import io.ark.engine.web.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

/**
 * @Description: 全局异常处理器
 * <p>处理优先级（从高到低）：
 * <ol>
 *   <li>参数校验异常 — 400，返回具体字段错误信息</li>
 *   <li>ArkException 及其子类 — 使用异常自带的 code 和 message</li>
 *   <li>Spring MVC 标准异常 — 映射到对应 HTTP 语义</li>
 *   <li>未知异常 — 500，隐藏内部细节，只记日志</li>
 * </ol>
 * @Author: Noah Zhou
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数校验失败（@Valid / @Validated 触发）
     * 将所有字段错误拼接后返回，方便前端定位
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败: {}", message);
        return Result.fail(GlobalErrorCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 请求体无法解析（JSON 格式错误、类型不匹配等）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleNotReadable(HttpMessageNotReadableException ex) {
        log.warn("请求体解析失败: {}", ex.getMessage());
        return Result.fail(GlobalErrorCode.BAD_REQUEST);
    }

    /**
     * ArkException 及其所有子类（UserException、AuthException 等）
     * 直接使用异常携带的 code 和 message，业务模块完全自主控制响应内容
     */
    @ExceptionHandler(ArkException.class)
    public Result<Void> handleArkException(ArkException ex) {
        log.error("异常[{}]: {}", ex.getCode(), ex.getMessage(), ex);
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 404：资源不存在（路径不匹配）
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNotFound(NoResourceFoundException ex) {
        log.warn("资源不存在: {}", ex.getResourcePath());
        return Result.fail(GlobalErrorCode.NOT_FOUND);
    }

    /**
     * 405：请求方法不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        log.warn("请求方法不支持: {}", ex.getMethod());
        return Result.fail(GlobalErrorCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 兜底：未预期的异常，隐藏内部细节，避免泄露堆栈给客户端
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleUnknown(Exception ex) {
        log.error("未知异常", ex);
        return Result.fail(GlobalErrorCode.INTERNAL_ERROR);
    }
}
