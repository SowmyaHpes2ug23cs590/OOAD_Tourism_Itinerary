package com.tourism.planner.repository;

import com.tourism.planner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Single Responsibility Principle (SRP):
 * This repository is strictly responsible for database operations and data access queries.
 * It contains no business logic or routing logic.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
