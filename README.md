# CivilReport Backend

A REST API for citizen civil issue reporting — built with Spring Boot 3.3 and Java 21.
Citizens submit reports, officers handle them, admins oversee the workflow.

Live: https://civil-report.onrender.com

API is fully explorable via Swagger UI — register, login, copy your JWT token, authorize, and test all endpoints directly in the browser. No Postman required.

**Note:** Hosted on Render free tier — may take 30–60 seconds to wake on first request.

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
| API Docs | Swagger / OpenAPI |

---

## Features

- **JWT Authentication** — register and login with token-based auth
- **Role-Based Access Control** — four roles: `USER`, `OFFICER`, `ADMIN`, `SUPER_ADMIN`
- **Full Report Lifecycle** — four-stage workflow: `SUBMITTED` → `ASSIGNED` → `IN_PROGRESS` → `RESOLVED`
- **Auto-Routing by Category** — reports automatically assigned to matching department on submission; only unmatched categories require manual admin assignment
- **Role-Scoped Report Access** — users see own submissions, officers see assigned reports, admins see all
- **Assignment Enforcement** — officers can only update status on reports explicitly assigned to them
- **Department Management** — standalone department reference data (CRUD)
- **Input Validation** — request validation using `@Valid` annotations throughout
- **Dedicated Request DTOs** — separate DTO classes per domain keeping entities clean
- **Secure Endpoints** — every route protected by role
- **Exception Handling** — global exception handler with consistent error response structure
- **Swagger UI** — fully interactive API documentation; authorize with JWT and test endpoints in browser

---

## Architecture

Every request passes through the following layers:

~~~
HTTP Request
    → JWT Authentication Filter
    → Security Context (role verification)
    → Controller (request handling + @Valid)
    → Service (business logic + assignment enforcement)
    → Repository (JPA)
    → PostgreSQL
~~~

---

## API Reference

### Auth
| Method | Endpoint | USER | OFFICER | ADMIN |
|---|---|---|---|---|
| POST | `/api/v1/auth/register` | ✅ | ✅ | ✅ |
| POST | `/api/v1/auth/login` | ✅ | ✅ | ✅ |

### Users
| Method | Endpoint | USER | OFFICER | ADMIN |
|---|---|---|---|---|
| GET | `/api/v1/users` | ❌ | ❌ | ✅ |
| GET | `/api/v1/users/me` | ✅ | ✅ | ✅ |

### Reports
| Method | Endpoint | USER | OFFICER | ADMIN |
|---|---|---|---|---|
| POST | `/api/v1/reports` | ✅ | ✅ | ✅ |
| GET | `/api/v1/reports` | ✅ | ✅ | ✅ |
| GET | `/api/v1/reports/{id}` | ✅ | ✅ | ✅ |
| GET | `/api/v1/reports/my-submissions` | ✅ | ✅ | ✅ |
| GET | `/api/v1/reports/my-reports` | ❌ | ✅ | ❌ |
| POST | `/api/v1/reports/{id}/assign` | ❌ | ❌ | ✅ |
| POST | `/api/v1/reports/{id}/status` | ❌ | ✅ | ❌ |

**Note:** `/my-submissions` returns only the currently authenticated user's own submitted reports — identity-based, not role-based. `/my-reports` returns reports assigned to the officer. Officers can only update status on reports assigned to them.

### Departments
| Method | Endpoint | USER | OFFICER | ADMIN |
|---|---|---|---|---|
| POST | `/api/v1/departments` | ❌ | ❌ | ✅ |
| GET | `/api/v1/departments` | ✅ | ✅ | ✅ |
| GET | `/api/v1/departments/{id}` | ✅ | ✅ | ✅ |

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
│   └── service/           # Includes assignment enforcement and auto-routing logic
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

All roles can submit reports and view their own submissions via `/my-submissions`.

| Role | Unique Permissions |
|---|---|
| `USER` | Track own report status through full lifecycle |
| `OFFICER` | View assigned reports, update status (assigned reports only) |
| `ADMIN` | View all users, assign reports to officers, manage departments |
| `SUPER_ADMIN` | Full system access — planned for Phase 2 |

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

**4. Explore with Swagger**

Visit `http://localhost:8080/swagger-ui/index.html` locally or `https://civil-report.onrender.com` for the live version. Register, login, copy your JWT token, click Authorize, and test all endpoints directly in the browser.

---

## Status

Live on Render. Current build covers JWT authentication, four-role RBAC, complete report lifecycle (SUBMITTED → ASSIGNED → IN_PROGRESS → RESOLVED), auto-routing by category, assignment enforcement, department management, input validation, and Swagger UI. Phase 2 planned: department-scoped roles, super admin features, and response DTOs.

---

## License

MIT
