# myproject - SpringBoot Application

## Overview
SpringBoot REST API application for bank2 microservice.

## Technology Stack
- **Java**: JDK 21
- **Spring Boot**: 3.5.9
- **Build Tool**: Maven
- **Database**: H2 (In-Memory)
- **API Documentation**: Swagger/OpenAPI
- **Testing**: JUnit 5, Mockito
- **Code Coverage**: JaCoCo

## Prerequisites
- JDK 21 or higher
- Maven 3.8+

## Project Structure
```
code/
├── src/
│   ├── main/
│   │   ├── java/com/myproject/
│   │   │   ├── controllers/       # REST controllers
│   │   │   ├── models/
│   │   │   │   ├── dtos/         # Data Transfer Objects
│   │   │   │   ├── entities/     # JPA entities
│   │   │   │   └── repositories/ # Data repositories
│   │   │   ├── services/         # Business logic
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── exceptions/       # Exception handlers
│   │   │   └── MyprojectApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                     # Test files
└── pom.xml
```

## Building the Application

### Clean and Build
```bash
cd code
mvn clean install
```

### Run Tests
```bash
mvn test
```

### Generate Coverage Report
```bash
mvn jacoco:report
```
Coverage report will be available at: `target/site/jacoco/index.html`

## Running the Application

### Using Maven
```bash
mvn spring-boot:run
```

### Using Java
```bash
java -jar target/myproject-1.0.0.jar
```

## Application Endpoints

### Base URL
```
http://localhost:8080/api
```

### Health Check
```
GET /api/v1/health
```

### API Documentation
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/api-docs

### H2 Console
```
http://localhost:8080/api/h2-console
```
- **JDBC URL**: jdbc:h2:mem:myprojectdb
- **Username**: sa
- **Password**: (leave empty)

## Configuration

### Application Properties
Key configurations in `application.properties`:
- Server port: 8080
- Context path: /api
- H2 database: In-memory
- CORS: Enabled for http://localhost:4200

### CORS Configuration
CORS is configured to allow:
- Origin: http://localhost:4200 (Angular dev server)
- All headers
- All HTTP methods (GET, POST, PUT, DELETE, PATCH)
- Credentials

## Testing

### Unit Tests
Unit tests are located in `src/test/java/`

### Run All Tests
```bash
mvn test
```

### Test Reports
Test reports are generated in:
- Surefire reports: `target/surefire-reports/`
- JaCoCo coverage: `target/site/jacoco/`

## CI/CD

### GitHub Actions
The project includes a GitHub Actions workflow (`.github/workflows/build.yml`) that:
- Builds the application
- Runs all tests
- Generates coverage reports
- Uploads test results and coverage as artifacts

### Trigger Workflow
Go to Actions tab in GitHub and manually trigger the "Build and Test SpringBoot Application" workflow.

## Development

### Adding New Endpoints
1. Create DTO classes in `models/dtos/`
2. Create controller in `controllers/`
3. Create service interface in `services/interfaces/`
4. Implement service in `services/impl/`
5. Create repository in `models/repositories/`
6. Add unit tests

### Code Quality
- Follow Spring Boot best practices
- Write unit tests for all business logic
- Maintain code coverage above 80%
- Use Lombok to reduce boilerplate
- Follow RESTful API conventions

## Support
For issues or questions, please refer to the project documentation or contact the development team.
