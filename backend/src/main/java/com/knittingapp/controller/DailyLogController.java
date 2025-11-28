package com.knittingapp.controller;

import com.knittingapp.dto.DailyLogRequestDTO;
import com.knittingapp.dto.DailyLogResponseDTO;
import com.knittingapp.service.DailyLogService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 작업 일지(DailyLog) API 컨트롤러
 * 모든 응답/설명은 한국어로 작성
 */
@RestController
@RequestMapping("/api/projects/{projectId}/logs")
public class DailyLogController {
    private final DailyLogService dailyLogService;

    public DailyLogController(DailyLogService dailyLogService) {
        this.dailyLogService = dailyLogService;
    }

    /**
     * 작업 일지 등록/수정(UPSERT)
     */
    @PostMapping
    public DailyLogResponseDTO upsertDailyLog(@PathVariable Long projectId, @RequestBody DailyLogRequestDTO dto) {
        return dailyLogService.upsertDailyLog(projectId, dto);
    }

    /**
     * 프로젝트별 작업 일지 전체 조회
     */
    @GetMapping
    public List<DailyLogResponseDTO> getDailyLogs(@PathVariable Long projectId) {
        return dailyLogService.getDailyLogs(projectId);
    }
}
