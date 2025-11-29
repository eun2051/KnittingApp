package com.knittingapp.repository;

import com.knittingapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 회원(User) JPA Repository
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
