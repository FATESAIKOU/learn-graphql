# Learn GraphQL

A Spring Boot application with GraphQL implementation, containerized with Docker Compose and PostgreSQL.

## Features

- **Spring Boot 3.3.0** with Java 21
- **GraphQL** API with Spring GraphQL starter
- **PostgreSQL** database with JPA/Hibernate
- **Docker Compose** setup for development
- **Complete Todo domain model** for task management
- **GraphiQL** interface for interactive queries
- **Health check endpoints**
- **Sample data seeding**
- **Comprehensive test suite** with Testcontainers

## GraphQL Schema

The application provides a complete GraphQL schema for managing Todo tasks:

### Types

```graphql
type Todo {
    id: ID!
    title: String!
    description: String
    completed: Boolean!
    createdAt: String!
    updatedAt: String!
}
```

### Queries

```graphql
type Query {
    # Get all todos
    todos: [Todo!]!
    
    # Get todo by ID
    todo(id: ID!): Todo
    
    # Get todos by completion status
    todosByStatus(completed: Boolean!): [Todo!]!
    
    # Test query
    hello: String
}
```

### Mutations

```graphql
type Mutation {
    # Create a new todo
    createTodo(input: CreateTodoInput!): Todo!
    
    # Update an existing todo
    updateTodo(id: ID!, input: UpdateTodoInput!): Todo
    
    # Toggle todo completion status
    toggleTodo(id: ID!): Todo
    
    # Delete a todo
    deleteTodo(id: ID!): Boolean!
}
```

### Input Types

```graphql
input CreateTodoInput {
    title: String!
    description: String
}

input UpdateTodoInput {
    title: String
    description: String
    completed: Boolean
}
```

## Running the Application

### Using Docker Compose (Recommended)

1. **Build and start all services:**
   ```bash
   docker-compose up --build
   ```

2. **Access the application:**
   - GraphQL Endpoint: `http://localhost:8080/graphql`
   - GraphiQL Interface: `http://localhost:8080/graphiql`
   - Health Check: `http://localhost:8080/health`
   - API Info: `http://localhost:8080/`

3. **Stop the services:**
   ```bash
   docker-compose down
   ```

### Running Locally

1. **Start PostgreSQL:**
   ```bash
   docker run --name postgres-graphql -e POSTGRES_PASSWORD=password -e POSTGRES_DB=learn_graphql -p 5432:5432 -d postgres:15
   ```

2. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

## Sample GraphQL Queries

### Basic Queries

**Get all todos:**
```graphql
query {
  todos {
    id
    title
    description
    completed
    createdAt
    updatedAt
  }
}
```

**Get a specific todo:**
```graphql
query {
  todo(id: "1") {
    id
    title
    description
    completed
    createdAt
    updatedAt
  }
}
```

**Get completed todos:**
```graphql
query {
  todosByStatus(completed: true) {
    id
    title
    completed
  }
}
```

**Get pending todos:**
```graphql
query {
  todosByStatus(completed: false) {
    id
    title
    description
    createdAt
  }
}
```

### Mutations

**Create a new todo:**
```graphql
mutation {
  createTodo(input: {
    title: "Learn GraphQL"
    description: "Complete the GraphQL tutorial and build a todo app"
  }) {
    id
    title
    description
    completed
    createdAt
  }
}
```

**Update a todo:**
```graphql
mutation {
  updateTodo(id: "1", input: {
    title: "Learn GraphQL - Updated"
    completed: true
  }) {
    id
    title
    description
    completed
    updatedAt
  }
}
```

**Mark todo as completed:**
```graphql
mutation {
  updateTodo(id: "1", input: {
    completed: true
  }) {
    id
    title
    completed
    updatedAt
  }
}
```

**Toggle todo completion status:**
```graphql
mutation {
  toggleTodo(id: "1") {
    id
    title
    completed
    updatedAt
  }
}
```

**Delete a todo:**
```graphql
mutation {
  deleteTodo(id: "1")
}
```

## Sample Data

The application automatically seeds the database with sample todos including:

- **學習 GraphQL** (Pending) - 完成 GraphQL 教學課程和實作練習
- **完成專案** (Completed) - 完成 Spring Boot + GraphQL TODO 應用程式
- **買菜** (Pending) - 去超市買這週的食材
- **運動** (Pending) - 每天至少運動 30 分鐘
- **讀書** (Pending) - 閱讀技術書籍增進知識

## Architecture

- **Entity:** `Todo` with JPA annotations for task management
- **Repository:** Spring Data JPA repository for data access
- **Service:** Business logic layer for Todo operations
- **Resolver:** GraphQL resolver handling queries and mutations
- **DTOs:** Input types for GraphQL mutations (CreateTodoInput, UpdateTodoInput)
- **Data Seeding:** Automatic sample data loading on startup
- **Testing:** Comprehensive test suite with Testcontainers + PostgreSQL

## Technology Stack

- **Java 21** with Spring Boot 3.3.0
- **Spring GraphQL** for GraphQL implementation
- **Spring Data JPA** for database operations
- **PostgreSQL** as the database
- **Docker & Docker Compose** for containerization
- **Gradle** for build management

## Development

### Building the Project

```bash
./gradlew clean build
```

### Running Tests

```bash
./gradlew test
```

### Code Formatting

```bash
./gradlew spotlessApply
```

## Docker Configuration

The project includes:

- **Multi-stage Dockerfile** for optimized builds
- **Docker Compose** with app and PostgreSQL services
- **Health checks** for service monitoring
- **Volume mounts** for database persistence
- **Environment configuration** for different profiles

## API Endpoints

- `POST /graphql` - GraphQL endpoint for queries and mutations
- `GET /graphiql` - GraphiQL interface for interactive queries
- `GET /health` - Application health status
- `GET /` - Basic API information

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests and formatting
5. Submit a pull request

## License

This project is for educational purposes.
