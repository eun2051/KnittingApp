package com.knittingapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * 작업 일지 응답 DTO (record 타입, 직렬화 어노테이션 포함)
 */
public record DailyLogResponseDTO(
    Long id,
    Long projectId,
    LocalDate date,
    @JsonProperty("rowsWorked") int rowsWorked
) {}
