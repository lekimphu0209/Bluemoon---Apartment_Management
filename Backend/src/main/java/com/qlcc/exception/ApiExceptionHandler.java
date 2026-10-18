package com.qlcc.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Chuan hoa loi theo dac ta:
 * - Loi nghiep vu: { "message": "..." } -> hien Toast
 * - Loi theo truong: { "errors": { "tenTruong": "..." } } -> hien duoi o nhap
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApi(ApiException ex) {
        Map<String, Object> body = new HashMap<>();
        if (ex.getField() != null) {
            body.put("errors", Map.of(ex.getField(), ex.getMessage()));
        } else {
            body.put("message", ex.getMessage());
        }
        return ResponseEntity.status(ex.getStatus()).body(body);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(401)
                .body(Map.of("message", "Tên đăng nhập hoặc mật khẩu không đúng."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }
}
