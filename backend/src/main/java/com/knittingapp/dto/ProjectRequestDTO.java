package com.knittingapp.dto;

import com.knittingapp.domain.Gauge;
import com.knittingapp.domain.ProjectStatus;
import java.time.LocalDate;

/**
 * 프로젝트 생성/수정 요청 DTO
 */
public record ProjectRequestDTO(
    String name,
    ProjectStatus status,
    LocalDate startDate,
    LocalDate endDate,
    Integer targetRows,
    Integer currentRows,
    Gauge gauge,
    String yarnName,
    String needleType,
    Double needleSize,
    String patternLinkUrl,
    String patternPdfUrl,
    String imageUrl,
    String notes,
    String patternName
) {}
