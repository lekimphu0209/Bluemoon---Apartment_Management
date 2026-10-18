package com.qlcc.exception;

import org.springframework.http.HttpStatus;

/**
 * Loi nghiep vu theo dac ta BlueMoon.
 * - field = null  -> body { "message": msg } (Toast, E-codes nghiep vu)
 * - field != null -> body { "errors": { field: msg } } (loi gan vao o nhap)
 */
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String field;

    public ApiException(HttpStatus status, String message) {
        this(status, message, null);
    }

    public ApiException(HttpStatus status, String message, String field) {
        super(message);
        this.status = status;
        this.field = field;
    }

    public static ApiException badRequest(String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, message);
    }

    public static ApiException fieldError(String field, String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, message, field);
    }

    public static ApiException conflict(String message) {
        return new ApiException(HttpStatus.CONFLICT, message);
    }

    public static ApiException notFound(String message) {
        return new ApiException(HttpStatus.NOT_FOUND, message);
    }

    public static ApiException unauthorized(String message) {
        return new ApiException(HttpStatus.UNAUTHORIZED, message);
    }

    public HttpStatus getStatus() { return status; }
    public String getField() { return field; }
}
