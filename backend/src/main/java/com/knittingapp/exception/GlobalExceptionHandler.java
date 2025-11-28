package com.knittingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

/**
 * 글로벌 예외 처리 및 표준 에러 응답 포맷
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleException(Exception ex) {
        return Map.of(
            "timestamp", LocalDateTime.now(),
            "status", HttpStatus.BAD_REQUEST.value(),
            "code", "PROJECT_ERROR",
            "message", ex.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", java.time.LocalDateTime.now());
        error.put("status", 400);
        error.put("code", "PROJECT_INVALID_ROW");
        error.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", java.time.LocalDateTime.now());
        error.put("status", ex.getStatusCode().value());
        error.put("code", "PROJECT_RESOURCE_NOT_FOUND");
        error.put("message", ex.getReason() != null ? ex.getReason() : "리소스를 찾을 수 없습니다.");
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }
}
