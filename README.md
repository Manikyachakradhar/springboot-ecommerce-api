# E-Commerce Backend (Spring Boot)

## Overview
This is a Spring Boot-based backend for a simple e-commerce application.  
It includes:

- JWT authentication (USER & ADMIN roles)  
- Role-based access control  
- Cart management  
- Order placement, cancellation, and order history  
- Admin order management with *status lifecycle*  
- Stock validation & restoration  

---

## Features

### Authentication
- *Login/Register* endpoints  
- JWT-based authentication  
- Role-based authorization (USER / ADMIN)

### Cart
- Add products to cart  
- Update quantity  
- Prevent adding more than available stock  

### Orders
- Place order from cart  
- Cancel order (USER or ADMIN)  
- Order status lifecycle: CREATED → PAID → SHIPPED → DELIVERED → CANCELLED  
- Stock restoration on cancellation  
- Ownership validation (users cannot cancel others’ orders)

### Admin
- Product CRUD (CREATE/UPDATE/DELETE)  
- Update order status  
- Only ADMIN can perform admin operations  

### Additional
- Order history (for users)  
- Streamlined service layer for business logic  

---

## Database Schema

## H2 Database Console
H2 console may be blocked due to Spring Security. 
All functionality can be tested via Postman.

## Use internal data base Examle post gres
spring.datasource.url=jdbc:jdbc:postgresql://localhost:5432/<YOUR_DB_NAME>
spring.datasource.username=<YOUR_DB_USERNAME>
spring.datasource.password=<YOUR_DB_PASSWORD>

*Product*  
- id, name, description, price, quantity, category  

*User*  
- id, name, email, password, role(ADMIN OR USER)  

*Cart*  
- id, user, items (CartItem)  

*CartItem*  
- id, product, quantity, price, cart  

*Order*  
- id, user, items (OrderItem), totalAmount, status, createdAt  

*OrderItem*  
- id, product, quantity, price, order  

## Endpoints Summary

### Authentication
- POST /api/auth/login – login user/admin  
- POST /api/auth/register – register user  

### Products
- GET /api/products – list all products  
- POST /api/products – create product (ADMIN only)  
- DELETE /api/products/{id} – delete product (ADMIN only)  

### Cart (USER)
- POST /api/cart/add – add product to cart  
- GET /api/cart – view cart  

### Orders (USER)
- POST /api/order – place order from cart  
- PUT /api/order/cancel/{id}– cancel own order  
- GET /api/orders/history – view order history  

### Admin Orders
- PUT /api/admin/order/{id}/status?status=PAID|SHIPPED|DELIVERED|CANCELLED – update order status  



## Sample Postman Requests
## Register a USER

Endpoint: POST /api/auth/register
Headers:

Content-Type: application/json

Body:

{
  "name": "John Doe",
  "email": "user@example.com",
  "password": "password123",
  "role": "USER"
}
## Register an ADMIN

Endpoint: POST /api/auth/register
Headers:

Content-Type: application/json

Body:

{
  "name": "Admin User",
  "email": "admin@example.com",
  "password": "admin123",
  "role": "ADMIN"
}
## User Login

Endpoint: POST /api/auth/login?email=user@example.com&password=password123

Response:

{
  "token": "<USER_JWT_TOKEN>"
}
## Admin Login

Endpoint: POST /api/auth/login?email=admin@example.com&password=admin123
Response:

{
  "token": "<ADMIN_JWT_TOKEN>"
}
## List Products (Authenticated)

Endpoint: GET /api/products
Headers:

Authorization: Bearer <USER_JWT_TOKEN>

## Add Product (ADMIN Only)

Endpoint: POST /api/products
Headers:

Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: application/json

Body:

{
  "name": "Headphones",
  "description": "Wireless headphones",
  "price": 150.0,
  "quantity": 50,
  "category": "Electronics"
}
## Add to Cart (USER Only)

Endpoint: POST /api/cart/add
Headers:

Authorization: Bearer <USER_JWT_TOKEN>
Content-Type: application/json

Body:

{
  "productId": 1,
  "quantity": 2
}
## Place Order (USER Only)

Endpoint: POST /api/order
Headers:

Authorization: Bearer <USER_JWT_TOKEN>

## Get Order History

Endpoint: POST api/order/history
Response:
[
    {
        "orderId": 1,
        "createdAt": "CURRENT TIME",
        "status": "CREATED",
        "totalAmount": 300.0
    }
]
Authorization: Bearer <USER_JWT_TOKEN>



