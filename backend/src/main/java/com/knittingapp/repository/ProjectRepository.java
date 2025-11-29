package com.knittingapp.repository;

import com.knittingapp.domain.Project;
import com.knittingapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 프로젝트 Repository
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // 사용자별 프로젝트 목록 조회
    List<Project> findByUser(User user);
    
    // 사용자 ID로 프로젝트 목록 조회
    List<Project> findByUserId(Long userId);
}
