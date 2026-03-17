package com.example.microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import com.example.microservice.dao.OrderRepository;
import com.example.microservice.entity.Order;
import com.example.microservice.exception.ResourceNotFoundException;
import com.example.microservice.exception.InvalidOperationException;
import java.time.LocalDateTime;

import java.util.*;
import java.util.UUID;

/**
 * Service class for Order operations
 * Contains business logic for managing orders
 * Uses OrderRepository (DAO) for database operations
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    // Order status constants
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_SHIPPED = "SHIPPED";
    public static final String STATUS_DELIVERED = "DELIVERED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    /**
     * Get all orders
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Get order by ID
     */
    public Order getOrderById(@NonNull Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }

    /**
     * Get order by order number
     */
    public Order getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with number: " + orderNumber));
    }

    /**
     * Get all orders for a user
     */
    public List<Order> getOrdersByUserId(@NonNull Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * Get user orders with specific status
     */
    public List<Order> getUserOrdersByStatus(@NonNull Long userId, String status) {
        return orderRepository.findByUserIdAndStatus(userId, status);
    }

    /**
     * Create a new order
     */
    public Order createOrder(Long userId, Long productId, Integer quantity,
            String shippingAddress) {
        // Validate product exists and get details
        var product = productService.getProductById(productId);

        // Validate quantity
        if (quantity <= 0) {
            throw new InvalidOperationException("Quantity must be greater than 0");
        }

        // Check stock
        if (product.getQuantity() < quantity) {
            throw new InvalidOperationException("Insufficient stock. Available: " + product.getQuantity());
        }

        // Create order with unique order number
        String orderNumber = "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
        Order order = new Order(orderNumber, userId, productId, quantity, product.getPrice());
        order.setShippingAddress(shippingAddress);
        order.setStatus(STATUS_PENDING);

        // Update product stock
        productService.updateStock(productId, quantity);

        return orderRepository.save(order);
    }

    /**
     * Update order status
     */
    public Order updateOrderStatus(@NonNull Long orderId, String newStatus) {
        Order order = getOrderById(orderId);

        // Validate status transition
        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new InvalidOperationException(
                    "Invalid status transition from " + order.getStatus() + " to " + newStatus);
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    /**
     * Cancel order
     */
    public Order cancelOrder(@NonNull Long orderId) {
        Order order = getOrderById(orderId);

        if (order.getStatus().equals(STATUS_DELIVERED) || order.getStatus().equals(STATUS_CANCELLED)) {
            throw new InvalidOperationException("Cannot cancel order with status: " + order.getStatus());
        }

        order.setStatus(STATUS_CANCELLED);

        // Restore product stock
        var product = productService.getProductById(order.getProductId());
        product.setQuantity(product.getQuantity() + order.getQuantity());

        return orderRepository.save(order);
    }

    /**
     * Delete order (soft delete)
     */
    public void deleteOrder(@NonNull Long orderId) {
        Order order = getOrderById(orderId);
        order.setIsDeleted(true);
        orderRepository.save(order);
    }

    /**
     * Get order statistics
     */
    public Map<String, Object> getOrderStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", orderRepository.count());
        stats.put("pendingOrders", orderRepository.countByStatus(STATUS_PENDING));
        stats.put("confirmedOrders", orderRepository.countByStatus(STATUS_CONFIRMED));
        stats.put("shippedOrders", orderRepository.countByStatus(STATUS_SHIPPED));
        stats.put("deliveredOrders", orderRepository.countByStatus(STATUS_DELIVERED));
        stats.put("cancelledOrders", orderRepository.countByStatus(STATUS_CANCELLED));
        return stats;
    }

    /**
     * Get orders for date range
     */
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCreatedAtBetween(startDate, endDate);
    }

    /**
     * Validate status transitions
     */
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus.equals(newStatus))
            return true;

        switch (currentStatus) {
            case STATUS_PENDING:
                return newStatus.equals(STATUS_CONFIRMED) || newStatus.equals(STATUS_CANCELLED);
            case STATUS_CONFIRMED:
                return newStatus.equals(STATUS_SHIPPED) || newStatus.equals(STATUS_CANCELLED);
            case STATUS_SHIPPED:
                return newStatus.equals(STATUS_DELIVERED);
            case STATUS_DELIVERED:
            case STATUS_CANCELLED:
                return false;
            default:
                return false;
        }
    }
}
