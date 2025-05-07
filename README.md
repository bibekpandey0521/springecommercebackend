# Spring E-commerce Backend – Part 3

This branch focuses on implementing **pagination**, **sorting**, and a standardized **API response structure** in the Spring Boot e-commerce backend project.

## 📌 Features Implemented (May 7)

### ✅ Pagination
- Handled pagination parameters using `@RequestParam`.
- Utilized `Pageable` and `PageRequest` for efficient data retrieval.
- Updated controller methods to return paginated responses.

### ✅ Sorting
- Enabled dynamic sorting through query parameters.
- Configured default sorting behavior when parameters are not provided.

### ✅ API Response Restructuring
- Introduced a standardized `ApiResponse` class to wrap all API responses.
- Paginated endpoints now include additional metadata:
  - Total pages
  - Current page
  - Page size
  - Total elements
  - Sort direction

### ✅ Testing & Validation
- Manually tested changes through API endpoints.
- Ensured defaults and sorting options work with or without parameters.

## 🧰 Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate Validator
- ModelMapper
- H2/PostgreSQL

## 📂 GitHub Branch
[Visit part-3 branch](https://github.com/bibekpandey0521/springecommercebackend/tree/part-3)

## 🧪 Example API Call
