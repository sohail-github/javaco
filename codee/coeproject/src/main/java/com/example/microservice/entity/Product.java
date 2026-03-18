package com.example.microservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Product Entity class for database persistence
 * Maps to the products table in MySQL database
 * Production-grade with timestamps, validation, and soft deletes
 */
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_sku", columnList = "sku"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name cannot be blank")
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @NotBlank(message = "SKU cannot be blank")
    @Column(name = "sku", nullable = false, unique = true, length = 50)
    private String sku;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Positive(message = "Price must be greater than 0")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Positive(message = "Quantity must be greater than or equal to 0")
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @JsonIgnore
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // Constructors
    public Product() {
    }

    public Product(String name, String sku, BigDecimal price) {
        this.name = name;
        this.sku = sku;
        this.price = price;
    }

    public Product(String name, String sku, String description, BigDecimal price, Integer quantity, String category) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    // JPA Lifecycle methods
    @PrePersist
    protected void onCreate() {
        if (createdBy == null) {
            createdBy = "SYSTEM";
        }
        if (updatedBy == null) {
            updatedBy = "SYSTEM";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (updatedBy == null) {
            updatedBy = "SYSTEM";
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
