# Multi-Factor Authentication (MFA) Proof of Concept Application

## Overview

This repository contains a Multi-Factor Authentication (MFA) proof of concept (PoC) application built using Spring Boot. The application demonstrates how to implement MFA using Google Authenticator. It includes user registration, login, QR code generation for MFA setup, and token verification.

## Project Structure

```plaintext
Multi factor Authentication POC/
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── org
│   │   │       └── example
│   │   │           └── mfademo
│   │   │               ├── config
│   │   │               │   └── SecurityConfig.java
│   │   │               ├── controller
│   │   │               │   └── AuthController.java
│   │   │               ├── dto
│   │   │               │   ├── LoginRequest.java
│   │   │               │   ├── MfaTokenVerificationRequest.java
│   │   │               │   └── SignUpRequest.java
│   │   │               ├── MfaDemoApplication.java
│   │   │               ├── model
│   │   │               │   └── User.java
│   │   │               ├── repository
│   │   │               │   └── UserRepository.java
│   │   │               └── service
│   │   │                   ├── AuthService.java
│   │   │                   └── QrCodeService.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
└── target
    ├── classes
    │   ├── application.properties
    │   └── org
    │       └── example
    │           └── mfademo
    │               ├── config
    │               │   └── SecurityConfig.class
    │               ├── controller
    │               │   └── AuthController.class
    │               ├── dto
    │               │   ├── LoginRequest.class
    │               │   ├── MfaTokenVerificationRequest.class
    │               │   └── SignUpRequest.class
    │               ├── MfaDemoApplication.class
    │               ├── model
    │               │   └── User.class
    │               ├── repository
    │               │   └── UserRepository.class
    │               └── service
    │                   ├── AuthService.class
    │                   └── QrCodeService.class
    ├── generated-sources
    │   └── annotations
    └── maven-status
        └── maven-compiler-plugin
            └── compile
                └── default-compile
                    ├── createdFiles.lst
                    └── inputFiles.lst
```

## Key Components

### 1. Configuration

- **SecurityConfig.java**: Configures Spring Security to permit all requests to `/api/auth/**` and requires authentication for other endpoints. It also defines a `PasswordEncoder` bean.

### 2. Controllers

- **AuthController.java**: Handles authentication-related endpoints such as user registration (`/signup`), user login (`/signin`), QR code generation (`/generate-qr-code`), and MFA token verification (`/verify-token`).

### 3. Data Transfer Objects (DTOs)

- **LoginRequest.java**: Represents the login request payload.
- **MfaTokenVerificationRequest.java**: Represents the MFA token verification request payload.
- **SignUpRequest.java**: Represents the user registration payload.

### 4. Models

- **User.java**: Represents the user entity with fields for ID, username, password, and MFA secret.

### 5. Repositories

- **UserRepository.java**: Provides CRUD operations for the `User` entity.

### 6. Services

- **AuthService.java**: Contains business logic for user registration, login, QR code URL generation, and MFA token verification.
- **QrCodeService.java**: Generates QR codes for MFA setup.

### 7. Application Entry Point

- **MfaDemoApplication.java**: The main class that starts the Spring Boot application.

## Dependencies

The `pom.xml` file includes the necessary dependencies for Spring Boot, Spring Security, JPA, MySQL, H2, Google Authenticator, and QR code generation.

## Configuration

### Create application.properties in the provided format

```properties
#spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=
spring.jpa.show-sql=
spring.jpa.properties.hibernate.format_sql=
logging.level.org.springframework.jdbc.datasource=
spring.datasource.url=
spring.datasource.driverClassName=
spring.datasource.username=
spring.datasource.password=
spring.jpa.database-platform=
```

## Running the Application

1. Clone the repository.
2. Update the `application.properties` file with your database credentials.
3. Build and run the application using Maven:

   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

## API Endpoints

### 1. User Registration

**Endpoint**: `/api/auth/signup`

**Method**: POST

**Payload**:
```json
{
  "username": "user2",
  "password": "password123"
}
```

**Curl Command**:
```sh
curl --location 'http://localhost:8080/api/auth/signup' \
--header 'Content-Type: application/json' \
--data '{
        "username": "user2",
        "password": "password123"
      }'
```

### 2. User Login

**Endpoint**: `/api/auth/signin`

**Method**: POST

**Payload**:
```json
{
  "username": "user1",
  "password": "password123"
}
```

**Curl Command**:
```sh
curl --location 'http://localhost:8080/api/auth/signin' \
--header 'Content-Type: application/json' \
--data '{
        "username": "user1",
        "password": "password123"
      }'
```

### 3. Generate QR Code for MFA Setup

**Endpoint**: `/api/auth/generate-qr-code`

**Method**: GET

**Query Parameter**:
```sh
username=user2
```

**Curl Command**:
```sh
curl --location 'http://localhost:8080/api/auth/generate-qr-code?username=user2'
```

### 4. Verify MFA Token

**Endpoint**: `/api/auth/verify-token`

**Method**: POST

**Payload**:
```json
{
  "username": "user2",
  "mfaToken": "863769"
}
```

**Curl Command**:
```sh
curl --location 'http://localhost:8080/api/auth/verify-token' \
--header 'Content-Type: application/json' \
--data '{"username": "user2", "mfaToken": "863769"}'
```

## Conclusion

This repository serves as a PoC for implementing MFA in a Spring Boot application. It covers essential aspects such as user registration, login, MFA setup using QR codes, and token verification, providing a robust foundation for securing applications with MFA.