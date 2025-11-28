package com.knittingapp.repository;

import com.knittingapp.domain.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 도안 Repository
 */
public interface PatternRepository extends JpaRepository<Pattern, Long> {}
