# Task API

A small, production-style REST API built with **Java 21** and **Spring Boot 3**.
It manages tasks (create, read, update, delete, filter, paginate) and is meant as a
readable reference for learning backend development.

## Run it

Requirements: JDK 21+ and Maven 3.9+.

```bash
mvn spring-boot:run     # start the API on http://localhost:8080
mvn test                # run the tests
```

- Swagger UI (try the API in your browser): http://localhost:8080/swagger-ui.html
- Health check: http://localhost:8080/actuator/health

Data lives in an in-memory H2 database and resets on every restart.

## Endpoints

| Method | Path                  | Description                                  | Success |
|--------|-----------------------|----------------------------------------------|---------|
| POST   | `/api/v1/tasks`       | Create a task                                | 201     |
| GET    | `/api/v1/tasks`       | List tasks (`?status=DONE&page=0&size=20`)   | 200     |
| GET    | `/api/v1/tasks/{id}`  | Get one task                                 | 200     |
| PUT    | `/api/v1/tasks/{id}`  | Replace a task                               | 200     |
| DELETE | `/api/v1/tasks/{id}`  | Delete a task                                | 204     |

```bash
curl -X POST localhost:8080/api/v1/tasks \
  -H 'Content-Type: application/json' \
  -d '{"title": "Learn Spring Boot", "description": "Read the README"}'

curl 'localhost:8080/api/v1/tasks?status=TODO'
```

Errors use the standard problem-details format:

```json
{ "status": 400, "detail": "Validation failed", "errors": { "title": "must not be blank" } }
```

## Project structure

```
src/main/java/com/example/taskapi
├── controller/   HTTP layer: routes, status codes. No business logic.
├── service/      Business logic and transactions.
├── repository/   Database access (Spring Data JPA interface).
├── model/        JPA entity (Task) and enum (TaskStatus).
├── dto/          Request/response shapes exposed to clients.
└── exception/    Custom exceptions and the global error handler.

src/main/resources
├── application.yml
└── db/migration/ Flyway SQL migrations (the source of truth for the schema)
```

A request flows **Controller → Service → Repository → Database**, and each layer
only talks to the one below it.

## Design decisions worth learning from

- **DTOs, not entities, in the API.** Clients never see or control your database model.
- **Separate create/update requests.** Each endpoint accepts exactly what it needs.
- **Validation at the edge.** `@Valid` rejects bad input before it reaches your logic.
- **Flyway owns the schema** (`ddl-auto: none`). Schema changes are versioned SQL files.
- **Constructor injection** everywhere: dependencies are explicit and easy to test.
- **Bounded pagination** (max 100) so one request can't pull the whole table.
- **Unexpected errors are logged, not leaked.** Clients get a generic 500 message.
- **`open-in-view: false`** avoids hidden lazy-loading queries in the web layer.

## Next steps when you outgrow the demo

1. Swap H2 for PostgreSQL: add the driver, set `spring.datasource.*`, keep the same migrations.
2. Add authentication with Spring Security.
3. Add a `Dockerfile` and CI that runs `mvn verify`.
