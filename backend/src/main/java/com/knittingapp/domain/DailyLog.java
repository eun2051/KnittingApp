package com.knittingapp.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * 뜨개질 작업 일지 엔티티
 * 날짜별 단수 기록(UPSERT용)
 */
@Entity
public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private LocalDate date;
    private int rowsWorked;

    // 기본 생성자
    public DailyLog() {}

    // 전체 필드 생성자
    public DailyLog(Project project, LocalDate date, int rowsWorked) {
        this.project = project;
        this.date = date;
        this.rowsWorked = rowsWorked;
    }

    // Getter/Setter
    public Long getId() { return id; }
    public Project getProject() { return project; }
    public LocalDate getDate() { return date; }
    public int getRowsWorked() { return rowsWorked; }
    public void setRowsWorked(int rowsWorked) { this.rowsWorked = rowsWorked; }
    public void setProject(Project project) { this.project = project; }
    public void setDate(LocalDate date) { this.date = date; }

    // 프로젝트 ID 반환 (NPE 방지)
    public Long getProjectId() {
        return project != null ? project.getId() : null;
    }
}
