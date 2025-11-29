package com.knittingapp.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * 뜨개질 프로젝트 Entity
 * 프로젝트별로 관리, 상태(PLANNING, WIP, FINISHED, SUSPENDED) 포함
 */
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq")
    @SequenceGenerator(name = "project_seq", sequenceName = "project_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(name = "target_rows")
    private Integer targetRows; // 목표 단수
    
    @Column(name = "current_rows") 
    private Integer currentRows; // 현재 단수

    @Embedded
    private Gauge gauge;

    @ManyToOne(cascade = CascadeType.ALL)
    private Yarn yarn;

    @ManyToOne(cascade = CascadeType.ALL)
    private Needle needle;

    @ManyToOne(cascade = CascadeType.ALL)
    private Pattern pattern;
    
    @Column(name = "project_image_url")
    private String imageUrl; // 프로젝트 진행 중 사진 URL
    
    @Column(length = 2000)
    private String notes; // 프로젝트 전체 메모/일지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 프로젝트 소유자

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<DailyLog> dailyLogs = new java.util.ArrayList<>();

    // Getter 메서드 추가
    public Long getId() { return id; }
    public String getName() { return name; }
    public ProjectStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Integer getTargetRows() { return targetRows; }
    public Integer getCurrentRows() { return currentRows; }
    public Gauge getGauge() { return gauge; }
    public Yarn getYarn() { return yarn; }
    public Needle getNeedle() { return needle; }
    public Pattern getPattern() { return pattern; }
    public String getImageUrl() { return imageUrl; }
    public String getNotes() { return notes; }
    public User getUser() { return user; }
    public java.util.List<DailyLog> getDailyLogs() { return dailyLogs; }

    // Setter 메서드 추가
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setTargetRows(Integer targetRows) { this.targetRows = targetRows; }
    public void setCurrentRows(Integer currentRows) { this.currentRows = currentRows; }
    public void setGauge(Gauge gauge) { this.gauge = gauge; }
    public void setYarn(Yarn yarn) { this.yarn = yarn; }
    public void setNeedle(Needle needle) { this.needle = needle; }
    public void setPattern(Pattern pattern) { this.pattern = pattern; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setUser(User user) { this.user = user; }
    public void setDailyLogs(java.util.List<DailyLog> dailyLogs) { this.dailyLogs = dailyLogs; }
}

// ...실(Yarn), 바늘(Needle), 도안(Pattern) Entity는 별도 파일로 생성 예정
