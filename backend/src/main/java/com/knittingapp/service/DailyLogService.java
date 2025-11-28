package com.knittingapp.service;

import com.knittingapp.domain.DailyLog;
import com.knittingapp.domain.Project;
import com.knittingapp.dto.DailyLogRequestDTO;
import com.knittingapp.dto.DailyLogResponseDTO;
import com.knittingapp.repository.DailyLogRepository;
import com.knittingapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 작업 일지(DailyLog) 서비스
 * UPSERT, 단수 차감 유효성 검사, DTO 변환
 */
@Service
public class DailyLogService {
    private final DailyLogRepository dailyLogRepository;
    private final ProjectRepository projectRepository;

    public DailyLogService(DailyLogRepository dailyLogRepository, ProjectRepository projectRepository) {
        this.dailyLogRepository = dailyLogRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * 프로젝트별 작업 일지 전체 조회
     */
    public List<DailyLogResponseDTO> getDailyLogs(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("projectId는 null일 수 없습니다.");
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));
        List<DailyLog> logs = dailyLogRepository.findByProject_Id(project.getId());
        System.out.println("[DEBUG] getDailyLogs: projectId=" + projectId + ", logs.size=" + logs.size());
        /**
         * [디버그] getDailyLogs 호출 시 DB에서 실제로 조회되는 값 로그 추가
         */
        for (DailyLog log : logs) {
            System.out.println("[DEBUG] DailyLog: date=" + log.getDate() + ", rowsWorked=" + log.getRowsWorked());
        }
        return logs.stream()
                .map(log -> new DailyLogResponseDTO(
                    log.getId(),
                    log.getProjectId(),
                    log.getDate(),
                    log.getRowsWorked()
                ))
                .collect(Collectors.toList());
    }

    /**
     * + 버튼 클릭 시: 프로젝트 currentRows 1 증가 & 오늘의 기록 rows 누적 (logs 배열과 DB 동기화)
     * 반드시 오늘 날짜의 DailyLog가 존재하면 +1 누적, 없으면 1로 생성
     * 누적 후 logs 배열에 반영되도록 saveAndFlush
     */
    @Transactional
    public DailyLogResponseDTO incrementTodayRow(Long projectId) {
        LocalDate today = LocalDate.now();
        DailyLog log = dailyLogRepository.findByProject_IdAndDate(projectId, today).orElse(null);
        int newRows = (log != null ? log.getRowsWorked() : 0) + 1;
        // 0 미만 불가(푸르시오 유효성)
        if (newRows < 0) newRows = 0;
        DailyLog upserted = upsertDailyLog(projectId, today, newRows);
        return toResponseDTO(upserted);
    }

    /**
     * 작업 일지 UPSERT (날짜별 1개 레코드)
     * 같은 날짜에 여러 번 작업해도 마지막에 입력한 값으로 덮어쓰기(최종 결과)
     * saveAndFlush로 DB 즉시 반영
     */
    public DailyLog upsertDailyLog(Long projectId, LocalDate date, int rowsWorked) {
        DailyLog log = dailyLogRepository.findByProject_IdAndDate(projectId, date).orElse(null);
        if (log == null) {
            // DailyLog 생성 시 projectId 대신 Project 객체를 넘김
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));
            log = new DailyLog(project, date, rowsWorked);
        } else {
            // 기존 값 무시, 마지막 입력값으로 덮어쓰기
            log.setRowsWorked(rowsWorked);
        }
        DailyLog saved = dailyLogRepository.saveAndFlush(log);
        return saved;
    }

    /**
     * 푸르시오(단수 차감) - 0 미만 불가
     */
    public DailyLogResponseDTO decrementTodayRow(Long projectId) {
        LocalDate today = LocalDate.now();
        DailyLog log = dailyLogRepository.findByProject_IdAndDate(projectId, today).orElse(null);
        int newRows = (log != null ? log.getRowsWorked() : 0) - 1;
        if (newRows < 0) newRows = 0;
        DailyLog upserted = upsertDailyLog(projectId, today, newRows);
        return toResponseDTO(upserted);
    }

    /**
     * [핵심] 오늘의 기록 UPSERT: 같은 날짜에 여러 번 저장해도 누적, 0 이상만 저장, DB에 즉시 반영
     */
    @Transactional
    public DailyLogResponseDTO upsertDailyLog(Long projectId, DailyLogRequestDTO dto) {
        System.out.println("[DEBUG] upsertDailyLog 호출: projectId=" + projectId + ", date=" + dto.date() + ", rowsWorked=" + dto.rowsWorked());
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));
        // 날짜가 null이거나 잘못된 값이면 오늘 날짜로 강제 대입
        LocalDate logDate = (dto.date() != null) ? dto.date() : LocalDate.now();
        if (logDate == null) logDate = LocalDate.now();
        System.out.println("[DEBUG] UPSERT 직전: projectId=" + projectId + ", logDate=" + logDate + ", rowsWorked=" + dto.rowsWorked());
        DailyLog log = dailyLogRepository.findByProject_IdAndDate(project.getId(), logDate)
                .orElse(null);
        System.out.println("[DEBUG] findByProjectIdAndDate 결과: " + (log != null ? ("id=" + log.getId() + ", date=" + log.getDate() + ", rowsWorked=" + log.getRowsWorked()) : "null"));
        // 음수도 허용 (실수 입력 취소/푸르시오 용도)
        if (dto.rowsWorked() == null) {
            throw new IllegalArgumentException("작업 단수는 필수입니다.");
        }
        if (log == null) {
            log = new DailyLog(project, (logDate != null ? logDate : LocalDate.now()), dto.rowsWorked());
            dailyLogRepository.saveAndFlush(log);
            System.out.println("[DEBUG] 새 DailyLog 생성 및 저장: rowsWorked=" + log.getRowsWorked() + ", projectId=" + log.getProjectId() + ", date=" + log.getDate());
        } else {
            int newRows = log.getRowsWorked();
            log.setRowsWorked(newRows + dto.rowsWorked());
            dailyLogRepository.saveAndFlush(log);
            System.out.println("[DEBUG] 기존 DailyLog 수정 및 저장(누적): rowsWorked=" + log.getRowsWorked() + ", projectId=" + log.getProjectId() + ", date=" + log.getDate());
        }
        // 프로젝트 currentRows를 전체 DailyLog 누적 합산값으로 갱신 (날짜가 바뀌어도 리셋되지 않음)
        List<DailyLog> allLogs = dailyLogRepository.findByProject_Id(project.getId());
        int totalRows = allLogs.stream().mapToInt(DailyLog::getRowsWorked).sum();
        project.setCurrentRows(totalRows);
        projectRepository.saveAndFlush(project);
        System.out.println("[DEBUG] Project currentRows DB 반영: currentRows=" + project.getCurrentRows());
        return new DailyLogResponseDTO(
            log.getId(),
            log.getProjectId(),
            log.getDate(),
            log.getRowsWorked()
        );
    }

    /**
     * 작업 일지 UPSERT (날짜별 합산, 음수 입력 허용, 최종 단수 0 미만 불가)
     */
    @Transactional
    public DailyLogResponseDTO upsertDailyLog(DailyLogRequestDTO dto) {
        Project project = projectRepository.findById(dto.projectId())
            .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));
        LocalDate logDate = (dto.date() != null) ? dto.date() : LocalDate.now();
        DailyLog log = dailyLogRepository.findByProjectAndDate(project, logDate).orElse(null);
        int newRows = (log != null ? log.getRowsWorked() : 0) + dto.rowsWorked();
        if (newRows < 0) newRows = 0; // 단수 0 이상 보장
        if (log == null) {
            log = new DailyLog(project, logDate, newRows);
        } else {
            log.setRowsWorked(newRows);
        }
        dailyLogRepository.saveAndFlush(log);
        project.setCurrentRows(newRows < 0 ? 0 : newRows); // currentRows도 0 이상 보장
        projectRepository.saveAndFlush(project);
        return new DailyLogResponseDTO(
            log.getId(),
            log.getProject().getId(),
            log.getDate(),
            log.getRowsWorked()
        );
    }

    /**
     * DailyLog 엔티티를 DailyLogResponseDTO로 변환 (rowsWorked만 사용)
     */
    public DailyLogResponseDTO toResponseDTO(DailyLog log) {
        if (log == null) return null;
        return new DailyLogResponseDTO(
            log.getId(),
            log.getProjectId(),
            log.getDate(),
            log.getRowsWorked()
        );
    }

    /**
     * 프로젝트 진행률 계산 (0~100%)
     * 현재까지 작업한 단수(currentRows) / 목표 단수(targetRows) * 100
     * 단, 목표 단수가 0이면 0% 반환
     */
    public int calculateProgress(int currentRows, int targetRows) {
        if (targetRows <= 0) return 0;
        double percent = ((double) currentRows / targetRows) * 100.0;
        return (int) Math.round(percent);
    }
}
