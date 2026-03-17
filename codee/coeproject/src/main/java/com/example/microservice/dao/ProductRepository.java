package com.example.microservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.microservice.entity.Product;
import java.util.Optional;
import java.util.List;

/**
 * ProductRepository (DAO) interface for Product entity
 * Provides database operations for Product entity using Spring Data JPA
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find product by SKU
     */
    Optional<Product> findBySku(String sku);

    /**
     * Find products by category
     */
    List<Product> findByCategory(String category);

    /**
     * Find active products by name (case-insensitive)
     */
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    /**
     * Find active products
     */
    List<Product> findByIsActiveTrue();

    /**
     * Find products with low stock
     */
    List<Product> findByQuantityLessThanAndIsActiveTrueAndIsDeletedFalse(Integer quantity);
}
