# Postman Collection Setup Guide

## Overview
This is a complete Postman collection for the **3 Microservices** (User, Product, Order) API.
All endpoints are production-ready with proper error handling and validation.

## Files

1. **Microservices_API.postman_collection.json** - Main API collection
2. **Microservices_Environment.postman_environment.json** - Environment variables

## Setup Instructions

### Step 1: Import Collection
1. Open Postman
2. Click **Import** (top-left corner)
3. Select **Microservices_API.postman_collection.json**
4. Click **Import**

### Step 2: Import Environment
1. Click **Settings** (gear icon, top-right)
2. Click **Environments**
3. Click **Import**
4. Select **Microservices_Environment.postman_environment.json**
5. Click **Import**

### Step 3: Select Environment
1. Click the environment dropdown (top-right)
2. Select **Microservices Environment**

### Step 4: Verify Base URL
- Default: `http://localhost:8081`
- Application runs on port **8081**

## API Endpoints Overview

### User Service (`/api/users`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| GET | `/api/users/health` | Health check |

**Create User Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "+1-555-0100"
}
```

**Update User Body:**
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "phone": "+1-555-0101"
}
```

---

### Product Service (`/api/products`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/sku/{sku}` | Get by SKU |
| GET | `/api/products/category/{category}` | Get by category |
| GET | `/api/products/search?name={name}` | Search products |
| GET | `/api/products/lowstock/{threshold}` | Low stock check |
| POST | `/api/products` | Create product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |

**Create Product Body:**
```json
{
  "name": "Dell XPS 13 Laptop",
  "sku": "DELL-XPS-13",
  "description": "High-performance ultrabook with 13.3 inch FHD display",
  "price": 1299.99,
  "quantity": 50,
  "category": "Electronics"
}
```

**Update Product Body:**
```json
{
  "name": "Dell XPS 13 Laptop - Updated",
  "sku": "DELL-XPS-13-V2",
  "description": "Updated high-performance ultrabook",
  "price": 1349.99,
  "quantity": 45,
  "category": "Electronics"
}
```

---

### Order Service (`/api/orders`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| GET | `/api/orders/number/{orderNumber}` | Get by order number |
| GET | `/api/orders/user/{userId}` | Get user's orders |
| GET | `/api/orders/status/{status}` | Get by status |
| GET | `/api/orders/statistics/all` | Order statistics |
| POST | `/api/orders` | Create order |
| PUT | `/api/orders/{id}/status` | Update status |
| PUT | `/api/orders/{id}/cancel` | Cancel order |
| DELETE | `/api/orders/{id}` | Delete order |

**Create Order Body:**
```json
{
  "userId": 1,
  "productId": 1,
  "quantity": 2,
  "shippingAddress": "123 Main St, New York, NY 10001"
}
```

**Update Order Status Body:**
```json
{
  "status": "CONFIRMED"
}
```

**Valid Order Statuses:**
- `PENDING` - Initial status
- `CONFIRMED` - Order confirmed
- `SHIPPED` - Order shipped
- `DELIVERED` - Order delivered
- `CANCELLED` - Order cancelled

**Valid Status Transitions:**
- PENDING → CONFIRMED, PENDING → CANCELLED
- CONFIRMED → SHIPPED, CONFIRMED → CANCELLED
- SHIPPED → DELIVERED
- DELIVERED (no further changes)
- CANCELLED (no further changes)

---

## Example Workflow

### 1. Create a User
```
POST /api/users
{
  "name": "Alice Smith",
  "email": "alice@example.com",
  "phone": "+1-555-0200"
}
```
**Response:** User ID (e.g., 1)

### 2. Create a Product
```
POST /api/products
{
  "name": "iPhone 15 Pro",
  "sku": "IPHONE-15-PRO",
  "description": "Latest Apple smartphone",
  "price": 999.99,
  "quantity": 100,
  "category": "Electronics"
}
```
**Response:** Product ID (e.g., 1)

### 3. Create an Order
```
POST /api/orders
{
  "userId": 1,
  "productId": 1,
  "quantity": 1,
  "shippingAddress": "456 Oak Ave, Los Angeles, CA 90001"
}
```
**Response:** Order created with auto-generated order number

### 4. Check Order Status
```
GET /api/orders/1
```

### 5. Update Order Status
```
PUT /api/orders/1/status
{
  "status": "CONFIRMED"
}
```

### 6. Get Product (Stock Updated)
```
GET /api/products/1
```
Note: Quantity decreased by 1 (100 → 99)

### 7. Cancel Order
```
PUT /api/orders/1/cancel
```
Note: Product quantity restored (99 → 100)

---

## Environment Variables

Edit in Postman Settings → Environments:

| Variable | Default Value | Description |
|----------|---------------|-------------|
| `base_url` | `http://localhost:8081` | API base URL |
| `user_id` | `1` | User ID for testing |
| `product_id` | `1` | Product ID for testing |
| `order_id` | `1` | Order ID for testing |

**Usage in requests:**
- `{{base_url}}` - API base URL
- `{{user_id}}` - Current user ID
- `{{product_id}}` - Current product ID
- `{{order_id}}` - Current order ID

---

## Response Format

All successful responses follow this format:
```json
{
  "status": "success",
  "message": "Operation successful",
  "data": { /* ... */ }
}
```

Error responses:
```json
{
  "status": "error",
  "message": "Error description",
  "statusCode": 400
}
```

---

## Common Response Codes

| Code | Meaning |
|------|---------|
| 200 | OK - Successful GET, PUT |
| 201 | Created - Successful POST |
| 400 | Bad Request - Invalid input |
| 404 | Not Found - Resource doesn't exist |
| 500 | Internal Server Error |

---

## Database Configuration

The microservices use **MySQL 8.0** at:
- **URL:** `jdbc:mysql://localhost:3306/microservice_db123`
- **Username:** `root`
- **Password:** `wowsql`

Ensure MySQL is running before starting the application.

---

## Running the Application

```bash
cd c:\Users\Sohail\Documents\ppp\javaco\javaco\codee\coeproject
mvn spring-boot:run
```

Application will start on: `http://localhost:8081`

---

## Tips & Best Practices

1. **Always create User first** before creating Orders
2. **Create Products before creating Orders** referencing them
3. **Check stock availability** via GET `/api/products/{id}` before ordering
4. **Monitor low stock** using `/api/products/lowstock/{threshold}`
5. **Follow order status workflow** to avoid invalid transitions
6. **Use timestamps** when querying for performance analysis
7. **Soft deletes** preserve data integrity - deleted records are marked, not removed

---

## Troubleshooting

### Connection Refused
- Ensure application is running on port 8081
- Check MySQL is running

### Database Error
- Verify MySQL credentials in `application.properties`
- Ensure database `microservice_db123` exists

### Validation Error
- Check required fields in request body
- Ensure email format is valid for User creation
- Ensure price is positive for Products

---

## Support

For API documentation, see inline comments in the collection.
For implementation details, refer to the source code in the microservices.

Happy testing! 🚀
