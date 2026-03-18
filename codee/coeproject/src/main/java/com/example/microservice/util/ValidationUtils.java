package com.example.microservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for comprehensive input validation
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{10,15}$");

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;

    /**
     * Validate user creation/update request
     */
    public static List<String> validateUserRequest(String name, String email, String phone) {
        List<String> errors = new ArrayList<>();

        if (name == null || name.trim().isEmpty()) {
            errors.add("Name is required");
        } else if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            errors.add("Name must be between " + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is required");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Email format is invalid");
        }

        if (phone != null && !phone.trim().isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            errors.add("Phone number format is invalid (10-15 digits)");
        }

        return errors;
    }

    /**
     * Validate product request
     */
    public static List<String> validateProductRequest(String name, String sku,
            Double price, Integer quantity, String category) {
        List<String> errors = new ArrayList<>();

        if (name == null || name.trim().isEmpty()) {
            errors.add("Product name is required");
        } else if (name.length() < 3 || name.length() > 200) {
            errors.add("Product name must be between 3 and 200 characters");
        }

        if (sku == null || sku.trim().isEmpty()) {
            errors.add("SKU is required");
        } else if (sku.length() > 50) {
            errors.add("SKU must not exceed 50 characters");
        }

        if (price == null || price < 0) {
            errors.add("Price must be provided and non-negative");
        } else if (price > 999999.99) {
            errors.add("Price is too high (max: 999999.99)");
        }

        if (quantity == null || quantity < 0) {
            errors.add("Quantity must be provided and non-negative");
        }

        return errors;
    }

    /**
     * Validate order request
     */
    public static List<String> validateOrderRequest(Long userId, Long productId,
            Integer quantity, String status) {
        List<String> errors = new ArrayList<>();

        if (userId == null || userId <= 0) {
            errors.add("Valid User ID is required");
        }

        if (productId == null || productId <= 0) {
            errors.add("Valid Product ID is required");
        }

        if (quantity == null || quantity <= 0) {
            errors.add("Quantity must be greater than 0");
        }

        if (quantity != null && quantity > 1000) {
            errors.add("Quantity cannot exceed 1000 units");
        }

        if (status != null && !isValidOrderStatus(status)) {
            errors.add("Invalid order status. Allowed: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED");
        }

        return errors;
    }

    /**
     * Check if status is valid
     */
    private static boolean isValidOrderStatus(String status) {
        return status.equals("PENDING") ||
                status.equals("CONFIRMED") ||
                status.equals("SHIPPED") ||
                status.equals("DELIVERED") ||
                status.equals("CANCELLED");
    }
}
