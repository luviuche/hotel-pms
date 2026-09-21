# hotel-pms

[![build](https://github.com/luviuche/hotel-pms/actions/workflows/build.yml/badge.svg)](https://github.com/luviuche/hotel-pms/actions/workflows/build.yml)

A property management system (PMS) for hotel chains, built as a Spring Boot REST
API. It models the core of hotel operations: chains and their properties, rooms
and room types, guests and staff, reservations, extra amenities, payments and
invoices.

## Tech stack

- Java 25
- Spring Boot 4.1 (Spring Data JPA / Hibernate)
- PostgreSQL (in-memory H2 for the test suite)
- Flyway for schema migrations
- Maven Wrapper (`mvnw`)

## Getting started

### Prerequisites

- JDK 25
- A running PostgreSQL instance

Maven does not need to be installed: the project ships the wrapper.

### 1. Create the database

```sql
CREATE DATABASE hotel_pms;
```

### 2. Configure the connection

`src/main/resources/application.properties` reads environment variables and
falls back to sensible local defaults:

| Variable      | Default                                        |
|---------------|------------------------------------------------|
| `DB_URL`      | `jdbc:postgresql://localhost:5432/hotel_pms`   |
| `DB_USER`     | `postgres`                                     |
| `DB_PASSWORD` | `postgres`                                     |

### 3. Run it

```bash
./mvnw spring-boot:run
```

On startup Flyway creates the whole schema and seeds the catalogue tables.
Flyway owns the schema; Hibernate only validates that the entities match it
(`ddl-auto=validate`).

The API is then available at `http://localhost:8080/api`:

```bash
curl http://localhost:8080/api/roles
```

### 4. Run the tests

```bash
./mvnw test
```

The suite runs against in-memory H2 under the `test` profile, so PostgreSQL is
not required.

## Project layout

```
hotel-pms/
├── docs/
│   ├── api-reference.md               # REST API reference
│   └── data-model.md                  # ER diagram and constraints
├── src/main/java/io/github/luviuche/hotel/
│   ├── HotelPmsApplication.java
│   ├── entity/                        # JPA entities
│   ├── enums/                         # Controlled domains (statuses, types)
│   ├── repository/                    # Spring Data repositories
│   ├── service/                       # Business logic and validation
│   ├── controller/                    # REST endpoints (/api/...)
│   └── exception/                     # Global error handling
├── src/main/resources/
│   ├── application.properties         # PostgreSQL configuration
│   ├── application-test.properties    # H2 configuration (tests)
│   └── db/migration/                  # Flyway migrations
│       ├── V1__create_tables.sql
│       ├── V2__constraints_and_indexes.sql
│       └── V3__seed_data.sql
└── src/test/java/...
```

## Documentation

- [REST API reference](docs/api-reference.md) — endpoints, payloads, status codes.
- [Data model](docs/data-model.md) — ER diagram, relationships, constraints.

## Working on the schema

Never edit a migration that has already been applied. Add a new one
(`V4__...sql`) instead, and keep the entities, the migration and the diagram in
`docs/data-model.md` consistent with each other.
