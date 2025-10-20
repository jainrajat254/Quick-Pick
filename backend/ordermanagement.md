# Order Management API Documentation
---

## 1. VENDOR ENDPOINTS - Menu Management

### 1.1 Create Menu Item
**POST** `/api/menu-items`
**Role:** VENDOR

```json
{
  "name": "Chicken Burger",
  "description": "Delicious grilled chicken burger with fresh vegetables",
  "price": 150.0,
  "quantity": 50,
  "category": "Burgers",
  "isVeg": false,
  "imageUrl": "https://example.com/chicken-burger.jpg",
  "isAvailable": true
}
```

### 1.4 Get My Menu Items (Paginated)
**GET** `/api/menu-items/my-menu?page=0&size=20`
**Role:** VENDOR


### 1.5 Get My Available Menu Items
**GET** `/api/menu-items/my-menu/available`
**Role:** VENDOR


### 1.6 Get My Menu Items by Category
**GET** `/api/menu-items/my-menu/category/Burgers`
**Role:** VENDOR

No body required

### 1.7 Search My Menu Items
**GET** `/api/menu-items/my-menu/search?query=chicken`
**Role:** VENDOR

No body required

### 1.8 Get Menu Items by Price Range
**GET** `/api/menu-items/my-menu/price-range?minPrice=50&maxPrice=200`
**Role:** VENDOR

No body required

### 1.9 Get My Menu Categories
**GET** `/api/menu-items/my-menu/categories`
**Role:** VENDOR

No body required

### 1.10 Update Menu Item
**PUT** `/api/menu-items/{menuItemId}`
**Role:** VENDOR

```json
{
  "name": "Chicken Burger Deluxe",
  "description": "Premium grilled chicken burger with cheese and bacon",
  "price": 180.0,
  "quantity": 40,
  "category": "Burgers",
  "isVeg": false,
  "imageUrl": "https://example.com/chicken-burger-deluxe.jpg",
  "isAvailable": true
}
```

### 1.11 Toggle Menu Item Availability
**PATCH** `/api/menu-items/{menuItemId}/toggle-availability`
**Role:** VENDOR

No body required

### 1.12 Update Menu Item Quantity
**PATCH** `/api/menu-items/{menuItemId}/quantity?quantity=25`
**Role:** VENDOR

No body required

### 1.13 Delete Menu Item
**DELETE** `/api/menu-items/{menuItemId}`
**Role:** VENDOR

No body required

### 1.14 Delete Multiple Menu Items
**DELETE** `/api/menu-items/bulk`
**Role:** VENDOR

```json
{
  "menuItemIds": ["menuItemId1", "menuItemId2", "menuItemId3"]
}
```

### 1.15 Get Menu Item Stats
**GET** `/api/menu-items/my-menu/stats`
**Role:** VENDOR

No body required

---

## 2. STUDENT ENDPOINTS - Browse & Order

### 2.1 Get Public Vendor Menu
**GET** `/api/menu-items/vendor/{vendorId}`
**Role:** PUBLIC (No auth required)

No body required

### 2.2 Get Vendor Menu by Category
**GET** `/api/menu-items/vendor/{vendorId}/category/Burgers`
**Role:** PUBLIC (No auth required)

No body required

### 2.3 Get Menu Item by ID
**GET** `/api/menu-items/{menuItemId}`
**Role:** PUBLIC (No auth required)

No body required

### 2.4 Create Order
**POST** `/api/orders`
**Role:** STUDENT

```json
{
  "vendorId": "vendor_user_id_here",
  "orderItems": [
    {
      "menuItemId": "menu_item_id_1",
      "quantity": 2
    },
    {
      "menuItemId": "menu_item_id_2",
      "quantity": 1
    }
  ],
  "specialInstructions": "Please make it extra spicy and pack separately"
}
```

### 2.5 Get Order by ID
**GET** `/api/orders/{orderId}`
**Role:** STUDENT

No body required

### 2.6 Get My Orders
**GET** `/api/orders/my-orders`
**Role:** STUDENT

No body required

### 2.7 Get My Orders by Status
**GET** `/api/orders/my-orders/status/PENDING`
**Role:** STUDENT

Available statuses: `PENDING`, `CONFIRMED`, `PREPARING`, `READY`, `COMPLETED`, `CANCELLED`, `REJECTED`

### 2.8 Cancel Order
**PATCH** `/api/orders/{orderId}/cancel`
**Role:** STUDENT

No body required

### 2.9 Get My Order Stats
**GET** `/api/orders/my-orders/stats`
**Role:** STUDENT

