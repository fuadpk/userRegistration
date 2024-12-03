# User Registration Task

## Overview
This project is a **User Registration API** that includes the following features:
- **User Registration**: Allows new users to sign up.
- **Authentication**: Secure login with JWT tokens.
- **User CRUD Operations**: Create, retrieve, update, and delete user details.
- **Endpoint Security**: Protects private endpoints.
- **Test Cases**: Unit and integration tests.
- **Dockerized Application**: Deployable as a container.

---

## Architecture
The project uses the **Spring Boot** framework with the following components:
- **Hibernate**: For Object-Relational Mapping (ORM).
- **H2 Database**: In-memory database with Liquibase for schema migrations.

---

## Design Choices
- **Modularity**: Separate configuration files for cleaner code organization.
- **Constants Management**: A single class for all constants.
- **Generic Interfaces**: For reusable CRUD services and controllers.
- **Database Schema Reusability**: Utilized `@MappedSuperclass` for reusable attributes.
- **Global Exception Handling**: Unified and consistent error management.
- **Caching**: Faster data retrieval with cache implementation.
- **Error Responses**: Structured error responses.

---

## Security
- **JWT-Based Authentication**: Secure token-based login.
- **Schema Abstraction**: Data Transfer Objects (DTOs) to hide internal schema.
- **Password Security**: Passwords stored using encoding techniques.
- **UUIDs**: Database primary keys hidden by exposing only UUIDs.

---

## Performance Enhancements
- **Database Connection Pooling**: Reduces connection establishment overhead.
- **GZIP Compression**: Minimizes network data transfer size over network.
- **User Caching**: For faster response times.

---

## Project Object Model (POM)
### Dependencies:
- **Java**: 17
- **Spring Boot**: 3.4.0
- Other dependencies:
    - `spring-boot-starter-web`
    - `spring-boot-starter-security`
    - `spring-boot-starter-validation`
    - `jjwt-api`, `jjwt-impl`, `jjwt-jackson`
    - `spring-boot-starter-cache`
    - `liquibase-core`
    - `spring-boot-starter-data-jpa`
    - `H2 Database`
    - `spring-boot-starter-test`
    - `spring-security-test`
    - `mockito-inline`

---

## Steps to Run
1. Build the Docker image.
    ```bash
   docker build -t mykare-task-application .
2. Run the container with the following command:
   ```bash
   docker run -d -p 8080:8080 mykare-task-application

## Steps to Load From Docker
1. Load the Docker image.
    ```bash
   docker load -i mykare-task-application.tar .
2. Run the container with the following command:
   ```bash
   docker run -d -p 8080:8080 mykare-task-application

## API Documentation

### Public API

| **Endpoint**         | **Method** | **Description**                                       | **Request Body**                                                                                                  | **Response**                                                                                                     |
|----------------------|------------|-------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| `/public/welcome`    | `GET`      | Returns a welcome message.                           | None                                                                                                              | `"Welcome to the Mykare Task Application"`                                                                        |
| `/public/signUp`     | `POST`     | Registers a new user.                                | `{ "firstName": "string", "lastName": "string", "gender": "MALE/FEMALE", "email": "string", "password": "string" }` | `{ "uuid": "uuid", "firstName": "string", "lastName": "string", "gender": "MALE/FEMALE", "email": "string", "password": "string" }`    |
| `/public/signIn`     | `POST`     | Authenticates the user and sets a secure JWT token.  | `{ "email": "string", "password": "string" }`                                                                     | JWT token in secure cookie.                                                                                      |
| `/public/signOut`    | `POST`     | Invalidates the JWT token by setting an expired cookie. | None                                                                                                              | None                                                                                      |

---

### Private API

| **Endpoint**             | **Method** | **Description**                              | **Request Body**                                                                                                  | **Response**                                                                                                     |
|--------------------------|------------|----------------------------------------------|-------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| `/api/users/post`        | `POST`     | Creates a new user.                          | `{ "firstName": "string", "lastName": "string", "gender": "MALE/FEMALE", "email": "string", "password": "string" }` | `{ "uuid": "uuid", "firstName": "string", "lastName": "string", "gender": "MALE/FEMALE", "email": "string", "password": "string" }`    |
| `/api/users/get/{uuid}`  | `GET`      | Retrieves user details by UUID.              | None                                                                                                              | `{ "uuid": "uuid", "firstName": "string", "lastName": "string", "gender": "MALE/FEMALE", "email": "string", "password": "string" }`    |
| `/api/users/update/{uuid}` | `PUT`     | Updates an existing user's details.          | `{ "firstName": "string", "lastName": "string", "gender": "MALE/FEMALE", "email": "string", "password": "string" }` | `{ "uuid": "uuid", "firstName": "string", "lastName": "string", "gender": "MALE/FEMALE", "email": "string", "password": "string" }`    |
| `/api/users/delete/{uuid}` | `DELETE`  | Deletes a user by UUID.                      | None                                                                                                              | None                                                                     |
| `/api/users/getAll`      | `GET`      | Fetches a paginated list of all users.        | Query Parameters: `page` (default: 0), `size` (default: 10)                                                       | List of users.                                                                               |
