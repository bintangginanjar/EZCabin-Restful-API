# EZCabin Airline Reservation API

A robust, secure, and simple airline reservation RESTful API built with Spring Boot.

## ✨ Features

-   Tax management (register, get, list, update, delete)
-   User registration and JWT authentication (Planned)
-   Manage airline, airport, flight, booking, etc. (Planned)
-   Role-based access control (User/Admin)

## 🛠️ Technologies Used

-   **Java 17**
-   **Spring Boot 3**
-   **Spring Security + JWT**: For secure authentication and authorization.
-   **H2 Database (Development)**: In-memory database for development and testing.
-   **PostgreSQL (Production)**: Relational database for production environments.
-   **Spring Data JPA**: For data persistence and ORM.

## 🚀 Getting Started

Instructions for setting up and running the project locally will be added here.

## 🧭 API Reference

> Base URL: `/api`

### 💰 Taxes

| Method | Endpoint         | Description          | Allowed User |
| ------ | ---------------- | -------------------- | ------------ |
| POST   | `/tax`           | Register new tax     | ADMIN        |
| GET    | `/tax/{taxId}`   | Get tax by ID        | ADMIN        |
| GET    | `/tax/list`      | List all taxes       | ADMIN        |
| PATCH  | `/tax/{taxId}`   | Update tax by ID     | ADMIN        |
| DELETE | `/tax/{taxId}`   | Delete tax by ID     | ADMIN        |

#### Register Tax

Endpoint : `POST /api/tax`

Request Header :

*   Authorization : "Bearer " + Token (mandatory)

Allowed User : ADMIN

Request Body:

```json
{
    "name": "VAT",
    "rate": 0.1
}
```

Response Body:

```json
{
    "status": true,
    "messages": "Tax registration success",
    "errors": null,
    "data": {
        "id": "1",
        "name": "VAT",
        "rate": 0.1,
        "createdAt": "2025-01-01T10:00:00Z",
        "updatedAt": "2025-01-01T10:00:00Z"
    }
}
```

#### Get Tax by ID

Endpoint : `GET /api/tax/{taxId}`

Request Header :

*   Authorization : "Bearer " + Token (mandatory)

Allowed User : ADMIN

Request Body: None

Response Body:

```json
{
    "status": true,
    "messages": "Tax fetching success",
    "errors": null,
    "data": {
        "id": "1",
        "name": "VAT",
        "rate": 0.1,
        "createdAt": "2025-01-01T10:00:00Z",
        "updatedAt": "2025-01-01T10:00:00Z"
    }
}
```

#### Update Tax

Endpoint : `PATCH /api/tax/{taxId}`

Request Header :

*   Authorization : "Bearer " + Token (mandatory)

Allowed User : ADMIN

Request Body:

```json
{
    "name": "Sales Tax",
    "rate": 0.08
}
```

Response Body:

```json
{
    "status": true,
    "messages": "Tax update success",
    "errors": null,
    "data": {
        "id": "1",
        "name": "Sales Tax",
        "rate": 0.08,
        "createdAt": "2025-01-01T10:00:00Z",
        "updatedAt": "2025-01-01T11:00:00Z"
    }
}
```

#### Delete Tax

Endpoint : `DELETE /api/tax/{taxId}`

Request Header :

*   Authorization : "Bearer " + Token (mandatory)

Allowed User : ADMIN

Request Body: None

Response Body:

```json
{
    "status": true,
    "messages": "Tax deletion success",
    "errors": null,
    "data": null
}
```
