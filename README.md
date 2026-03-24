# Drug Management

Spring Boot based medicine inventory service with Actuator health checks and CRUD-style medicine management APIs.

## Features

- Add, update, delete, and fetch medicines
- Update stock directly
- Restock and order medicines
- Search by keyword
- Filter by category and manufacturer
- Check low-stock and expiring-soon medicines
- Actuator health endpoint
- Local in-memory H2 profile for quick testing

## Tech Stack

- Java 17
- Spring Boot 3.2.5
- Spring Web
- Spring Data JPA
- MySQL
- H2 for local testing
- Maven

## Prerequisites

- Java 17
- Maven wrapper included in the project
- Optional: MySQL if you want to run with the default datasource config

## Build

Compile, test, and package:

```bash
./mvnw clean package
```

Compile only:

```bash
./mvnw -DskipTests compile
```

## Run

### Option 1: Run with local H2 profile

This is the easiest way to test the APIs without setting up MySQL.

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

The local profile is configured in [application-local.properties](/Users/abhisheksharma/Downloads/drug-management/src/main/resources/application-local.properties).

### Option 2: Run with MySQL

The default datasource config is in [application.properties](/Users/abhisheksharma/Downloads/drug-management/src/main/resources/application.properties).

Make sure these are valid for your machine:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

Then run:

```bash
./mvnw spring-boot:run
```

## Base URLs

- Application: `http://localhost:8080`
- Health: `http://localhost:8080/actuator/health`

## API Endpoints

### Health

- `GET /actuator/health`

### Medicine APIs

- `GET /api/medicines`
- `GET /api/medicines/{id}`
- `POST /api/medicines`
- `PUT /api/medicines/{id}`
- `DELETE /api/medicines/{id}`
- `PATCH /api/medicines/{id}/stock?stock=120`
- `POST /api/medicines/{id}/restock?quantity=25`
- `POST /api/medicines/{id}/order?quantity=5`
- `GET /api/medicines/search?keyword=para`
- `GET /api/medicines/low-stock?threshold=20`
- `GET /api/medicines/expiring-soon?beforeDate=2026-12-31`
- `GET /api/medicines/category/{category}`
- `GET /api/medicines/manufacturer/{manufacturer}`

## Example Request Payload

```json
{
  "name": "Paracetamol",
  "manufacturer": "ABC Pharma",
  "category": "General",
  "price": 49.99,
  "stock": 50,
  "expiryDate": "2027-12-31"
}
```

## End-to-End Curl Flow

Run the service first, then execute the following in sequence.

### 1. Health check

```bash
curl -X GET http://localhost:8080/actuator/health
```

### 2. Add medicine 1

```bash
curl -X POST http://localhost:8080/api/medicines \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Paracetamol",
    "manufacturer": "ABC Pharma",
    "category": "General",
    "price": 49.99,
    "stock": 50,
    "expiryDate": "2027-12-31"
  }'
```

### 3. Add medicine 2

```bash
curl -X POST http://localhost:8080/api/medicines \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Insulin",
    "manufacturer": "HealthCorp",
    "category": "Diabetes",
    "price": 299.00,
    "stock": 5,
    "expiryDate": "2026-12-15"
  }'
```

### 4. Get all medicines

```bash
curl -X GET http://localhost:8080/api/medicines
```

### 5. Get medicine by id

```bash
curl -X GET http://localhost:8080/api/medicines/1
```

### 6. Update full medicine

```bash
curl -X PUT http://localhost:8080/api/medicines/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Paracetamol 650",
    "manufacturer": "ABC Pharma",
    "category": "General",
    "price": 59.99,
    "stock": 60,
    "expiryDate": "2028-01-31"
  }'
```

### 7. Set stock directly

```bash
curl -X PATCH "http://localhost:8080/api/medicines/1/stock?stock=120"
```

### 8. Restock

```bash
curl -X POST "http://localhost:8080/api/medicines/1/restock?quantity=25"
```

### 9. Order medicine

```bash
curl -X POST "http://localhost:8080/api/medicines/1/order?quantity=5"
```

### 10. Verify updated medicine

```bash
curl -X GET http://localhost:8080/api/medicines/1
```

### 11. Search

```bash
curl -X GET "http://localhost:8080/api/medicines/search?keyword=para"
```

### 12. Low stock

```bash
curl -X GET "http://localhost:8080/api/medicines/low-stock?threshold=20"
```

### 13. Expiring soon

```bash
curl -X GET "http://localhost:8080/api/medicines/expiring-soon?beforeDate=2026-12-31"
```

### 14. Get by category

```bash
curl -X GET http://localhost:8080/api/medicines/category/General
```

### 15. Get by manufacturer

```bash
curl -X GET "http://localhost:8080/api/medicines/manufacturer/ABC%20Pharma"
```

### 16. Delete medicine

```bash
curl -X DELETE http://localhost:8080/api/medicines/2
```

### 17. Confirm remaining medicines

```bash
curl -X GET http://localhost:8080/api/medicines
```

### 18. Confirm deleted item returns 404

```bash
curl -X GET http://localhost:8080/api/medicines/2
```

## Error Handling

### Not found

```json
{
  "message": "Medicine not found with id: 99"
}
```

### Invalid order quantity

```json
{
  "message": "Order quantity must be greater than 0"
}
```

### Insufficient stock

The response includes available stock.

```json
{
  "message": "Insufficient stock for medicine id: 1",
  "availableStock": 45
}
```

Example:

```bash
curl -X POST "http://localhost:8080/api/medicines/1/order?quantity=10000"
```

## Running the packaged jar

Build first:

```bash
./mvnw clean package
```

Run the jar:

```bash
java -jar target/drugs-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

## Notes

- The project is configured for Java 17.
- For local testing, prefer the `local` profile to avoid MySQL setup.
- The default profile expects a reachable MySQL database.
