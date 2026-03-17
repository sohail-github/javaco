package com.example.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import com.example.microservice.service.UserService;
import com.example.microservice.entity.User;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for User operations
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * GET /api/users - Get all users
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Retrieved all users");
        response.put("users", userService.getAllUsers());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/users/{id} - Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@NonNull @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.getUserById(id);
            response.put("status", "success");
            response.put("message", "Retrieved user with ID: " + id);
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * POST /api/users - Create new user
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, String> userRequest) {
        Map<String, Object> response = new HashMap<>();
        String name = userRequest.get("name");
        String email = userRequest.get("email");

        if (name == null || email == null || name.trim().isEmpty() || email.trim().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Name and email are required");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            User createdUser = userService.createUser(name, email);
            response.put("status", "success");
            response.put("message", "User created successfully");
            response.put("user", createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to create user: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * PUT /api/users/{id} - Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@NonNull @PathVariable Long id,
            @RequestBody Map<String, String> userRequest) {
        Map<String, Object> response = new HashMap<>();
        String name = userRequest.get("name");
        String email = userRequest.get("email");
        String phone = userRequest.get("phone");

        if (name == null || email == null || name.trim().isEmpty() || email.trim().isEmpty()) {
            response.put("status", "error");
            response.put("message", "Name and email are required");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            User updatedUser = userService.updateUser(id, name, email, phone);
            response.put("status", "success");
            response.put("message", "User updated successfully");
            response.put("user", updatedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * DELETE /api/users/{id} - Delete user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@NonNull @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.deleteUser(id);
            response.put("status", "success");
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "User service is running");
        return ResponseEntity.ok(response);
    }

}
