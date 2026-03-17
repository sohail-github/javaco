package com.example.microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import com.example.microservice.dao.ProductRepository;
import com.example.microservice.entity.Product;
import com.example.microservice.exception.ResourceNotFoundException;
import com.example.microservice.exception.InvalidOperationException;
import java.util.*;

/**
 * Service class for Product operations
 * Contains business logic for managing products
 * Uses ProductRepository (DAO) for database operations
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all active products
     */
    public List<Product> getAllProducts() {
        return productRepository.findByIsActiveTrue();
    }

    /**
     * Get product by ID
     */
    public Product getProductById(@NonNull Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    /**
     * Get product by SKU
     */
    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
    }

    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Search products by name
     */
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
    }

    /**
     * Create a new product
     */
    public Product createProduct(String name, String sku, String description,
            java.math.BigDecimal price, Integer quantity, String category) {
        Product product = new Product(name, sku, description, price, quantity, category);
        return productRepository.save(product);
    }

    /**
     * Update existing product
     */
    public Product updateProduct(@NonNull Long id, String name, String sku,
            String description, java.math.BigDecimal price,
            Integer quantity, String category) {
        Product product = getProductById(id);
        if (name != null)
            product.setName(name);
        if (sku != null)
            product.setSku(sku);
        if (description != null)
            product.setDescription(description);
        if (price != null)
            product.setPrice(price);
        if (quantity != null)
            product.setQuantity(quantity);
        if (category != null)
            product.setCategory(category);
        return productRepository.save(product);
    }

    /**
     * Delete product (soft delete)
     */
    public void deleteProduct(@NonNull Long id) {
        Product product = getProductById(id);
        product.setIsDeleted(true);
        product.setIsActive(false);
        productRepository.save(product);
    }

    /**
     * Get low stock products (quantity <= threshold)
     */
    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findByQuantityLessThanAndIsActiveTrueAndIsDeletedFalse(threshold);
    }

    /**
     * Update product stock after order
     */
    public void updateStock(@NonNull Long productId, Integer quantityOrdered) {
        Product product = getProductById(productId);
        int newQuantity = product.getQuantity() - quantityOrdered;

        if (newQuantity < 0) {
            throw new InvalidOperationException("Insufficient stock for product: " + product.getName());
        }

        product.setQuantity(newQuantity);
        productRepository.save(product);
    }

    /**
     * Activate product
     */
    public Product activateProduct(@NonNull Long id) {
        Product product = getProductById(id);
        product.setIsActive(true);
        product.setIsDeleted(false);
        return productRepository.save(product);
    }
}
