# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

ECP-Catalog-Service is a Spring Boot 3.5 (Java 21) REST service for managing a product catalog, backed by PostgreSQL via Spring Data JPA. Currently it exposes category creation only.

## Commands

Use the Gradle wrapper (`./gradlew` on bash, `.\gradlew.bat` or `gradlew` in PowerShell) — do not rely on a system-installed Gradle.

- Build: `./gradlew build`
- Run the app: `./gradlew bootRun`
- Run all tests: `./gradlew test`
- Run a single test class: `./gradlew test --tests "com.proxy.ecpcatalogservice.service.CategoryServiceImplTest"`
- Run a single test method: `./gradlew test --tests "com.proxy.ecpcatalogservice.controller.CategoryControllerTest.methodName"`

Tests use JUnit 5 (`useJUnitPlatform()`).

## Configuration

`src/main/resources/application.yaml` requires three environment variables to start the app: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (PostgreSQL connection). `spring.jpa.hibernate.ddl-auto` is `update`, so entity changes auto-migrate the schema on startup — there are no migration scripts (e.g. Flyway/Liquibase) in this repo. Actuator exposes only `health` and `info`.

## Architecture

Standard layered Spring Boot structure under `src/main/java/com/proxy/ecpcatalogservice/`:

- `controller/` — `@RestController` classes mapping HTTP endpoints (e.g. `CategoryController` under `/api/v1/categories`) to service calls, plus `ExceptionHandlingAdvice`, a `@ControllerAdvice` that turns `MethodArgumentNotValidException` (bean validation failures) into a structured `ValidationErrorResponse` with per-field error messages.
- `dto/` — request/response records used at the controller boundary (e.g. `CreateCategoryRequest`, `CreateCategoryResponse`). Entities are never returned directly from controllers.
- `mapper/` — plain `@Component` classes that hand-convert between DTOs and entities (no MapStruct/ModelMapper). Follow this pattern (`toX` methods) for new entities rather than introducing a mapping library.
- `service/` — an interface (e.g. `CategoryService`) plus a package-private `*Impl` implementation. Implementations are intentionally not public — keep new services consistent with this visibility pattern.
- `model/` — JPA `@Entity` classes (e.g. `Category`, table `categories`), using `UUID` primary keys generated via `GenerationType.UUID`.
- `repository/` — Spring Data JPA repositories.

Tests mirror the main package structure under `src/test/java/...` (e.g. `CategoryControllerTest`, `CategoryServiceImplTest`).
