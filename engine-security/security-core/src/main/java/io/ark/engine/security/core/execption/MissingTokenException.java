package io.ark.engine.security.core.execption;

/**
 * @Description: token未携带
 * @Author: Noah Zhou
 */
public class MissingTokenException extends RuntimeException {
    public MissingTokenException(String message) {
        super(message);
    }
}
