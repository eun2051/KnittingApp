package com.knittingapp.dto;

/**
 * 실 응답 DTO
 */
public record YarnResponseDTO(
    Long id,
    String name,
    String brand,
    String color,
    String material
) {}
