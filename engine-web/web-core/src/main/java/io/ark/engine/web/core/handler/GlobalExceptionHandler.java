package io.ark.engine.web.core.handler;

import io.ark.engine.core.i18n.MessageSourceHolder;
import io.ark.engine.web.core.exception.WebArkException;
import io.ark.engine.web.core.exception.WebErrorCode;
import io.ark.engine.web.core.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        String i18n = messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
        log.warn("参数校验失败: {}", i18n);
        return Result.fail(WebErrorCode.BAD_REQUEST.getCode(),i18n);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleNotReadable(HttpMessageNotReadableException ex) {
        log.warn("请求体解析失败: {}", ex.getMessage());
        String i18n = messageSource.getMessage(WebErrorCode.BAD_REQUEST.getMessageKey(), null, LocaleContextHolder.getLocale());
        return Result.fail(WebErrorCode.BAD_REQUEST.getCode(),i18n);
    }

    @ExceptionHandler(WebArkException.class)
    public Result<Void> handleArkException(WebArkException ex) {
        // 翻译：用异常携带的 key + args，结合当前请求 locale
        String message = MessageSourceHolder.getMessage(ex.getMessageKey(), ex.getArgs());
        if (ex.getCode() >= 500) {
            log.error("业务异常[{}][{}]: {}", ex.getCode(), ex.getMessageKey(), message, ex);
        } else {
            log.warn("业务异常[{}][{}]: {}", ex.getCode(), ex.getMessageKey(), message);
        }
        return Result.fail(ex.getCode(), message);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNotFound(NoResourceFoundException ex) {
        log.warn("资源不存在: {}", ex.getResourcePath());
        return Result.fail(WebErrorCode.NOT_FOUND.getCode(),
                MessageSourceHolder.getMessage(WebErrorCode.NOT_FOUND.getMessageKey()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        log.warn("请求方法不支持: {}", ex.getMethod());
        return Result.fail(WebErrorCode.METHOD_NOT_ALLOWED.getCode(),
                MessageSourceHolder.getMessage(WebErrorCode.METHOD_NOT_ALLOWED.getMessageKey()));
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleUnknown(Exception ex) {
        log.error("未知异常", ex);
        return Result.fail(WebErrorCode.INTERNAL_ERROR.getCode(),
                MessageSourceHolder.getMessage(WebErrorCode.INTERNAL_ERROR.getMessageKey()));
    }
}
