package com.example.microservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.microservice.entity.User;
import java.util.Optional;
import java.util.List;

/**
 * UserRepository (DAO) interface for User entity
 * Provides database operations for User entity using Spring Data JPA
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find users by name (case-insensitive)
     */
    List<User> findByNameContainingIgnoreCase(String name);
}
