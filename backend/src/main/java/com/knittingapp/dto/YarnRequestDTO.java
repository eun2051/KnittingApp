package com.knittingapp.dto;

/**
 * 실 등록/수정 요청 DTO
 */
public record YarnRequestDTO(
    String name,
    String brand,
    String color,
    String material
) {}
