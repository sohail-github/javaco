package com.example.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.microservice.service.UserService;
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
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Retrieved user with ID: " + id);
        response.put("user", userService.getUserById(id));
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/users - Create new user
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, String> userRequest) {
        Map<String, Object> response = new HashMap<>();
        String name = userRequest.get("name");
        String email = userRequest.get("email");

        if (name == null || email == null) {
            response.put("status", "error");
            response.put("message", "Name and email are required");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("status", "success");
        response.put("message", "User created successfully");
        response.put("user", userService.createUser(name, email));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/users/{id} - Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id,
            @RequestBody Map<String, String> userRequest) {
        Map<String, Object> response = new HashMap<>();
        String name = userRequest.get("name");
        String email = userRequest.get("email");

        if (name == null || email == null) {
            response.put("status", "error");
            response.put("message", "Name and email are required");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("status", "success");
        response.put("message", "User with ID " + id + " updated successfully");
        response.put("user", userService.updateUser(id, name, email));
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/users/{id} - Delete user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User with ID " + id + " deleted successfully");
        userService.deleteUser(id);
        return ResponseEntity.ok(response);
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
