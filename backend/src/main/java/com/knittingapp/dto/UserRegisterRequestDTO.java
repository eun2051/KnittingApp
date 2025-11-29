package com.knittingapp.dto;

/**
 * 회원가입 요청 DTO
 */
public record UserRegisterRequestDTO(
    String email,
    String password
) {}
