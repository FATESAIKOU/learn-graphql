# Learn GraphQL Project

A Spring Boot + GraphQL learning project with automated code formatting and CI/CD.

## Quick Start

### Setup Development Environment

1. **Install Git Hooks** (for automatic code formatting):
   ```bash
   ./install-hooks.sh
   ```

2. **Build and Run**:
   ```bash
   ./gradlew bootRun
   ```

3. **Run Tests**:
   ```bash
   ./gradlew test
   ```

## Code Formatting

This project uses **Spotless** with Google Java Format for consistent code style.

### Available Commands

- **Check formatting**: `./gradlew spotlessCheck`
- **Apply formatting**: `./gradlew spotlessApply`

### Automatic Formatting

- **Git Pre-commit Hook**: Code is automatically formatted before each commit
- **CI Check**: GitHub Actions verifies code formatting on every push

### Manual Installation

If you prefer to install the git hook manually:

```bash
# Copy the hook
cp .githooks/pre-commit .git/hooks/pre-commit

# Make it executable
chmod +x .git/hooks/pre-commit
```

## Project Structure

```
src/
├── main/
│   ├── java/com/learngraphql/
│   │   └── LearnGraphQlApplication.java
│   └── resources/
│       ├── application.properties
│       └── graphql/schema.graphqls
└── test/
    └── java/com/learngraphql/
        └── LearnGraphQlApplicationTests.java
```

## Technology Stack

- **Java 21**
- **Spring Boot 3.3.0**
- **GraphQL Java**
- **H2 Database** (development)
- **PostgreSQL** (production)
- **Gradle** (build tool)
- **Spotless** (code formatting)
- **GitHub Actions** (CI/CD)

## Development Workflow

1. Make your code changes
2. Commit (code will be auto-formatted by pre-commit hook)
3. Push to GitHub (CI will run tests and formatting checks)

## CI/CD

The project uses GitHub Actions for continuous integration:

- ✅ Code formatting verification (`spotlessCheck`)
- ✅ Unit tests (`test`)
- ✅ Build verification (`build`)

## Dependencies

All project dependencies are managed in `build.gradle`:

- Spring Boot Starters (Web, GraphQL, JPA, Validation)
- Database drivers (H2, PostgreSQL)
- Testing frameworks (JUnit 5, Spring Test)
- Development tools (DevTools, Spotless)
