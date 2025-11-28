package com.knittingapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.knittingapp.domain.Gauge;
import com.knittingapp.domain.ProjectStatus;
import java.time.LocalDate;
import java.util.List;

/**
 * 프로젝트 응답 DTO (POJO 버전)
 * 반드시 모든 필드 getter/setter 포함, logs 필드 명시적 직렬화
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectResponseDTO {
    private Long id;
    private String name;
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer targetRows;
    private Integer currentRows;
    private Gauge gauge;
    private String yarnName;
    private String needleType;
    private Double needleSize;
    private String patternName;
    private String patternPdfUrl;
    private String patternLinkUrl;
    private String imageUrl;
    private String notes;
    @JsonProperty("logs")
    private List<DailyLogResponseDTO> logs;
    private int progress; // 진행률 필드 추가

    // 기본 생성자
    public ProjectResponseDTO() {}

    // 전체 필드 생성자
    public ProjectResponseDTO(Long id, String name, ProjectStatus status, LocalDate startDate, LocalDate endDate,
                             Integer targetRows, Integer currentRows, Gauge gauge, String yarnName, String needleType,
                             Double needleSize, String patternName, String patternPdfUrl, String patternLinkUrl,
                             String imageUrl, String notes, List<DailyLogResponseDTO> logs, int progress) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetRows = targetRows;
        this.currentRows = currentRows;
        this.gauge = gauge;
        this.yarnName = yarnName;
        this.needleType = needleType;
        this.needleSize = needleSize;
        this.patternName = patternName;
        this.patternPdfUrl = patternPdfUrl;
        this.patternLinkUrl = patternLinkUrl;
        this.imageUrl = imageUrl;
        this.notes = notes;
        this.logs = logs;
        this.progress = progress;
    }

    // Getter/Setter 모두 추가
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Integer getTargetRows() { return targetRows; }
    public void setTargetRows(Integer targetRows) { this.targetRows = targetRows; }
    public Integer getCurrentRows() { return currentRows; }
    public void setCurrentRows(Integer currentRows) { this.currentRows = currentRows; }
    public Gauge getGauge() { return gauge; }
    public void setGauge(Gauge gauge) { this.gauge = gauge; }
    public String getYarnName() { return yarnName; }
    public void setYarnName(String yarnName) { this.yarnName = yarnName; }
    public String getNeedleType() { return needleType; }
    public void setNeedleType(String needleType) { this.needleType = needleType; }
    public Double getNeedleSize() { return needleSize; }
    public void setNeedleSize(Double needleSize) { this.needleSize = needleSize; }
    public String getPatternName() { return patternName; }
    public void setPatternName(String patternName) { this.patternName = patternName; }
    public String getPatternPdfUrl() { return patternPdfUrl; }
    public void setPatternPdfUrl(String patternPdfUrl) { this.patternPdfUrl = patternPdfUrl; }
    public String getPatternLinkUrl() { return patternLinkUrl; }
    public void setPatternLinkUrl(String patternLinkUrl) { this.patternLinkUrl = patternLinkUrl; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<DailyLogResponseDTO> getLogs() { return logs; }
    public void setLogs(List<DailyLogResponseDTO> logs) { this.logs = logs; }
    public int getProgress() { return progress; } // 진행률 getter 추가
    public void setProgress(int progress) { this.progress = progress; } // 진행률 setter 추가
}
