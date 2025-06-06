# 🛒 Spring E-Commerce Backend

A robust and modular Spring Boot backend for an e-commerce application. This project is designed with clean architecture principles, DTO patterns, exception handling, pagination, and more to support a scalable shopping platform.

## 🚀 Features

- ✅ Category Management (CRUD)
- 📦 Product Management (Coming Soon)
- 🧾 DTO Pattern for Clean API Contracts
- ⚠️ Centralized Exception Handling
- 🧪 Field-level Validation with Hibernate Validator
- 📃 Standardized API Response Structure
- 📄 Pagination & Sorting Support
- 🔄 Service and Controller Layer Separation
- 🧰 Future Integration with Authentication & Authorization

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Hibernate Validator**
- **Lombok**
- **MySQL**
- **Maven**

---

## 🗂️ Project Structure

```bash
springecommercebackend/
├── src/
│   ├── main/
│   │   ├── java/com/bibek/ecommerce/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── payload/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── EcommerceApplication.java
│   └── resources/
│       └── application.properties
└── pom.xml
