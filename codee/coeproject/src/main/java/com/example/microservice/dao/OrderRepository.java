package com.example.microservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.microservice.entity.Order;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

/**
 * OrderRepository (DAO) interface for Order entity
 * Provides database operations for Order entity using Spring Data JPA
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find order by order number
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Find all orders for a specific user
     */
    List<Order> findByUserId(Long userId);

    /**
     * Find orders by status
     */
    List<Order> findByStatus(String status);

    /**
     * Find orders for a user with specific status
     */
    List<Order> findByUserIdAndStatus(Long userId, String status);

    /**
     * Find orders created within a date range
     */
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count orders by user
     */
    Long countByUserId(Long userId);

    /**
     * Count orders by status
     */
    Long countByStatus(String status);
}
