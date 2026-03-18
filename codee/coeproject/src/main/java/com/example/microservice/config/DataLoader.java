package com.example.microservice.config;

import com.example.microservice.dao.UserRepository;
import com.example.microservice.dao.ProductRepository;
import com.example.microservice.dao.OrderRepository;
import com.example.microservice.entity.User;
import com.example.microservice.entity.Product;
import com.example.microservice.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DataLoader - Initializes the database with production-grade sample data on
 * startup
 * Runs only if the database is empty
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Only load data if database is empty
            long userCount = userRepository.count();
            System.out.println("Checking existing users: " + userCount);
            if (userCount == 0) {
                loadData();
            } else {
                System.out.println("📊 Database already contains data. Skipping data load.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error during data loading: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Transactional
    private void loadData() {
        try {
            System.out.println("📦 Loading production data into database...");

            // Create sample users
            User user1 = new User("John Smith", "john.smith@techcorp.com", "+1-555-0101");
            user1.setIsActive(true);
            userRepository.save(user1);

            User user2 = new User("Sarah Johnson", "sarah.johnson@email.com", "+1-555-0102");
            user2.setIsActive(true);
            userRepository.save(user2);

            User user3 = new User("Michael Chen", "michael.chen@startup.io", "+1-555-0103");
            user3.setIsActive(true);
            userRepository.save(user3);

            User user4 = new User("Emily Davis", "emily.davis@business.com", "+1-555-0104");
            user4.setIsActive(true);
            userRepository.save(user4);

            User user5 = new User("Robert Wilson", "robert.wilson@enterprise.org", "+1-555-0105");
            user5.setIsActive(true);
            userRepository.save(user5);
            System.out.println("✅ Created 5 sample users");

            // Create sample products
            Product product1 = new Product(
                    "Professional Laptop",
                    "LAPTOP-PRO-001",
                    "High-performance 15-inch laptop with Intel i7, 16GB RAM, 512GB SSD. Ideal for developers and designers.",
                    new BigDecimal("1299.99"),
                    45,
                    "Electronics");
            product1.setIsActive(true);
            productRepository.save(product1);

            Product product2 = new Product(
                    "Wireless Mouse",
                    "MOUSE-WIRELESS-002",
                    "Ergonomic wireless mouse with 2.4GHz connection, 18-month battery life. Compatible with all OS.",
                    new BigDecimal("29.99"),
                    150,
                    "Accessories");
            product2.setIsActive(true);
            productRepository.save(product2);

            Product product3 = new Product(
                    "USB-C Cable (2m)",
                    "CABLE-USBC-003",
                    "Premium USB-C charging and data cable. 60A power delivery support, durable nylon braided.",
                    new BigDecimal("14.99"),
                    200,
                    "Cables");
            product3.setIsActive(true);
            productRepository.save(product3);

            Product product4 = new Product(
                    "Mechanical Keyboard",
                    "KEYBOARD-MECH-004",
                    "RGB mechanical keyboard with Cherry MX switches, aluminum frame, premium keycaps.",
                    new BigDecimal("149.99"),
                    78,
                    "Peripherals");
            product4.setIsActive(true);
            productRepository.save(product4);

            Product product5 = new Product(
                    "4K Monitor",
                    "MONITOR-4K-005",
                    "27-inch 4K UHD display with 99% DCI-P3 color gamut. Perfect for content creators.",
                    new BigDecimal("599.99"),
                    22,
                    "Displays");
            product5.setIsActive(true);
            productRepository.save(product5);

            Product product6 = new Product(
                    "Webcam 1080p",
                    "WEBCAM-1080-006",
                    "Professional 1080p @30fps webcam with auto-focus and built-in microphone.",
                    new BigDecimal("79.99"),
                    89,
                    "Cameras");
            product6.setIsActive(true);
            productRepository.save(product6);

            Product product7 = new Product(
                    "Laptop Stand",
                    "STAND-LAPTOP-007",
                    "Adjustable aluminum laptop stand, compatible with all 11-17 inch laptops. Reduces neck strain.",
                    new BigDecimal("39.99"),
                    134,
                    "Accessories");
            product7.setIsActive(true);
            productRepository.save(product7);

            Product product8 = new Product(
                    "USB Hub 7-Port",
                    "HUB-USB-008",
                    "Powered USB 3.0 hub with 7 ports. Supports simultaneous charging and data transfer.",
                    new BigDecimal("34.99"),
                    56,
                    "Hubs");
            product8.setIsActive(true);
            productRepository.save(product8);
            System.out.println("✅ Created 8 sample products");

            // Create sample orders
            Order order1 = new Order(
                    generateOrderNumber(),
                    user1.getId(),
                    product1.getId(),
                    1,
                    product1.getPrice());
            order1.setStatus("DELIVERED");
            order1.setShippingAddress("123 Tech Street, San Francisco, CA 94105");
            order1.setNotes("Fast shipping, excellent packaging");
            orderRepository.save(order1);

            Order order2 = new Order(
                    generateOrderNumber(),
                    user2.getId(),
                    product2.getId(),
                    3,
                    product2.getPrice());
            order2.setStatus("SHIPPED");
            order2.setShippingAddress("456 Business Ave, New York, NY 10001");
            order2.setNotes("Order placed for office supplies");
            orderRepository.save(order2);

            Order order3 = new Order(
                    generateOrderNumber(),
                    user3.getId(),
                    product4.getId(),
                    1,
                    product4.getPrice());
            order3.setStatus("CONFIRMED");
            order3.setShippingAddress("789 Startup Ln, Austin, TX 78701");
            order3.setNotes("Customer prefers standard delivery");
            orderRepository.save(order3);

            Order order4 = new Order(
                    generateOrderNumber(),
                    user4.getId(),
                    product5.getId(),
                    2,
                    product5.getPrice());
            order4.setStatus("PENDING");
            order4.setShippingAddress("321 Enterprise Blvd, Chicago, IL 60601");
            order4.setNotes("Awaiting payment confirmation");
            orderRepository.save(order4);

            Order order5 = new Order(
                    generateOrderNumber(),
                    user1.getId(),
                    product3.getId(),
                    5,
                    product3.getPrice());
            order5.setStatus("DELIVERED");
            order5.setShippingAddress("123 Tech Street, San Francisco, CA 94105");
            order5.setNotes("Bulk order for development team");
            orderRepository.save(order5);

            Order order6 = new Order(
                    generateOrderNumber(),
                    user5.getId(),
                    product6.getId(),
                    1,
                    product6.getPrice());
            order6.setStatus("CONFIRMED");
            order6.setShippingAddress("654 Corporate Dr, Boston, MA 02101");
            order6.setNotes("For video conference setup");
            orderRepository.save(order6);

            Order order7 = new Order(
                    generateOrderNumber(),
                    user2.getId(),
                    product7.getId(),
                    4,
                    product7.getPrice());
            order7.setStatus("SHIPPED");
            order7.setShippingAddress("456 Business Ave, New York, NY 10001");
            order7.setNotes("Ergonomic setup for team");
            orderRepository.save(order7);

            Order order8 = new Order(
                    generateOrderNumber(),
                    user3.getId(),
                    product8.getId(),
                    2,
                    product8.getPrice());
            order8.setStatus("DELIVERED");
            order8.setShippingAddress("789 Startup Ln, Austin, TX 78701");
            order8.setNotes("Setup complete and tested");
            orderRepository.save(order8);
            System.out.println("✅ Created 8 sample orders");

            System.out.println("🎉 Database initialization complete!");
            System.out.println("📊 Summary: 5 Users, 8 Products, 8 Orders loaded successfully");
        } catch (Exception e) {
            System.err.println("❌ Error during data insertion: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Generate a unique order number in format ORD-{timestamp}-{uuid}
     */
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
