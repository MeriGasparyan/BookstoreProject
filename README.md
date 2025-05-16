# 📚 BookstoreProject

A Spring Boot backend for a online bookstore with user registration, authentication, book search, ratings, shopping cart, checkout, CSV upload, and more.

---

## 🚀 Features

- 📖 Public book and author browsing
- 👥 User registration and authentication (JWT)
- ⭐ User reviews and ratings
- 🛒 Cart and checkout system
- 🧾 Order management with status tracking
- 🔐 Role-based access control with fine-grained permissions
- 📂 Admin CSV upload for bulk book import
- 🧪 Developer endpoints for generating test data

---

## 🧱 Technologies Used

- Java 17
- Spring Boot
- Spring Security (JWT)
- Hibernate / JPA
- PostgreSQL
- Lombok
- Maven

---

## 🧑‍💻 API Endpoints Overview

### 🔓 Public Endpoints (`/api/public`)

- `POST /register`: User registration
- `GET /books`: Browse/search books (`BookSearchCriteria`)
- `GET /authors`: Browse/search authors (`AuthorSearchCriteria`)

### 📦 Book-Related

- `GET /books/{bookId}/recommend`: Get genre-based book recommendations
- `GET /books/{id}/reviews`: Paginated book reviews
- `PUT /api/books/{id}`: Update book details.
- `DELETE /api/books/{id}`: Delete a book by id.
- `POST /api/books`: Post a new book.
#### Metadata related
- `POST /api/books/{id}/authors`: Add authors to a book. An author should be already created
- `DELETE /api/books/{id}/authors`: Remove an author from book. Note the author itself is not deleted
Similar endpoints for Awards, Characters, Genres, Settings
### ⭐ Ratings (`/api/ratings`)

- `DELETE /reviews/{reviewId}`: Delete a user’s review text (admins or owners)

### 🛒 Cart Management (`/api/cart`)

- `GET /cart`: Get current user's cart
- `POST /cart/add`: Add book to cart
- `PUT /cart/item/{cartItemId}`: Update quantity
- `DELETE /cart/item/{cartItemId}`: Remove item
- `DELETE /cart/clear`: Clear the cart

### 🧾 Orders (`/api/orders`)

- `POST /checkout`: Place an order
- `GET /my`: Get logged-in user's orders
- `GET /{orderId}`: Get single order
- `GET ?status={OrderStatus}`: Filter orders by status (admin only)
- `PUT /{orderId}/status`: Update order status
- `DELETE /{orderId}/cancel`: Cancel an order

### 🧪 Developer (`/api/dev`)
Please note that these set of endpoints exist for testing purposes only.
- `POST /admin/setup`: Create initial admin user
- `POST /generate-users`: Seed 500 fake users
- `POST /generate-ratings`: Generate fake ratings
- `POST /generate-purchases`: Generate fake purchases
### 📂 CSV Upload (`/api/csv`)

- `POST /upload`: Upload CSV file of books (role: `MANAGE_BOOK_METADATA`)

---

## 🔐 Roles & Permissions

🔐 JWT Authentication Required
-------------------------------------------
All protected routes must include the header:
Authorization: Bearer <token>

-------------------------------------------
👤 USER (Default Role)
-------------------------------------------
Permissions:
- `VIEW_BOOKS`
- `VIEW_AUTHORS`
- `VIEW_RATINGS`
- `VIEW_REVIEWS`
- `RATE_BOOKS`
- `POST_REVIEW`
- `EDIT_OWN_REVIEW`
- `DELETE_OWN_REVIEW`
- `BOOKMARK_BOOK`
- `SUGGEST_BOOK`
- `REQUEST_BOOK_COPY`
- `LIKE_REVIEW`
- `REPORT_REVIEW`
- `VIEW_PERSONAL_REVIEW_STATS`
- `DELETE_OWN_ACCOUNT`
- `DOWNLOAD_ACCOUNT_DATA`
- `ACCESS_BETA_FEATURES`
- `ADD_TO_CART`
- `MODIFY_CART`
- `PLACE_ORDER`
- `VIEW_ORDER_HISTORY`
- `CANCEL_OWN_ORDER`
- `REQUEST_REFUND`

Note: Some of the permissions listed above are granted via admin user update request.

-------------------------------------------
🛠️ ADMIN
-------------------------------------------
Full permission access

-------------------------------------------
🧩 EDITOR / REVIEWER / MODERATOR
-------------------------------------------
These are fine-grained, composable roles with scoped permissions such as:

- `MANAGE_BOOK_METADATA`     Manage genres, authors, etc.
- `DELETE_ANY_REVIEW`        Remove any review for moderation
- `MODERATE_RATINGS`         Flag/approve/review book ratings
- `APPROVE_REVIEWS`
- `FLAG_REVIEW_CONTENT`
- `VIEW_FLAGGED_CONTENT`
- `POST_PROFESSIONAL_REVIEW`
- `ACCESS_REVIEWER_DASHBOARD`

Use these roles to support content moderation, professional reviews, and catalog management.

-------------------------------------------
🧾 Example Use in API Routes
-------------------------------------------
Example protected route

`GET /api/books/recommend`

Headers:
Authorization: Bearer <token>

Only users with `VIEW_BOOKS` will be authorized.

-------------------------------------------


## 🛠️ Setup & Run

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/BookstoreProject.git
   cd BookstoreProject 

2. Update your `application.properties`
- `spring.datasource.url=jdbc:postgresql://localhost:5432/bookstore`
- `spring.datasource.username=your_user`
- `spring.datasource.password=your_password`
- If you want to download images to your machine as well update `image.processing.enabled=true`
- `image.path=/put/absolute/path/to/your/project/here/BookstoreProject/`
