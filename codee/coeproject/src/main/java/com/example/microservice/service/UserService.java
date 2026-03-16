package com.example.microservice.service;

import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service class for User operations
 * Contains business logic for managing users
 */
@Service
public class UserService {

    // Mock database
    private static final List<Map<String, Object>> users = new ArrayList<>(Arrays.asList(
            Map.of("id", 1L, "name", "John Doe", "email", "john@example.com"),
            Map.of("id", 2L, "name", "Jane Smith", "email", "jane@example.com")));

    /**
     * Get all users
     */
    public List<Map<String, Object>> getAllUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Get user by ID
     */
    public Map<String, Object> getUserById(Long id) {
        return users.stream()
                .filter(user -> user.get("id").equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Create a new user
     */
    public Map<String, Object> createUser(String name, String email) {
        Map<String, Object> user = new HashMap<>();
        long newId = users.stream()
                .mapToLong(u -> (Long) u.get("id"))
                .max()
                .orElse(0) + 1;

        user.put("id", newId);
        user.put("name", name);
        user.put("email", email);
        users.add(user);
        return user;
    }

    /**
     * Update user
     */
    public Map<String, Object> updateUser(Long id, String name, String email) {
        Map<String, Object> userToUpdate = getUserById(id);
        if (userToUpdate != null) {
            userToUpdate.put("name", name);
            userToUpdate.put("email", email);
        }
        return userToUpdate;
    }

    /**
     * Delete user
     */
    public boolean deleteUser(Long id) {
        return users.removeIf(user -> user.get("id").equals(id));
    }

}
