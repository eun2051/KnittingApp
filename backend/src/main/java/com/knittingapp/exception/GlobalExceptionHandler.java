package com.knittingapp.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

/**
 * 글로벌 예외 처리 및 표준 에러 응답 포맷
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // IllegalArgumentException을 먼저 처리 (구체적인 예외를 먼저 처리해야 함)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", java.time.LocalDateTime.now());
        error.put("status", 400);
        String msg = ex.getMessage();
        if (msg.contains("회원정보가 없습니다")) {
            error.put("code", "AUTH_USER_NOT_FOUND");
        } else if (msg.contains("아이디 오류")) {
            error.put("code", "AUTH_INVALID_ID");
        } else if (msg.contains("비밀번호 오류")) {
            error.put("code", "AUTH_INVALID_PASSWORD");
        } else if (msg.contains("이메일")) {
            error.put("code", "AUTH_INVALID_EMAIL");
        } else if (msg.contains("비밀번호")) {
            error.put("code", "AUTH_INVALID_PASSWORD");
        } else {
            error.put("code", "PROJECT_INVALID_ROW");
        }
        error.put("message", msg);
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

    // 일반 Exception은 마지막에 처리 (가장 포괄적인 예외)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        ex.printStackTrace(); // 스택 트레이스 출력으로 디버깅
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", java.time.LocalDateTime.now());
        error.put("status", 500);
        error.put("code", "INTERNAL_SERVER_ERROR");
        error.put("message", ex.getMessage() != null ? ex.getMessage() : "서버 내부 오류가 발생했습니다.");
        return ResponseEntity.status(500).body(error);
    }
}
