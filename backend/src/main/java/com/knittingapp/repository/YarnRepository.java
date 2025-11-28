package com.knittingapp.repository;

import com.knittingapp.domain.Yarn;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 실 Repository
 */
public interface YarnRepository extends JpaRepository<Yarn, Long> {}
