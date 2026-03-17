package com.example.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import com.example.microservice.service.OrderService;
import com.example.microservice.entity.Order;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Order operations
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * GET /api/orders - Get all orders
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Retrieved all orders");
        response.put("orders", orderService.getAllOrders());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/orders/{id} - Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@NonNull @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order order = orderService.getOrderById(id);
            response.put("status", "success");
            response.put("message", "Retrieved order with ID: " + id);
            response.put("order", order);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/orders/number/{orderNumber} - Get order by order number
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<Map<String, Object>> getOrderByNumber(@PathVariable String orderNumber) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order order = orderService.getOrderByNumber(orderNumber);
            response.put("status", "success");
            response.put("message", "Retrieved order with number: " + orderNumber);
            response.put("order", order);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/orders/user/{userId} - Get all orders for a user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getOrdersByUserId(@NonNull @PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Retrieved orders for user: " + userId);
        response.put("orders", orderService.getOrdersByUserId(userId));
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/orders/status/{status} - Get orders by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getOrdersByStatus(@PathVariable String status) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Retrieved orders with status: " + status);
        response.put("orders", orderService.getOrdersByStatus(status));
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/orders - Create new order
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> orderRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long userId = ((Number) orderRequest.get("userId")).longValue();
            Long productId = ((Number) orderRequest.get("productId")).longValue();
            Integer quantity = ((Number) orderRequest.get("quantity")).intValue();
            String shippingAddress = (String) orderRequest.get("shippingAddress");

            if (userId == null || productId == null || quantity == null) {
                response.put("status", "error");
                response.put("message", "User ID, Product ID, and quantity are required");
                return ResponseEntity.badRequest().body(response);
            }

            Order createdOrder = orderService.createOrder(userId, productId, quantity, shippingAddress);
            response.put("status", "success");
            response.put("message", "Order created successfully");
            response.put("order", createdOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to create order: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * PUT /api/orders/{id}/status - Update order status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@NonNull @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String newStatus = request.get("status");
            if (newStatus == null) {
                response.put("status", "error");
                response.put("message", "Status is required");
                return ResponseEntity.badRequest().body(response);
            }

            Order updatedOrder = orderService.updateOrderStatus(id, newStatus);
            response.put("status", "success");
            response.put("message", "Order status updated successfully");
            response.put("order", updatedOrder);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * PUT /api/orders/{id}/cancel - Cancel order
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@NonNull @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order cancelledOrder = orderService.cancelOrder(id);
            response.put("status", "success");
            response.put("message", "Order cancelled successfully");
            response.put("order", cancelledOrder);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * DELETE /api/orders/{id} - Delete order
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@NonNull @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            orderService.deleteOrder(id);
            response.put("status", "success");
            response.put("message", "Order deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/orders/statistics - Get order statistics
     */
    @GetMapping("/statistics/all")
    public ResponseEntity<Map<String, Object>> getOrderStatistics() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Retrieved order statistics");
        response.put("statistics", orderService.getOrderStatistics());
        return ResponseEntity.ok(response);
    }
}
