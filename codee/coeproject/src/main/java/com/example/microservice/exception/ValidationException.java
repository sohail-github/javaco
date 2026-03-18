package com.example.microservice.exception;

import java.util.List;

/**
 * Custom exception for validation errors
 */
public class ValidationException extends RuntimeException {
    private List<String> errors;

    public ValidationException(List<String> errors) {
        super("Validation failed: " + String.join(", ", errors));
        this.errors = errors;
    }

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
