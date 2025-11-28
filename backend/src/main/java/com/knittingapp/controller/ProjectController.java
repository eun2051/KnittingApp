package com.knittingapp.controller;

import com.knittingapp.dto.ProjectRequestDTO;
import com.knittingapp.dto.ProjectResponseDTO;
import com.knittingapp.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

/**
 * 프로젝트 API Controller
 * Entity 직접 반환 금지, DTO만 반환
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ProjectResponseDTO createProject(@RequestBody ProjectRequestDTO dto) {
        return projectService.createProject(dto);
    }

    @GetMapping
    public List<ProjectResponseDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectResponseDTO getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    public ProjectResponseDTO updateProject(@PathVariable Long id, @RequestBody ProjectRequestDTO dto) {
        return projectService.updateProject(id, dto);
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

    // 기타 CRUD API 추가 가능
}
