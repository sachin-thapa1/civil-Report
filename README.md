Here is your final README, updated to match your actual code:

```markdown
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
- **Secure Endpoints** — each route protected by role; no endpoint is accidentally public

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

### Departments
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/departments` | Admin only |
| GET | `/api/v1/departments` | Authenticated |
| GET | `/api/v1/departments/{id}` | Authenticated |

---

## Project Structure

```
src/main/java/civil/
├── auth/
│   └── controller/        # Register, login
├── common/
│   ├── dto/               # Shared DTOs (ErrorResponse)
│   └── exception/         # GlobalExceptionHandler
├── config/                # SecurityConfig, JwtUtil, JwtAuthenticationFilter
├── department/
│   ├── controller/
│   ├── entity/
│   ├── repository/
│   └── service/
├── report/
│   ├── controller/
│   ├── dto/               # ReportRequestDto, AssignReportRequest
│   ├── entity/            # Report, ReportCategory, ReportStatus
│   ├── repository/
│   └── service/
├── user/
│   ├── controller/
│   ├── dto/               # UserRequestDto
│   ├── entity/            # User, UserRole
│   ├── repository/
│   └── service/
└── CivilReportApplication.java
```

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

Copy `application-example.properties` to `application-dev.properties`:
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

> `application-dev.properties` is gitignored and never committed.

**3. Run the application**
```bash
./mvnw spring-boot:run
```

API available at `http://localhost:8080`.

**4. Test with Postman**

For protected routes, first call `/api/v1/auth/login`, copy the returned JWT, then add it to your request headers:
```
Authorization: Bearer <your_token>
```

---

## Role Overview

| Role | Permissions |
|---|---|
| `USER` | Submit reports, view own report by ID |
| `OFFICER` | View reports assigned to them |
| `ADMIN` | View all users, assign reports, manage departments |

---

## License

MIT
```

**Save this as `README.md` and push to GitHub.**

Your project is complete and CV-ready. Good work.
