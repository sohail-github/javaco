# API Quick Reference Guide

## Base URL
```
http://localhost:8081
```

---

## USER SERVICE - `/api/users`

### 1. Get All Users
```
GET /api/users
Response: List of all active users
```

### 2. Get User by ID
```
GET /api/users/1
Response: Single user object
```

### 3. Create User
```
POST /api/users
Body:
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+1-555-0100"
}
Response: Created user with ID
```

### 4. Update User
```
PUT /api/users/1
Body:
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "phone": "+1-555-0101"
}
Response: Updated user
```

### 5. Delete User
```
DELETE /api/users/1
Response: Success message
```

### 6. Health Check
```
GET /api/users/health
Response: { "status": "UP", "message": "User service is running" }
```

---

## PRODUCT SERVICE - `/api/products`

### 1. Get All Products
```
GET /api/products
Response: List of all active products
```

### 2. Get Product by ID
```
GET /api/products/1
Response: Single product object
```

### 3. Get Product by SKU
```
GET /api/products/sku/DELL-XPS-13
Response: Product with matching SKU
```

### 4. Get Products by Category
```
GET /api/products/category/Electronics
Response: All products in category
```

### 5. Search Products
```
GET /api/products/search?name=Laptop
Response: Products matching search term
```

### 6. Get Low Stock Products
```
GET /api/products/lowstock/10
Response: Products with quantity ≤ 10
```

### 7. Create Product
```
POST /api/products
Body:
{
  "name": "Dell XPS 13 Laptop",
  "sku": "DELL-XPS-13",
  "description": "High-performance ultrabook",
  "price": 1299.99,
  "quantity": 50,
  "category": "Electronics"
}
Response: Created product with ID
```

### 8. Update Product
```
PUT /api/products/1
Body:
{
  "name": "Dell XPS 13 - Updated",
  "sku": "DELL-XPS-13-V2",
  "price": 1349.99,
  "quantity": 45
}
Response: Updated product
```

### 9. Delete Product
```
DELETE /api/products/1
Response: Success message
```

---

## ORDER SERVICE - `/api/orders`

### 1. Get All Orders
```
GET /api/orders
Response: List of all orders
```

### 2. Get Order by ID
```
GET /api/orders/1
Response: Single order object
```

### 3. Get Order by Order Number
```
GET /api/orders/number/ORD-1234567890
Response: Order with matching number
```

### 4. Get User's Orders
```
GET /api/orders/user/1
Response: All orders for user ID 1
```

### 5. Get Orders by Status
```
GET /api/orders/status/PENDING
Response: All orders with PENDING status
Possible statuses: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
```

### 6. Get Order Statistics
```
GET /api/orders/statistics/all
Response: 
{
  "totalOrders": 10,
  "pendingOrders": 2,
  "confirmedOrders": 3,
  "shippedOrders": 2,
  "deliveredOrders": 2,
  "cancelledOrders": 1
}
```

### 7. Create Order
```
POST /api/orders
Body:
{
  "userId": 1,
  "productId": 1,
  "quantity": 2,
  "shippingAddress": "123 Main St, New York, NY"
}
Response: Created order with auto-generated order number
Note: Stock automatically updated, returns error if insufficient
```

### 8. Update Order Status
```
PUT /api/orders/1/status
Body:
{
  "status": "CONFIRMED"
}
Response: Updated order
Valid transitions:
  PENDING → CONFIRMED or CANCELLED
  CONFIRMED → SHIPPED or CANCELLED
  SHIPPED → DELIVERED
  DELIVERED → (no further changes)
  CANCELLED → (no further changes)
```

### 9. Cancel Order
```
PUT /api/orders/1/cancel
Response: Cancelled order, product stock restored
```

### 10. Delete Order
```
DELETE /api/orders/1
Response: Success message
```

---

## Status Codes

| Code | Meaning | Example Scenario |
|------|---------|------------------|
| 200 | OK | GET successful, PUT completed |
| 201 | Created | POST successful |
| 400 | Bad Request | Missing required field |
| 404 | Not Found | User/Product/Order not found |
| 500 | Server Error | Database error, unexpected issue |

---

## Common Request Headers
```
Content-Type: application/json
Accept: application/json
```

---

## Error Response Example
```json
{
  "status": "error",
  "statusCode": 404,
  "message": "User not found with ID: 999",
  "timestamp": "2024-01-01T12:00:00"
}
```

---

## Success Response Example
```json
{
  "status": "success",
  "message": "Retrieved all users",
  "users": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "phone": "+1-555-0100",
      "isActive": true,
      "createdAt": "2024-01-01T10:00:00"
    }
  ]
}
```

---

## Quick Testing Checklist

- [ ] User: Create → Read → Update → Delete
- [ ] Product: Create → Read → Search → Update → Delete
- [ ] Order: Create → Check Status → Update Status → Cancel → Delete
- [ ] Verify product stock decreases on order creation
- [ ] Verify stock restores on order cancellation
- [ ] Check order statistics endpoint
- [ ] Test error cases (invalid IDs, missing fields)
- [ ] Test status transitions (valid and invalid)

---

## MySQL Quick Commands

```bash
# Connect to MySQL
mysql -u root -p wowsql

# Select database
USE microservice_db123;

# View all users
SELECT * FROM users;

# View all products
SELECT * FROM products;

# View all orders
SELECT * FROM orders;

# Count records
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM products;
SELECT COUNT(*) FROM orders;
```

---

## Notes

- All timestamps are in UTC
- Soft deletes preserve data integrity (marked as deleted, not removed)
- Stock updates are transactional with order creation
- Order numbers are auto-generated and unique
- Dates are returned in ISO 8601 format

Made with ❤️ for Microservices Development
