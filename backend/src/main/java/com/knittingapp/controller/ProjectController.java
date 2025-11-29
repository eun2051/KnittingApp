package com.knittingapp.controller;

import com.knittingapp.dto.ProjectRequestDTO;
import com.knittingapp.dto.ProjectResponseDTO;
import com.knittingapp.service.ProjectService;
import com.knittingapp.util.JwtUtil;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import java.io.File;
import com.knittingapp.domain.Project;
import com.knittingapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;

/**
 * 프로젝트 API Controller
 * Entity 직접 반환 금지, DTO만 반환
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // JWT 토큰에서 사용자 이메일 추출
    private String getUserEmailFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("인증 토큰이 필요합니다.");
        }
        String token = authHeader.substring(7);
        return JwtUtil.getEmail(token);
    }

    @PostMapping
    public ProjectResponseDTO createProject(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody ProjectRequestDTO dto
    ) {
        String userEmail = getUserEmailFromToken(authHeader);
        return projectService.createProject(dto, userEmail);
    }

    @GetMapping
    public List<ProjectResponseDTO> getAllProjects(
        @RequestHeader("Authorization") String authHeader
    ) {
        String userEmail = getUserEmailFromToken(authHeader);
        return projectService.getAllProjectsByUser(userEmail);
    }

    @GetMapping("/{id}")
    public ProjectResponseDTO getProjectById(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id
    ) {
        String userEmail = getUserEmailFromToken(authHeader);
        return projectService.getProjectById(id, userEmail);
    }

    @PutMapping("/{id}")
    public ProjectResponseDTO updateProject(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id,
        @RequestBody ProjectRequestDTO dto
    ) {
        String userEmail = getUserEmailFromToken(authHeader);
        return projectService.updateProject(id, dto, userEmail);
    }

    /**
     * 프로젝트 진행 단수 증가 (+ 버튼)
     */
    @PutMapping("/{id}/rows")
    public ResponseEntity<ProjectResponseDTO> incrementRows(
        @PathVariable Long id,
        @RequestBody Map<String, Integer> body
    ) {
        int rows = body.getOrDefault("rows", 1);
        ProjectResponseDTO dto = projectService.incrementRows(id, rows);
        return ResponseEntity.ok(dto);
    }

    /**
     * 프로젝트 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 프로젝트 이미지 반환 (없으면 기본 이미지 반환)
     */
    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getProjectImage(@PathVariable Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        String imageUrl = (project != null) ? project.getImageUrl() : null;
        Resource imageResource;
        if (imageUrl == null || imageUrl.isBlank()) {
            // 기본 이미지 반환
            imageResource = new ClassPathResource("static/cloud-pattern.svg");
        } else {
            File imageFile = new File(imageUrl);
            if (imageFile.exists()) {
                imageResource = new FileSystemResource(imageFile);
            } else {
                imageResource = new ClassPathResource("static/cloud-pattern.svg");
            }
        }
        return ResponseEntity.ok().body(imageResource);
    }

    // 기타 CRUD API 추가 가능
}
