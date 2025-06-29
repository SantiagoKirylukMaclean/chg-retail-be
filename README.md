# Retail Price API

## Overview
This project is a Spring Boot application that provides a REST API for querying product prices based on various criteria. The API allows users to find the applicable price for a product at a specific date and time, taking into account different pricing rules and priorities.

## Technical Challenge
The application implements a solution for the following technical challenge:

The company's e-commerce database contains a PRICES table that reflects the final price (pvp) and the rate that applies to a product of a chain between certain dates. The table has the following structure:

```
PRICES
-------
BRAND_ID  START_DATE            END_DATE              PRICE_LIST  PRODUCT_ID  PRIORITY  PRICE   CURR
--------  ---------------------  --------------------  ----------  ----------  --------  ------  ----
1         2020-06-14-00.00.00    2020-12-31-23.59.59   1           35455       0         35.50  EUR
1         2020-06-14-15.00.00    2020-06-14-18.30.00   2           35455       1         25.45  EUR
1         2020-06-15-00.00.00    2020-06-15-11.00.00   3           35455       1         30.50  EUR
1         2020-06-15-16.00.00    2020-12-31-23.59.59   4           35455       1         38.95  EUR
```

Where:
- BRAND_ID: Foreign key of the chain group (1 = ZARA)
- START_DATE, END_DATE: Range of dates when the indicated price rate applies
- PRICE_LIST: Identifier of the applicable price list
- PRODUCT_ID: Product code identifier
- PRIORITY: Price application disambiguator. If two rates coincide in a date range, the one with the higher priority (higher numerical value) is applied
- PRICE: Final sale price
- CURR: Currency ISO

## Requirements
- Java 17 or higher
- Gradle

## Running the Application
To run the application locally:

```bash
./gradlew bootRun
```

The application will start on port 8080 by default.

## API Documentation
Once the application is running, you can access the API documentation at:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

The Swagger UI provides an interactive interface to explore and test the API, while the OpenAPI JSON endpoint provides the raw API specification that can be imported into other tools.

## API Endpoints

### Get Price
Returns the applicable price for a product at a specific date and time.

```
GET /api/prices?date={date}&productId={productId}&brandId={brandId}
```

Parameters:
- `date`: The date and time to query in ISO format (format: yyyy-MM-dd'T'HH:mm:ss)
- `productId`: The product identifier
- `brandId`: The brand identifier

Response:
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14-00.00.00",
  "endDate": "2020-12-31-23.59.59",
  "price": 35.50,
  "currency": "EUR"
}
```

## Testing
To run the tests:

```bash
./gradlew test
```

The test suite includes 5 specific test cases that validate the API's behavior with different date/time inputs.

### Test Coverage
The project uses JaCoCo for test coverage reporting. Current test coverage metrics:

| Metric | Coverage |
|--------|----------|
| Instruction | 60.04% |
| Branch | 80.00% |
| Line | 66.67% |
| Complexity | 60.32% |
| Method | 58.62% |
| Class | 83.33% |

To generate the test coverage report:

```bash
./gradlew jacocoTestReport
```

The HTML report will be available at `build/reports/jacoco/index.html`.

## Project Structure
The project follows a hexagonal architecture with three main layers:

1. **Domain Layer**: Contains the core business logic and domain models
2. **Application Layer**: Contains the application services and DTOs
3. **Infrastructure Layer**: Contains the controllers, repositories, and external dependencies

## Technologies Used
- Spring Boot 3.5.3
- Spring Data JPA
- H2 Database
- Flyway for database migrations
- Springdoc OpenAPI 2.4.0 for API documentation
- JUnit 5 for testing

## Implementation Details and Reasoning

This section describes the implementation approach and the reasoning behind key design decisions.

### Hexagonal Architecture

The project follows a hexagonal architecture (also known as ports and adapters) with three main layers:

1. **Domain Layer**: Contains the core business logic and domain models
   - The `Price` model represents the core domain entity with all business rules
   - The `PriceRepository` interface defines the port for data access

2. **Application Layer**: Contains the application services and use cases
   - The `PriceService` interface defines the application's use cases
   - The `DefaultPriceService` implementation orchestrates the business logic
   - DTOs (`PriceQueryRequest`, `PriceResponse`) isolate the domain from external concerns

3. **Infrastructure Layer**: Contains adapters to external systems
   - The `PriceController` adapts HTTP requests to application services
   - The `PriceEntity` and `JpaPriceRepository` adapt the database to the domain
   - The `GlobalExceptionHandler` provides consistent error handling

This architecture provides several benefits:
- **Separation of concerns**: Each layer has a clear responsibility
- **Testability**: The domain and application layers can be tested in isolation
- **Flexibility**: External systems can be replaced without affecting the core business logic

### Key Design Decisions

1. **Price Selection Logic**: 
   - Implemented using the Strategy pattern to make the disambiguation rule explicit and maintainable
   - The `PriceDisambiguationStrategy` interface defines the contract for price selection
   - The `HighestPriorityPriceDisambiguationStrategy` implementation selects the price with the highest priority
   - This approach follows the Open/Closed Principle, allowing new strategies to be added without modifying existing code

2. **Exception Handling**:
   - Centralized in `GlobalExceptionHandler` to provide consistent error responses
   - Custom exceptions map to appropriate HTTP status codes
   - Detailed error messages help clients understand what went wrong

3. **API Documentation**:
   - Implemented with Springdoc OpenAPI for automatic documentation generation
   - Annotated controllers and DTOs provide rich metadata for the API documentation
   - Interactive Swagger UI allows easy exploration and testing of the API

4. **Testing Strategy**:
   - Unit tests for the service layer verify business logic in isolation
   - Integration tests for the controller verify the API contract
   - The five specific test cases validate the correct price selection under different scenarios

### Implementation Challenges and Solutions

1. **JPA Entity Requirements**:
   - Challenge: JPA requires a default constructor for entities
   - Solution: Added a default constructor to `PriceEntity` while maintaining immutability

2. **OpenAPI Compatibility**:
   - Challenge: Version compatibility issues between Springdoc OpenAPI and Spring Boot
   - Solution: Updated to Springdoc OpenAPI 2.4.0 to resolve method not found errors

3. **Price Selection Logic**:
   - Challenge: Ensuring the correct price is selected when multiple prices apply
   - Solution: Implemented a clear priority-based selection algorithm in the service layer
   - Used Kotlin's functional programming features for concise and readable code

### Future Improvements

While the current implementation satisfies all requirements, several improvements could be made:

1. **Caching**: Implement caching for frequently requested prices to improve performance
2. **Pagination**: Add pagination for endpoints that might return large result sets
3. **Security**: Implement authentication and authorization for API access
4. **Monitoring**: Add metrics and health checks for operational visibility
5. **Performance Optimization**: Optimize database queries for large datasets
