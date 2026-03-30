# auth-service

A **Spring Boot** microservice responsible for user authentication and JWT token issuance within the banking platform. It handles user registration, login, and acts as the identity provider for other services like `account-service`.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running with Docker](#running-with-docker)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Security](#security)
- [Relation to Other Services](#relation-to-other-services)
- [Building & Testing](#building--testing)

---

## Overview

`auth-service` is a dedicated authentication microservice in the banking system. It manages user registration and login, issuing signed JWT tokens that downstream services (such as `account-service`) use to verify identity and enforce access control.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.5.x |
| Language | Java 17 |
| Database | PostgreSQL |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| ORM | Spring Data JPA |
| Validation | Spring Validation |
| Cloud | Spring Cloud 2025.0.1 |
| Build Tool | Maven (Maven Wrapper included) |
| Containerisation | Docker |

---

## Prerequisites

- Java 17+
- Maven 3.8+ (or use the included `./mvnw`)
- PostgreSQL instance

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/apramania/auth-service.git
cd auth-service
```

### 2. Configure environment

Set the following in `src/main/resources/application.properties` (or via environment variables):

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/your_auth_db
spring.datasource.username=your_user
spring.datasource.password=your_password

# JWT
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# Server
server.port=8081
```

### 3. Build and run

```bash
./mvnw spring-boot:run
```

The service will start on `http://localhost:8081` by default (adjust port as needed).

---

## Running with Docker

### Build the JAR first

```bash
./mvnw clean package -DskipTests
```

### Build the Docker image

```bash
docker build -t auth-service .
```

### Run the container

```bash
docker run -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/your_auth_db \
  -e SPRING_DATASOURCE_USERNAME=your_user \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  auth-service
```

> **Tip:** Use Docker Compose to run `auth-service`, `account-service`, and PostgreSQL together.

---

## Configuration

| Property | Description | Default |
|---|---|---|
| `server.port` | HTTP port | `8080` |
| `spring.datasource.url` | PostgreSQL JDBC URL | — |
| `spring.datasource.username` | DB username | — |
| `spring.datasource.password` | DB password | — |
| `jwt.secret` | JWT signing secret key | — |
| `jwt.expiration` | Token TTL in milliseconds | — |

---

## Project Structure

```
auth-service/
├── src/
│   ├── main/
│   │   ├── java/com/apratim/banking/auth_service/
│   │   │   ├── controller/      # REST controllers (AuthController)
│   │   │   ├── service/         # Business logic (AuthService)
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── model/           # JPA entities (User, etc.)
│   │   │   ├── dto/             # DTOs (LoginRequest, LoginResponse,
│   │   │   │                    #        RegisterRequest)
│   │   │   ├── config/          # Security configuration
│   │   │   └── security/        # JWT utilities & filters
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Unit & integration tests
├── Dockerfile
├── pom.xml
└── mvnw / mvnw.cmd
```

---

## API Endpoints

All auth endpoints are prefixed with `/auth`. These endpoints are public and do not require a JWT.

| Method | Path | Description | Request Body | Response |
|---|---|---|---|---|
| `POST` | `/auth/register` | Register a new user | `RegisterRequest` | Confirmation string |
| `POST` | `/auth/login` | Authenticate and receive a JWT | `LoginRequest` | `LoginResponse` (includes JWT) |

### Example: Register

```http
POST /auth/register
Content-Type: application/json

{
  "username": "john",
  "password": "secret123",
  "email": "john@example.com"
}
```

### Example: Login

```http
POST /auth/login
Content-Type: application/json

{
  "username": "john",
  "password": "secret123"
}
```

**Response:**
```json
{
  "token": "<JWT>",
  "expiresIn": 86400000
}
```

---

## Security

- Passwords are stored securely using Spring Security's `PasswordEncoder` (BCrypt).
- On successful login, a signed **JWT** is returned and should be stored client-side.
- The JWT must be sent as a `Bearer` token in the `Authorization` header for all requests to downstream protected services (e.g. `account-service`).
- Token signing uses a configurable secret key and expiry.

---

## Relation to Other Services

This service is the identity provider for the banking platform. The flow is:

```
Client → auth-service (/auth/login) → receives JWT
Client → account-service (/api/v1/accounts/...) → presents JWT in Authorization header
```

Both services must share the **same `jwt.secret`** for token verification to work across services.

---

## Building & Testing

```bash
# Run all tests
./mvnw test

# Build JAR (skip tests)
./mvnw clean package -DskipTests

# Build JAR with tests
./mvnw clean package
```

The packaged JAR will be at `target/auth-service-0.0.1-SNAPSHOT.jar`.

---

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'Add your feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request

---
