package com.knittingapp.dto;

import java.time.LocalDate;

/**
 * 작업 일지 등록/수정 요청 DTO
 */
public record DailyLogRequestDTO(
    Long projectId,
    LocalDate date,
    Integer rowsWorked,
    Integer workTimeMinutes,
    String notes
) {}
