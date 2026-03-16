# Simple Microservice - Spring Boot

A simple Spring Boot microservice with REST API for user management.

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Maven 3.9+

### Run the Application

```bash
# 1. Build the project
mvn clean install

# 2. Run the application
mvn spring-boot:run
```

Server will be available at: `http://localhost:8081`

## 📁 Project Structure

```
src/main/
├── java/com/example/microservice/
│   ├── App.java                    # Main Spring Boot application
│   ├── controller/
│   │   └── UserController.java     # REST API endpoints
│   └── service/
│       └── UserService.java        # Business logic
└── resources/
    └── application.properties      # Configuration
```

## 🔑 API Endpoints

### Health Check
```bash
curl http://localhost:8081/api/users/health
```

### Get All Users
```bash
curl http://localhost:8081/api/users
```

### Get User by ID
```bash
curl http://localhost:8081/api/users/1
```

### Create User
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com"}'
```

### Update User
```bash
curl -X PUT http://localhost:8081/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"John Updated","email":"john.new@example.com"}'
```

### Delete User
```bash
curl -X DELETE http://localhost:8081/api/users/1
```

## 📊 Built-in Features

✅ REST API with full CRUD operations  
✅ Spring Boot 3.2.0  
✅ Java 21  
✅ DevTools for hot reload  
✅ Health check endpoint  
✅ CORS enabled  
✅ JSON request/response handling  

## 📝 Configuration

Edit `src/main/resources/application.properties` to customize:

```properties
# Change port
server.port=9090

# Enable debug logging
logging.level.com.example.microservice=DEBUG
```

## 🧪 Testing with Postman

Import `postman_collection.json` to Postman for full API testing with pre-configured requests and tests.

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Web MVC Guide](https://spring.io/guides/gs/serving-web-content/)
- [REST API Best Practices](https://spring.io/guides/tutorials/rest/)