No body required

---

## 3. VENDOR ENDPOINTS - Order Management

### 3.1 Get Vendor Orders (Paginated)
**GET** `/api/orders/vendor/orders/paginated?page=0&size=20`
**Role:** VENDOR

No body required

### 3.2 Get Vendor Order by ID
**GET** `/api/orders/vendor/orders/{orderId}`
**Role:** VENDOR

No body required

### 3.3 Get Vendor Orders by Status
**GET** `/api/orders/vendor/orders/status/PENDING`
**Role:** VENDOR

Available statuses: `PENDING`, `CONFIRMED`, `PREPARING`, `READY`, `COMPLETED`, `CANCELLED`, `REJECTED`

### 3.4 Update Order Status - CONFIRM Order
**PATCH** `/api/orders/vendor/orders/{orderId}/status`
**Role:** VENDOR

```json
{
  "orderStatus": "ACCEPTED"
}
```

### 3.9 Get Vendor Order Stats
**GET** `/api/orders/vendor/orders/stats`
**Role:** VENDOR

No body required

---

## 4. STUDENT ENDPOINTS - Review Management

### 4.1 Create Review (After Order Completion)
**POST** `/api/reviews`
**Role:** STUDENT

```json
{
  "vendorId": "vendor_user_id_here",
  "orderId": "completed_order_id_here",
  "rating": 5,
  "comment": "Excellent food! Quick service and delicious taste. Highly recommend!"
}
```

### 4.2 Create Another Review (Lower Rating)
**POST** `/api/reviews`
**Role:** STUDENT

```json
{
  "vendorId": "vendor_user_id_here",
  "orderId": "another_completed_order_id_here",
  "rating": 3,
  "comment": "Food was okay but delivery took longer than expected."
}
```

### 4.3 Get My Reviews
**GET** `/api/reviews/user/my-reviews`
**Role:** STUDENT

No body required

### 4.4 Update Review
**PUT** `/api/reviews/{reviewId}`
**Role:** STUDENT

```json
{
  "vendorId": "vendor_user_id_here",
  "orderId": "order_id_here",
  "rating": 4,
  "comment": "Updated: Service improved! Changed rating from 3 to 4."
}
```

### 4.5 Delete Review
**DELETE** `/api/reviews/{reviewId}`
**Role:** STUDENT

No body required

### 4.6 Check if User Reviewed Order
**GET** `/api/reviews/order/{orderId}/exists`
**Role:** STUDENT

No body required

### 4.7 Get My Review Count
**GET** `/api/reviews/user/count`
**Role:** STUDENT

No body required

---

## 5. PUBLIC ENDPOINTS - View Reviews

### 5.1 Get Review by ID
**GET** `/api/reviews/{reviewId}`
**Role:** PUBLIC

No body required

### 5.2 Get Reviews by Vendor
**GET** `/api/reviews/vendor/{vendorId}`
**Role:** PUBLIC

No body required

### 5.3 Get Reviews by Vendor (Paginated)
**GET** `/api/reviews/vendor/{vendorId}/paginated?page=0&size=10`
**Role:** PUBLIC

No body required

### 5.4 Get Reviews by Rating
**GET** `/api/reviews/vendor/{vendorId}/rating/5`
**Role:** PUBLIC

Rating can be 1-5

### 5.5 Get Reviews Above Minimum Rating
**GET** `/api/reviews/vendor/{vendorId}/rating-above/4`
**Role:** PUBLIC

Gets all reviews with rating >= 4

### 5.6 Get Vendor Rating Statistics
**GET** `/api/reviews/vendor/{vendorId}/statistics`
**Role:** PUBLIC

Returns average rating, total reviews, rating distribution

### 5.7 Get Review Count by Vendor
**GET** `/api/reviews/vendor/{vendorId}/count`
**Role:** PUBLIC

No body required

---

## 6. ADMIN ENDPOINTS - Review Moderation

### 6.1 Delete Review (Admin)
**DELETE** `/api/reviews/admin/{reviewId}`
**Role:** ADMIN

No body required

---


## WEBSOCKET ENDPOINTS

### WebSocket Connection
Connect to: `ws://localhost:8080/ws`

Protocol: STOMP over WebSocket with SockJS fallback

### Subscribe to Order Updates (Student)
```
SUBSCRIBE /user/queue/orders
```

### Subscribe to Order Updates (Vendor)
```
SUBSCRIBE /user/queue/vendor-orders
```

### Ping/Pong Test
```
SEND /app/ping
{
  "message": "test"
}
```

Response on: `/user/queue/pong`

---


