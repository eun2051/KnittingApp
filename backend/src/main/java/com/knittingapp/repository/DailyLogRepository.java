package com.knittingapp.repository;

import com.knittingapp.domain.DailyLog;
import com.knittingapp.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 작업 일지(DailyLog) Repository
 * 날짜+프로젝트 기준 UPSERT 및 조회 지원
 */
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {
    Optional<DailyLog> findByProjectAndDate(Project project, LocalDate date);
    List<DailyLog> findByProject(Project project);
    List<DailyLog> findByProject_Id(Long projectId);

    /**
     * 프로젝트ID와 날짜로 작업 일지 1개 조회 (UPSERT용)
     */
    Optional<DailyLog> findByProject_IdAndDate(Long projectId, LocalDate date);
}
