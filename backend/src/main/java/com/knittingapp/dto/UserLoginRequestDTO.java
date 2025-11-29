package com.knittingapp.dto;

/**
 * 로그인 요청 DTO
 */
public record UserLoginRequestDTO(
    String email,
    String password
) {}
