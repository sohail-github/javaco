package com.example.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import com.example.microservice.service.ProductService;
import com.example.microservice.entity.Product;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Product operations
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * GET /api/products - Get all active products
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Retrieved all products");
        response.put("products", productService.getAllProducts());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/products/{id} - Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@NonNull @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productService.getProductById(id);
            response.put("status", "success");
            response.put("message", "Retrieved product with ID: " + id);
            response.put("product", product);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/products/sku/{sku} - Get product by SKU
     */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Map<String, Object>> getProductBySku(@PathVariable String sku) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productService.getProductBySku(sku);
            response.put("status", "success");
            response.put("message", "Retrieved product with SKU: " + sku);
            response.put("product", product);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/products/category/{category} - Get products by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(@PathVariable String category) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Retrieved products for category: " + category);
        response.put("products", productService.getProductsByCategory(category));
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/products/search - Search products by name
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Search results for: " + name);
        response.put("products", productService.searchByName(name));
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/products - Create new product
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Map<String, Object> productRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            String name = (String) productRequest.get("name");
            String sku = (String) productRequest.get("sku");
            String description = (String) productRequest.get("description");
            java.math.BigDecimal price = new java.math.BigDecimal(productRequest.get("price").toString());
            Integer quantity = ((Number) productRequest.get("quantity")).intValue();
            String category = (String) productRequest.get("category");

            if (name == null || sku == null || price == null) {
                response.put("status", "error");
                response.put("message", "Name, SKU, and price are required");
                return ResponseEntity.badRequest().body(response);
            }

            Product createdProduct = productService.createProduct(name, sku, description, price, quantity, category);
            response.put("status", "success");
            response.put("message", "Product created successfully");
            response.put("product", createdProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to create product: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * PUT /api/products/{id} - Update product
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@NonNull @PathVariable Long id,
            @RequestBody Map<String, Object> productRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            String name = (String) productRequest.get("name");
            String sku = (String) productRequest.get("sku");
            String description = (String) productRequest.get("description");
            java.math.BigDecimal price = productRequest.get("price") != null
                    ? new java.math.BigDecimal(productRequest.get("price").toString())
                    : null;
            Integer quantity = productRequest.get("quantity") != null
                    ? ((Number) productRequest.get("quantity")).intValue()
                    : null;
            String category = (String) productRequest.get("category");

            Product updatedProduct = productService.updateProduct(id, name, sku, description, price, quantity,
                    category);
            response.put("status", "success");
            response.put("message", "Product updated successfully");
            response.put("product", updatedProduct);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * DELETE /api/products/{id} - Delete product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@NonNull @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            productService.deleteProduct(id);
            response.put("status", "success");
            response.put("message", "Product deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /api/products/lowstock/{threshold} - Get products with low stock
     */
    @GetMapping("/lowstock/{threshold}")
    public ResponseEntity<Map<String, Object>> getLowStockProducts(@PathVariable Integer threshold) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Products with stock <= " + threshold);
        response.put("products", productService.getLowStockProducts(threshold));
        return ResponseEntity.ok(response);
    }
}
