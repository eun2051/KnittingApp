package com.knittingapp.repository;

import com.knittingapp.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 프로젝트 Repository
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // 추가 쿼리 메서드는 필요 시 작성
}
