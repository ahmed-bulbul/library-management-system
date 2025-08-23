## API Documentation

### Base URL
- Development: `http://localhost:8080/api/v1`
- Production: `https://your-domain.com/api/v1`
- Swagger: `http://localhost:8080/swagger-ui/index.html`

### Endpoints

#### 1. Register a new borrower
```
POST /borrowers
Content-Type: application/json

Request Body:
{
  "name": "John Doe",
  "email": "john.doe@email.com"
}

Response (201 Created):
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@email.com"
}
```

#### 2. Register a new book
```
POST /books
Content-Type: application/json

Request Body:
{
  "isbn": "978-0-123456-47-2",
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald"
}

Response (201 Created):
{
  "id": 1,
  "isbn": "978-0-123456-47-2",
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "available": true
}
```

#### 3. Get all books
```
GET /books

Response (200 OK):
[
  {
    "id": 1,
    "isbn": "978-0-123456-47-2",
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "available": true
  },
  {
    "id": 2,
    "isbn": "978-0-123456-48-9",
    "title": "To Kill a Mockingbird",
    "author": "Harper Lee",
    "available": false
  }
]
```

#### 4. Borrow a book
```
POST /books/borrow
Content-Type: application/json

Request Body:
{
  "borrowerId": 1,
  "bookId": 1
}

Response (200 OK):
{
  "message": "Book borrowed successfully",
  "borrowRecordId": "1",
  "borrowedAt": "2024-01-15T10:30:00"
}
```

#### 5. Return a book
```
POST /books/{bookId}/return?borrowerId={borrowerId}

Response (200 OK):
{
  "message": "Book returned successfully",
  "borrowRecordId": "1",
  "returnedAt": "2024-01-20T14:30:00"
}
```

### Error Responses

#### Validation Error (400 Bad Request)
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "fieldErrors": {
    "name": "Name is required",
    "email": "Email should be valid"
  }
}
```

#### Resource Not Found (404 Not Found)
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Book Not Found",
  "message": "Book with ID 1 not found"
}
```

#### Business Logic Error (409 Conflict)
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 409,
  "error": "Book Already Borrowed",
  "message": "Book with ID 1 is already borrowed"
}
```

## Database Choice Justification

**PostgreSQL** is chosen as the primary database for production for the following reasons:

1. **ACID Compliance**: Ensures data integrity for library transactions
2. **Concurrent Access**: Handles multiple users borrowing/returning books simultaneously
3. **Scalability**: Supports horizontal scaling for growing library systems
4. **JSON Support**: Flexible for future feature additions
5. **Robust Ecosystem**: Excellent tooling and community support

**H2 Database** is used for development and testing due to:
- Fast setup and teardown for tests
- In-memory operation for quick development cycles
- No external dependencies required

## 12-Factor App Compliance

1. **Codebase**: Single codebase tracked in Git
2. **Dependencies**: All dependencies declared in pom.xml
3. **Config**: Environment-specific configuration via profiles
4. **Backing Services**: Database treated as attached resource
5. **Build/Release/Run**: Separate build and run stages via Docker
6. **Processes**: Stateless application design
7. **Port Binding**: Self-contained service exports HTTP
8. **Concurrency**: Scale out via process model (Kubernetes replicas)
9. **Disposability**: Fast startup and graceful shutdown
10. **Dev/Prod Parity**: Same technologies across environments
11. **Logs**: Logs treated as event streams via Spring Boot Actuator
12. **Admin Processes**: Database migrations via Flyway

## Running the Application

### Development
```bash
mvn spring-boot:run
```

### Production with Docker & Kubernetes
```bash
1. ./mvnw clean package -DskipTests

2. docker build -t library-management-system:latest -f docker/Dockerfile .

3. Go to docker folder : 
	docker-compose up -d --build

4. docker-compose logs -f

5. curl http://localhost:8080/actuator/health

6. minikube start

7. minikube image load library-management-system:latest

8. back to project dir : 
	kubectl apply -f k8s/

9. kubectl get pods

10. kubectl get svc library-api-service

11. minikube service library-api-service --url



```

## Testing
```bash
# Run all tests
mvn test

# Run with coverage
mvn clean test jacoco:report
```

The coverage report will be available at `target/site/jacoco/index.html`

## Health Checks
- Application health: `GET /actuator/health`
- Application info: `GET /actuator/info`
- Metrics: `GET /actuator/metrics`

## Outcome of the projects

- Swagger API Documentation
<img width="1512" height="982" alt="image" src="https://github.com/user-attachments/assets/f51b4e86-96ca-479c-b215-0b95fc2c2e4c" />


- Build Docker image locally </br>
<img width="1512" height="982" alt="image" src="https://github.com/user-attachments/assets/ded9d609-b0ee-46c1-9ef9-ab77127f927f" />


- pushed to docker hub through github actions : </br>
<img width="1503" height="746" alt="Screenshot 2025-08-24 at 1 08 00 AM" src="https://github.com/user-attachments/assets/2cb939c3-719e-4970-93e0-8d0423e7bd75" />


- CI/CD pipeline execution through GitHub Actions </br>
<img width="1491" height="617" alt="image" src="https://github.com/user-attachments/assets/80a5ebc4-6840-4ac8-94f4-fdda102da9f9" />

- Test Report using: Jacoco </br>
<img width="1512" height="982" alt="image" src="https://github.com/user-attachments/assets/7839b45d-24c2-424b-9d7b-ea3fbbd5a884" />







