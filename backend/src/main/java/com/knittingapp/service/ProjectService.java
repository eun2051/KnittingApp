package com.knittingapp.service;

import com.knittingapp.domain.Project;
import com.knittingapp.domain.User;
import com.knittingapp.domain.Yarn;
import com.knittingapp.domain.Needle;
import com.knittingapp.domain.Pattern;
import com.knittingapp.dto.ProjectRequestDTO;
import com.knittingapp.dto.ProjectResponseDTO;
import com.knittingapp.dto.DailyLogResponseDTO;
import com.knittingapp.repository.ProjectRepository;
import com.knittingapp.repository.UserRepository;
import com.knittingapp.repository.DailyLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 프로젝트 비즈니스 로직 서비스
 */
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final DailyLogRepository dailyLogRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, DailyLogRepository dailyLogRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.dailyLogRepository = dailyLogRepository;
    }

    @Transactional
    public ProjectResponseDTO createProject(ProjectRequestDTO dto, String userEmail) {
        // 사용자 조회
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        Project project = new Project();
        project.setName(dto.name());
        project.setStatus(dto.status());
        project.setStartDate(dto.startDate());
        project.setEndDate(dto.endDate());
        project.setTargetRows(dto.targetRows());
        project.setCurrentRows(dto.currentRows());
        project.setGauge(dto.gauge());
        project.setImageUrl(dto.imageUrl());
        project.setNotes(dto.notes());
        project.setUser(user); // 사용자 설정
        
        // 실 정보 저장
        if (dto.yarnName() != null && !dto.yarnName().isBlank()) {
            Yarn yarn = new Yarn();
            yarn.setName(dto.yarnName());
            project.setYarn(yarn);
        }
        // 바늘 정보 저장
        if ((dto.needleType() != null && !dto.needleType().isBlank()) || dto.needleSize() != null) {
            Needle needle = new Needle();
            if (dto.needleType() != null) needle.setType(dto.needleType());
            if (dto.needleSize() != null) needle.setSize(dto.needleSize());
            project.setNeedle(needle);
        }
        // 도안 정보 저장
        if ((dto.patternLinkUrl() != null && !dto.patternLinkUrl().isBlank()) || (dto.patternPdfUrl() != null && !dto.patternPdfUrl().isBlank()) || (dto.patternName() != null && !dto.patternName().isBlank())) {
            Pattern pattern = new Pattern();
            if (dto.patternName() != null) pattern.setName(dto.patternName());
            if (dto.patternLinkUrl() != null) pattern.setLinkUrl(dto.patternLinkUrl());
            if (dto.patternPdfUrl() != null) pattern.setPdfUrl(dto.patternPdfUrl());
            project.setPattern(pattern);
        }
        Project saved = projectRepository.save(project);
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getAllProjectsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        return projectRepository.findByUser(user).stream()
            // Project 엔티티를 ProjectResponseDTO로 변환
            .map(project -> new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getStatus(),
                project.getStartDate(),
                project.getEndDate(),
                project.getTargetRows(),
                project.getCurrentRows(),
                project.getGauge(),
                project.getYarn() != null ? project.getYarn().getName() : null,
                project.getNeedle() != null ? project.getNeedle().getType() : null,
                project.getNeedle() != null ? project.getNeedle().getSize() : null,
                project.getPattern() != null ? project.getPattern().getName() : null,
                project.getPattern() != null ? project.getPattern().getPdfUrl() : null,
                project.getPattern() != null ? project.getPattern().getLinkUrl() : null,
                project.getImageUrl(),
                project.getNotes(),
                List.of(), // 전체 조회 시 logs는 빈 리스트
                0 // 진행률 기본값 0
            ))
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ProjectResponseDTO getProjectById(Long id, String userEmail) {
        if (id == null) {
            throw new IllegalArgumentException("프로젝트 ID는 null일 수 없습니다.");
        }
        
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다. ID: " + id));
        
        // 프로젝트 소유자 확인
        if (project.getUser() == null || !project.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("해당 프로젝트에 대한 권한이 없습니다.");
        }
        
        // 작업 일지 리스트 조회 및 최신값 반영
        return toResponseDTO(project);
    }
    
    @Transactional
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto, String userEmail) {
        if (id == null) {
            throw new IllegalArgumentException("프로젝트 ID는 null일 수 없습니다.");
        }
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다. ID: " + id));
        
        // 프로젝트 소유자 확인
        if (!project.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 프로젝트에 대한 권한이 없습니다.");
        }
        if (dto.name() != null) project.setName(dto.name());
        if (dto.status() != null) project.setStatus(dto.status());
        if (dto.startDate() != null) project.setStartDate(dto.startDate());
        if (dto.endDate() != null) project.setEndDate(dto.endDate());
        if (dto.targetRows() != null) project.setTargetRows(dto.targetRows());
        if (dto.currentRows() != null) project.setCurrentRows(dto.currentRows());
        if (dto.gauge() != null) project.setGauge(dto.gauge());
        if (dto.imageUrl() != null) project.setImageUrl(dto.imageUrl());
        if (dto.notes() != null) project.setNotes(dto.notes());
        // 실 정보 수정
        if (dto.yarnName() != null && !dto.yarnName().isBlank()) {
            if (project.getYarn() == null) project.setYarn(new Yarn());
            project.getYarn().setName(dto.yarnName());
        }
        // 바늘 정보 수정
        if ((dto.needleType() != null && !dto.needleType().isBlank()) || dto.needleSize() != null) {
            if (project.getNeedle() == null) project.setNeedle(new Needle());
            if (dto.needleType() != null) project.getNeedle().setType(dto.needleType());
            if (dto.needleSize() != null) project.getNeedle().setSize(dto.needleSize());
        }
        // 도안 정보 수정
        if ((dto.patternLinkUrl() != null && !dto.patternLinkUrl().isBlank()) || (dto.patternPdfUrl() != null && !dto.patternPdfUrl().isBlank()) || (dto.patternName() != null && !dto.patternName().isBlank())) {
            if (project.getPattern() == null) project.setPattern(new Pattern());
            if (dto.patternName() != null) project.getPattern().setName(dto.patternName());
            if (dto.patternLinkUrl() != null) project.getPattern().setLinkUrl(dto.patternLinkUrl());
            if (dto.patternPdfUrl() != null) project.getPattern().setPdfUrl(dto.patternPdfUrl());
        }
        Project saved = projectRepository.save(project);
        return toResponseDTO(saved);
    }

    /**
     * 프로젝트 진행 단수 증가 (+ 버튼용)
     */
    @Transactional
    public ProjectResponseDTO incrementRows(Long projectId, int rows) {
        if (projectId == null) {
            throw new IllegalArgumentException("프로젝트 ID는 null일 수 없습니다.");
        }
        if (rows <= 0) throw new IllegalArgumentException("증가할 단수는 1 이상이어야 합니다.");
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));
        int newRows = project.getCurrentRows() + rows;
        if (newRows > project.getTargetRows())
            throw new IllegalArgumentException("목표 단수를 초과할 수 없습니다.");
        project.setCurrentRows(newRows);
        projectRepository.save(project);
        return toResponseDTO(project);
    }

    /**
     * 프로젝트 완전 삭제 (DB에서 카드 박스 및 작업 일지 완전히 제거)
     */
    @Transactional
    public void deleteProject(Long id) {
        if (id == null) throw new IllegalArgumentException("프로젝트 ID는 null일 수 없습니다.");
        projectRepository.deleteById(id); // cascade로 DailyLog 자동 삭제
    }

    /**
     * Project Entity를 ProjectResponseDTO로 변환 (진행률, logs, currentRows 모두 최신값 포함)
     */
    private ProjectResponseDTO toResponseDTO(Project project) {
        List<DailyLogResponseDTO> logs = dailyLogRepository.findByProject(project).stream()
            .map(log -> new DailyLogResponseDTO(
                log.getId(),
                log.getProject().getId(),
                log.getDate(),
                log.getRowsWorked()
            ))
            .collect(Collectors.toList());
        int progress = (project.getTargetRows() != null && project.getTargetRows() > 0)
            ? Math.min((int)Math.round((double)project.getCurrentRows() / project.getTargetRows() * 100), 100)
            : 0;
        System.out.println("[DEBUG] toResponseDTO: currentRows=" + project.getCurrentRows() + ", progress=" + progress + ", logs.size=" + logs.size());
        return new ProjectResponseDTO(
            project.getId(),
            project.getName(),
            project.getStatus(),
            project.getStartDate(),
            project.getEndDate(),
            project.getTargetRows(),
            project.getCurrentRows(),
            project.getGauge(),
            project.getYarn() != null && project.getYarn().getName() != null && !project.getYarn().getName().isBlank() ? project.getYarn().getName() : null,
            project.getNeedle() != null && project.getNeedle().getType() != null && !project.getNeedle().getType().isBlank() ? project.getNeedle().getType() : null,
            project.getNeedle() != null && project.getNeedle().getSize() != null ? project.getNeedle().getSize() : null,
            project.getPattern() != null && project.getPattern().getName() != null && !project.getPattern().getName().isBlank() ? project.getPattern().getName() : null,
            project.getPattern() != null && project.getPattern().getPdfUrl() != null && !project.getPattern().getPdfUrl().isBlank() ? project.getPattern().getPdfUrl() : null,
            project.getPattern() != null && project.getPattern().getLinkUrl() != null && !project.getPattern().getLinkUrl().isBlank() ? project.getPattern().getLinkUrl() : null,
            project.getImageUrl(),
            project.getNotes(),
            logs,
            progress
        );
    }

    // 기타 CRUD 메서드 추가 가능
}
