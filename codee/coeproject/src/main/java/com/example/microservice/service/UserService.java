package com.example.microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import com.example.microservice.dao.UserRepository;
import com.example.microservice.entity.User;
import com.example.microservice.exception.ResourceNotFoundException;
import java.util.*;

/**
 * Service class for User operations
 * Contains business logic for managing users
 * Uses UserRepository (DAO) for database operations
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all active users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> !user.getIsDeleted())
                .toList();
    }

    /**
     * Get user by ID from database
     */
    public User getUserById(@NonNull Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    /**
     * Create a new user in database
     */
    public User createUser(String name, String email) {
        User user = new User(name, email);
        return userRepository.save(user);
    }

    /**
     * Create a new user with phone number
     */
    public User createUser(String name, String email, String phone) {
        User user = new User(name, email, phone);
        return userRepository.save(user);
    }

    /**
     * Update existing user
     */
    public User updateUser(@NonNull Long id, String name, String email, String phone) {
        User user = getUserById(id);
        if (name != null)
            user.setName(name);
        if (email != null)
            user.setEmail(email);
        if (phone != null)
            user.setPhone(phone);
        return userRepository.save(user);
    }

    /**
     * Delete user (soft delete)
     */
    public void deleteUser(@NonNull Long id) {
        User user = getUserById(id);
        user.setIsDeleted(true);
        user.setIsActive(false);
        userRepository.save(user);
    }

    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Search users by name
     */
    public List<User> searchByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Activate user
     */
    public User activateUser(@NonNull Long id) {
        User user = getUserById(id);
        user.setIsActive(true);
        user.setIsDeleted(false);
        return userRepository.save(user);
    }

    /**
     * Deactivate user
     */
    public User deactivateUser(@NonNull Long id) {
        User user = getUserById(id);
        user.setIsActive(false);
        return userRepository.save(user);
    }
}
