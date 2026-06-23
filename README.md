# CivilReport Backend

A REST API for citizen civil issue reporting — built with Spring Boot 3.3 and Java 21.
Citizens submit reports, officers handle them, admins oversee the workflow.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3 |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Utilities | Lombok |

---

## Features

- **JWT Authentication** — register and login with token-based auth
- **Role-Based Access Control** — three roles: `USER`, `OFFICER`, `ADMIN`
- **Report Workflow** — citizens submit reports, admins assign them to officers, officers track their queue
- **Department Management** — standalone department reference data (CRUD)
- **Input Validation** — request validation using `@Valid` annotations throughout
- **Dedicated Request DTOs** — separate DTO classes per domain (user, report) keeping entities clean
- **Secure Endpoints** — every route protected by role; no endpoint is accidentally public
- **Exception Handling** — global exception handler with consistent error response structure
- **Full Report Lifecycle** — four-stage workflow: `SUBMITTED` → `ASSIGNED` → `IN_PROGRESS` → `RESOLVED`
- **Status Updates** — officers update report progress; citizens track resolution in real time

---

## Architecture

Every request passes through the following layers:

~~~
HTTP Request
    → JWT Authentication Filter
    → Security Context (role verification)
    → Controller (request handling + @Valid)
    → Service (business logic)
    → Repository (JPA)
    → PostgreSQL
~~~

---

## API Reference

### Auth
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/auth/register` | Public |
| POST | `/api/v1/auth/login` | Public |

### Users
| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/v1/users` | Admin only |

### Reports
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/reports` | Authenticated |
| GET | `/api/v1/reports/{id}` | Authenticated |
| POST | `/api/v1/reports/{id}/assign` | Admin only |
| GET | `/api/v1/reports/my-reports` | Officer only |
| POST | `/api/v1/reports/{id}/status` | Officer only |
| GET | `/api/v1/reports/my-submissions` | USER |

### Departments
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/departments` | Admin only |
| GET | `/api/v1/departments` | Authenticated |
| GET | `/api/v1/departments/{id}` | Authenticated |

---

## Project Structure

~~~
src/main/java/civil/
├── auth/
│   └── controller/        # Auth endpoints only; delegates to user service
├── common/
│   ├── Dto/               # Shared DTOs — ErrorResponse
│   └── exception/         # GlobalExceptionHandler — consistent error responses
├── config/
│   ├── JwtAuthenticationFilter   # Intercepts and validates JWT on every request
│   ├── JwtUtil                   # Token generation and validation
│   └── SecurityConfig            # Route protection and role definitions
├── department/
│   ├── controller/
│   ├── entity/            # Department entity — no DTO, entity used directly
│   ├── repository/
│   └── service/
├── report/
│   ├── controller/
│   ├── dto/               # ReportRequestDto, AssignReportRequest
│   ├── entity/            # Report, ReportCategory (enum), ReportStatus (enum)
│   ├── repository/
│   └── service/
├── user/
│   ├── controller/
│   ├── dto/               # UserRequestDto
│   ├── entity/            # User, UserRole (enum)
│   ├── repository/
│   └── service/
└── CivilReportApplication.java
~~~

---

## Role Overview

| Role | Permissions |
|---|---|
| `USER` | Submit reports, view own submissions, track status |
| `OFFICER` | View assigned reports, update report status |
| `ADMIN` | View all users, assign reports to officers, manage departments |

---

## Getting Started

### Prerequisites
- Java 21
- Maven
- PostgreSQL

### Setup

**1. Create the database**
```sql
CREATE DATABASE civil_report;
```

**2. Configure credentials**

Create `src/main/resources/application-dev.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/civil_report
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

jwt.secret=YOUR_JWT_SECRET_MIN_32_CHARS
```

**Note:** Never commit real credentials. `application-dev.properties` is gitignored.

**3. Run the application**
```bash
./mvnw spring-boot:run
```

API available at `http://localhost:8080`.

**4. Test with Postman**

Call `/api/v1/auth/login` first to get a JWT token, then add it to protected requests:
```
Authorization: Bearer <your_token>
```

---

## Status

Active development. Current build covers JWT authentication, three-role RBAC, complete report lifecycle (SUBMITTED → ASSIGNED → IN_PROGRESS → RESOLVED), department management, and input validation. Frontend and response DTOs planned.

---

## License

MIT
