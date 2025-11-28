package com.knittingapp.repository;

import com.knittingapp.domain.Needle;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 바늘 Repository
 */
public interface NeedleRepository extends JpaRepository<Needle, Long> {}
