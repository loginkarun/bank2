# API Test Cases - bank2 SpringBoot Application

## Test Execution Information
- **Application:** myproject (bank2)
- **Version:** 1.0.0
- **Base URL:** http://localhost:8080/api
- **Test Framework:** Postman/Newman
- **Date Generated:** 2024-01-15

---

## Table of Contents
1. [Health Check Endpoint Tests](#health-check-endpoint-tests)
2. [Actuator Endpoint Tests](#actuator-endpoint-tests)
3. [API Documentation Tests](#api-documentation-tests)
4. [CORS Configuration Tests](#cors-configuration-tests)
5. [Error Handling Tests](#error-handling-tests)

---

## Health Check Endpoint Tests

### TC-HEALTH-001: Health Check - Success (Happy Path)

**Test Case ID:** TC-HEALTH-001  
**Priority:** High  
**Type:** Positive Test  
**Endpoint:** GET /api/v1/health  
**Scenario:** Verify health check endpoint returns correct status when application is running

**Preconditions:**
- Application is running on http://localhost:8080
- No authentication required

**Test Steps:**
1. Send GET request to `/api/v1/health`
2. Set Accept header to `application/json`
3. Verify response status code
4. Verify response body structure
5. Verify response field values
6. Verify response time

**Expected Result:**
- Status Code: 200 OK
- Response Body:
  ```json
  {
    "status": "UP",
    "timestamp": "2024-01-15T10:30:45",
    "service": "myproject",
    "version": "1.0.0"
  }
  ```
- Response time < 500ms
- Content-Type: application/json

**Assertions:**
- ✓ Status code is 200
- ✓ Response has 'status' field
- ✓ Response has 'timestamp' field
- ✓ Response has 'service' field
- ✓ Response has 'version' field
- ✓ Status value equals 'UP'
- ✓ Service value equals 'myproject'
- ✓ Version value equals '1.0.0'
- ✓ Timestamp matches ISO format (yyyy-MM-dd'T'HH:mm:ss)
- ✓ Response time is below 500ms

**Test Data:** None required

**Pass/Fail Criteria:**
- PASS: All assertions pass
- FAIL: Any assertion fails

---

### TC-HEALTH-002: Health Check - Invalid HTTP Method

**Test Case ID:** TC-HEALTH-002  
**Priority:** Medium  
**Type:** Negative Test  
**Endpoint:** POST /api/v1/health  
**Scenario:** Verify health check endpoint rejects POST method

**Preconditions:**
- Application is running on http://localhost:8080

**Test Steps:**
1. Send POST request to `/api/v1/health`
2. Set Accept header to `application/json`
3. Verify response status code
4. Verify error response structure

**Expected Result:**
- Status Code: 405 Method Not Allowed
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:45",
    "status": 405,
    "error": "Method Not Allowed",
    "path": "/api/v1/health"
  }
  ```

**Assertions:**
- ✓ Status code is 405
- ✓ Response has 'timestamp' field
- ✓ Response has 'status' field
- ✓ Response has 'error' field
- ✓ Error indicates method not allowed

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: Returns 405 with proper error structure
- FAIL: Returns 200 or incorrect error format

---

### TC-HEALTH-003: Health Check - Invalid Endpoint Path

**Test Case ID:** TC-HEALTH-003  
**Priority:** Medium  
**Type:** Negative Test  
**Endpoint:** GET /api/v1/health/invalid  
**Scenario:** Verify non-existent endpoint returns 404

**Preconditions:**
- Application is running on http://localhost:8080

**Test Steps:**
1. Send GET request to `/api/v1/health/invalid`
2. Set Accept header to `application/json`
3. Verify response status code
4. Verify error response

**Expected Result:**
- Status Code: 404 Not Found
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:45",
    "status": 404,
    "error": "Not Found",
    "path": "/api/v1/health/invalid"
  }
  ```

**Assertions:**
- ✓ Status code is 404
- ✓ Response has error structure
- ✓ Status field equals 404

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: Returns 404 with proper error structure
- FAIL: Returns 200 or other status code

---

### TC-HEALTH-004: Health Check - Response Time Performance

**Test Case ID:** TC-HEALTH-004  
**Priority:** High  
**Type:** Performance Test  
**Endpoint:** GET /api/v1/health  
**Scenario:** Verify health check endpoint responds within acceptable time

**Preconditions:**
- Application is running on http://localhost:8080
- No heavy load on server

**Test Steps:**
1. Send GET request to `/api/v1/health`
2. Measure response time
3. Verify response time is within threshold

**Expected Result:**
- Response time < 500ms
- Status Code: 200 OK

**Assertions:**
- ✓ Response time is below 500ms
- ✓ Status code is 200

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: Response time < 500ms
- FAIL: Response time >= 500ms

---

### TC-HEALTH-005: Health Check - Content Type Validation

**Test Case ID:** TC-HEALTH-005  
**Priority:** Low  
**Type:** Positive Test  
**Endpoint:** GET /api/v1/health  
**Scenario:** Verify health check endpoint returns correct Content-Type header

**Preconditions:**
- Application is running on http://localhost:8080

**Test Steps:**
1. Send GET request to `/api/v1/health`
2. Verify Content-Type response header

**Expected Result:**
- Content-Type: application/json
- Status Code: 200 OK

**Assertions:**
- ✓ Content-Type header is present
- ✓ Content-Type includes 'application/json'

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: Content-Type is application/json
- FAIL: Content-Type is missing or incorrect

---

## Actuator Endpoint Tests

### TC-ACTUATOR-001: Actuator Health Endpoint

**Test Case ID:** TC-ACTUATOR-001  
**Priority:** High  
**Type:** Positive Test  
**Endpoint:** GET /api/actuator/health  
**Scenario:** Verify Spring Boot Actuator health endpoint is accessible

**Preconditions:**
- Application is running on http://localhost:8080
- Actuator endpoints are enabled

**Test Steps:**
1. Send GET request to `/api/actuator/health`
2. Verify response status code
3. Verify health status

**Expected Result:**
- Status Code: 200 OK
- Response Body:
  ```json
  {
    "status": "UP"
  }
  ```

**Assertions:**
- ✓ Status code is 200
- ✓ Health status is 'UP'

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: Returns 200 with status UP
- FAIL: Returns error or status DOWN

---

### TC-ACTUATOR-002: Actuator Info Endpoint

**Test Case ID:** TC-ACTUATOR-002  
**Priority:** Medium  
**Type:** Positive Test  
**Endpoint:** GET /api/actuator/info  
**Scenario:** Verify Spring Boot Actuator info endpoint is accessible

**Preconditions:**
- Application is running on http://localhost:8080
- Actuator endpoints are enabled

**Test Steps:**
1. Send GET request to `/api/actuator/info`
2. Verify response status code

**Expected Result:**
- Status Code: 200 OK
- Response Body: JSON object (may be empty if not configured)

**Assertions:**
- ✓ Status code is 200

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: Returns 200
- FAIL: Returns error status

---

### TC-ACTUATOR-003: Actuator Metrics Endpoint

**Test Case ID:** TC-ACTUATOR-003  
**Priority:** Low  
**Type:** Positive Test  
**Endpoint:** GET /api/actuator/metrics  
**Scenario:** Verify Spring Boot Actuator metrics endpoint is accessible

**Preconditions:**
- Application is running on http://localhost:8080
- Actuator endpoints are enabled
- Metrics endpoint is exposed

**Test Steps:**
1. Send GET request to `/api/actuator/metrics`
2. Verify response status code
3. Verify metrics list is returned

**Expected Result:**
- Status Code: 200 OK
- Response Body contains metrics names

**Assertions:**
- ✓ Status code is 200
- ✓ Response contains 'names' array

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: Returns 200 with metrics list
- FAIL: Returns error or empty response

---

## API Documentation Tests

### TC-DOCS-001: OpenAPI JSON Specification

**Test Case ID:** TC-DOCS-001  
**Priority:** High  
**Type:** Positive Test  
**Endpoint:** GET /api/api-docs  
**Scenario:** Verify OpenAPI specification is accessible and valid

**Preconditions:**
- Application is running on http://localhost:8080
- SpringDoc OpenAPI is configured

**Test Steps:**
1. Send GET request to `/api/api-docs`
2. Verify response status code
3. Verify OpenAPI structure
4. Verify required OpenAPI fields

**Expected Result:**
- Status Code: 200 OK
- Response Body: Valid OpenAPI 3.0 specification
  ```json
  {
    "openapi": "3.0.1",
    "info": {
      "title": "bank2 API",
      "version": "1.0.0"
    },
    "paths": { ... }
  }
  ```

**Assertions:**
- ✓ Status code is 200
- ✓ Response has 'openapi' field
- ✓ Response has 'info' field
- ✓ Response has 'paths' field
- ✓ OpenAPI version is 3.x

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: Returns valid OpenAPI specification
- FAIL: Returns error or invalid spec

---

### TC-DOCS-002: Swagger UI Accessibility

**Test Case ID:** TC-DOCS-002  
**Priority:** Medium  
**Type:** Positive Test  
**Endpoint:** GET /api/swagger-ui.html  
**Scenario:** Verify Swagger UI is accessible

**Preconditions:**
- Application is running on http://localhost:8080
- Swagger UI is enabled

**Test Steps:**
1. Send GET request to `/api/swagger-ui.html`
2. Verify response status code
3. Verify HTML content is returned

**Expected Result:**
- Status Code: 200 OK
- Content-Type: text/html
- Response contains Swagger UI HTML

**Assertions:**
- ✓ Status code is 200
- ✓ Content-Type is text/html

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: Returns 200 with HTML content
- FAIL: Returns error or non-HTML response

---

## CORS Configuration Tests

### TC-CORS-001: CORS Preflight Request

**Test Case ID:** TC-CORS-001  
**Priority:** High  
**Type:** Positive Test  
**Endpoint:** OPTIONS /api/v1/health  
**Scenario:** Verify CORS preflight request is handled correctly

**Preconditions:**
- Application is running on http://localhost:8080
- CORS is configured for http://localhost:4200

**Test Steps:**
1. Send OPTIONS request to `/api/v1/health`
2. Set Origin header to `http://localhost:4200`
3. Set Access-Control-Request-Method header to `GET`
4. Verify CORS response headers

**Expected Result:**
- Status Code: 200 OK
- Response Headers:
  - Access-Control-Allow-Origin: http://localhost:4200
  - Access-Control-Allow-Methods: GET, POST, PUT, DELETE, PATCH, OPTIONS
  - Access-Control-Allow-Headers: *
  - Access-Control-Allow-Credentials: true
  - Access-Control-Max-Age: 3600

**Assertions:**
- ✓ Status code is 200
- ✓ Access-Control-Allow-Origin header is present
- ✓ Access-Control-Allow-Methods header is present
- ✓ Access-Control-Allow-Headers header is present

**Test Data:**
- Origin: http://localhost:4200
- Request Method: GET

**Pass/Fail Criteria:**
- PASS: All CORS headers are present and correct
- FAIL: Missing or incorrect CORS headers

---

### TC-CORS-002: CORS Actual Request

**Test Case ID:** TC-CORS-002  
**Priority:** High  
**Type:** Positive Test  
**Endpoint:** GET /api/v1/health  
**Scenario:** Verify CORS headers are present in actual request

**Preconditions:**
- Application is running on http://localhost:8080
- CORS is configured

**Test Steps:**
1. Send GET request to `/api/v1/health`
2. Set Origin header to `http://localhost:4200`
3. Verify CORS response headers

**Expected Result:**
- Status Code: 200 OK
- Response Headers:
  - Access-Control-Allow-Origin: http://localhost:4200
  - Access-Control-Allow-Credentials: true

**Assertions:**
- ✓ Status code is 200
- ✓ Access-Control-Allow-Origin header is present

**Test Data:**
- Origin: http://localhost:4200

**Pass/Fail Criteria:**
- PASS: CORS headers are present
- FAIL: Missing CORS headers

---

### TC-CORS-003: CORS Rejected Origin

**Test Case ID:** TC-CORS-003  
**Priority:** Medium  
**Type:** Negative Test  
**Endpoint:** GET /api/v1/health  
**Scenario:** Verify requests from non-allowed origins are handled

**Preconditions:**
- Application is running on http://localhost:8080
- CORS is configured for http://localhost:4200 only

**Test Steps:**
1. Send GET request to `/api/v1/health`
2. Set Origin header to `http://malicious-site.com`
3. Verify response

**Expected Result:**
- Status Code: 200 OK (request succeeds but CORS headers may be absent)
- Access-Control-Allow-Origin header should not include malicious-site.com

**Assertions:**
- ✓ Request is processed
- ✓ CORS headers do not allow malicious origin

**Test Data:**
- Origin: http://malicious-site.com

**Pass/Fail Criteria:**
- PASS: Request succeeds but CORS headers don't allow origin
- FAIL: CORS headers allow unauthorized origin

---

## Error Handling Tests

### TC-ERROR-001: Global Exception Handler - 404 Not Found

**Test Case ID:** TC-ERROR-001  
**Priority:** High  
**Type:** Negative Test  
**Endpoint:** GET /api/v1/nonexistent  
**Scenario:** Verify 404 error is handled with proper error response

**Preconditions:**
- Application is running on http://localhost:8080
- GlobalExceptionHandler is configured

**Test Steps:**
1. Send GET request to non-existent endpoint `/api/v1/nonexistent`
2. Verify error response structure

**Expected Result:**
- Status Code: 404 Not Found
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:45",
    "status": 404,
    "error": "Not Found",
    "path": "/api/v1/nonexistent"
  }
  ```

**Assertions:**
- ✓ Status code is 404
- ✓ Response has 'timestamp' field
- ✓ Response has 'status' field
- ✓ Response has 'error' field
- ✓ Response has 'path' field

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: Returns 404 with proper error structure
- FAIL: Returns different status or malformed error

---

### TC-ERROR-002: Global Exception Handler - 500 Internal Server Error

**Test Case ID:** TC-ERROR-002  
**Priority:** High  
**Type:** Negative Test  
**Endpoint:** Any endpoint that triggers server error  
**Scenario:** Verify 500 error is handled with proper error response

**Preconditions:**
- Application is running on http://localhost:8080
- GlobalExceptionHandler is configured
- Endpoint that can trigger server error

**Test Steps:**
1. Trigger a server error (e.g., database connection failure)
2. Verify error response structure

**Expected Result:**
- Status Code: 500 Internal Server Error
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:45",
    "traceId": "uuid-string",
    "errorCode": "INTERNAL_SERVER_ERROR",
    "message": "An unexpected error occurred: ...",
    "details": []
  }
  ```

**Assertions:**
- ✓ Status code is 500
- ✓ Response has 'timestamp' field
- ✓ Response has 'traceId' field
- ✓ Response has 'errorCode' field
- ✓ Response has 'message' field
- ✓ Response has 'details' array

**Test Data:** Varies based on error trigger

**Pass/Fail Criteria:**
- PASS: Returns 500 with standardized error structure
- FAIL: Returns different status or exposes stack trace

---

### TC-ERROR-003: Validation Error Handling

**Test Case ID:** TC-ERROR-003  
**Priority:** High  
**Type:** Negative Test  
**Endpoint:** Any endpoint with validation  
**Scenario:** Verify validation errors return proper error response

**Preconditions:**
- Application is running on http://localhost:8080
- Endpoint has validation constraints

**Test Steps:**
1. Send request with invalid data
2. Verify validation error response

**Expected Result:**
- Status Code: 400 Bad Request
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:45",
    "traceId": "uuid-string",
    "errorCode": "VALIDATION_ERROR",
    "message": "Validation failed for one or more fields",
    "details": [
      {
        "field": "fieldName",
        "issue": "validation message"
      }
    ]
  }
  ```

**Assertions:**
- ✓ Status code is 400
- ✓ Response has error structure
- ✓ Error code is VALIDATION_ERROR
- ✓ Details array contains field-level errors

**Test Data:** Invalid request payload

**Pass/Fail Criteria:**
- PASS: Returns 400 with field-level validation errors
- FAIL: Returns different status or generic error

---

## Edge Cases and Additional Tests

### TC-EDGE-001: Large Response Payload

**Test Case ID:** TC-EDGE-001  
**Priority:** Low  
**Type:** Edge Case Test  
**Endpoint:** GET /api/v1/health  
**Scenario:** Verify application handles response correctly

**Preconditions:**
- Application is running

**Test Steps:**
1. Send GET request
2. Verify response is complete

**Expected Result:**
- Complete response received
- No truncation

**Assertions:**
- ✓ Response is complete
- ✓ All fields are present

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: Complete response
- FAIL: Truncated or incomplete response

---

### TC-EDGE-002: Concurrent Requests

**Test Case ID:** TC-EDGE-002  
**Priority:** Medium  
**Type:** Load Test  
**Endpoint:** GET /api/v1/health  
**Scenario:** Verify application handles multiple concurrent requests

**Preconditions:**
- Application is running

**Test Steps:**
1. Send 100 concurrent GET requests
2. Verify all requests succeed
3. Verify response times

**Expected Result:**
- All requests return 200 OK
- Average response time < 500ms
- No errors

**Assertions:**
- ✓ All requests succeed
- ✓ No timeout errors
- ✓ Response times acceptable

**Test Data:** None

**Pass/Fail Criteria:**
- PASS: All requests succeed within time limits
- FAIL: Any request fails or times out

---

### TC-EDGE-003: Special Characters in URL

**Test Case ID:** TC-EDGE-003  
**Priority:** Low  
**Type:** Edge Case Test  
**Endpoint:** GET /api/v1/health?param=<script>  
**Scenario:** Verify application handles special characters safely

**Preconditions:**
- Application is running

**Test Steps:**
1. Send GET request with special characters in query params
2. Verify application doesn't crash
3. Verify proper error handling

**Expected Result:**
- Application handles request safely
- No XSS vulnerability
- Proper error response if invalid

**Assertions:**
- ✓ Application doesn't crash
- ✓ No security vulnerability
- ✓ Proper error handling

**Test Data:** Special characters: <, >, &, ", '

**Pass/Fail Criteria:**
- PASS: Request handled safely
- FAIL: Application crashes or security issue

---

## Test Execution Summary Template

### Execution Metadata
- **Test Suite:** bank2 API Test Suite
- **Execution Date:** [To be filled]
- **Executed By:** [To be filled]
- **Environment:** Local Development
- **Application Version:** 1.0.0

### Test Results Summary

| Category | Total | Passed | Failed | Skipped | Pass Rate |
|----------|-------|--------|--------|---------|----------|
| Health Check | 5 | - | - | - | - |
| Actuator | 3 | - | - | - | - |
| API Docs | 2 | - | - | - | - |
| CORS | 3 | - | - | - | - |
| Error Handling | 3 | - | - | - | - |
| Edge Cases | 3 | - | - | - | - |
| **TOTAL** | **19** | **-** | **-** | **-** | **-%** |

### Detailed Results

| Test Case ID | Test Name | Status | Duration | Notes |
|--------------|-----------|--------|----------|-------|
| TC-HEALTH-001 | Health Check Success | - | - | - |
| TC-HEALTH-002 | Invalid HTTP Method | - | - | - |
| TC-HEALTH-003 | Invalid Endpoint | - | - | - |
| TC-HEALTH-004 | Response Time | - | - | - |
| TC-HEALTH-005 | Content Type | - | - | - |
| TC-ACTUATOR-001 | Actuator Health | - | - | - |
| TC-ACTUATOR-002 | Actuator Info | - | - | - |
| TC-ACTUATOR-003 | Actuator Metrics | - | - | - |
| TC-DOCS-001 | OpenAPI JSON | - | - | - |
| TC-DOCS-002 | Swagger UI | - | - | - |
| TC-CORS-001 | CORS Preflight | - | - | - |
| TC-CORS-002 | CORS Actual Request | - | - | - |
| TC-CORS-003 | CORS Rejected Origin | - | - | - |
| TC-ERROR-001 | 404 Error Handling | - | - | - |
| TC-ERROR-002 | 500 Error Handling | - | - | - |
| TC-ERROR-003 | Validation Errors | - | - | - |
| TC-EDGE-001 | Large Response | - | - | - |
| TC-EDGE-002 | Concurrent Requests | - | - | - |
| TC-EDGE-003 | Special Characters | - | - | - |

### Failed Test Details
[To be filled with failure details]

### Known Issues
[To be filled with known issues]

### Recommendations
[To be filled with recommendations]

---

## Test Execution Instructions

### Prerequisites
1. Install Postman or Newman CLI
2. Start the SpringBoot application:
   ```bash
   cd code
   mvn spring-boot:run
   ```
3. Verify application is running at http://localhost:8080/api

### Running Tests with Postman
1. Import `test/postman/collection.json` into Postman
2. Import `test/postman/environment.json` into Postman
3. Select the "bank2 API Environment" environment
4. Run the collection
5. View test results in Postman

### Running Tests with Newman (CLI)
```bash
# Install Newman
npm install -g newman

# Run tests
newman run test/postman/collection.json \
  -e test/postman/environment.json \
  --reporters cli,html \
  --reporter-html-export test/reports/newman-report.html
```

### Continuous Integration
Tests can be integrated into CI/CD pipeline:
```yaml
# Example GitHub Actions step
- name: Run API Tests
  run: |
    newman run test/postman/collection.json \
      -e test/postman/environment.json \
      --reporters cli,junit \
      --reporter-junit-export test/reports/junit-report.xml
```

---

## Test Data Management

### Environment Variables
- `base_url`: http://localhost:8080/api
- `host`: localhost
- `port`: 8080
- `context_path`: /api
- `service_name`: myproject
- `service_version`: 1.0.0

### Test Data Files
No external test data files required for current test suite.

---

## Maintenance and Updates

### When to Update Tests
- New endpoints are added
- Existing endpoints are modified
- New validation rules are implemented
- Error handling changes
- Security requirements change

### Test Review Schedule
- Review test cases after each sprint
- Update test cases when API changes
- Add new test cases for new features
- Remove obsolete test cases

---

## Contact and Support

For questions or issues with test cases:
- Review project documentation
- Check GitHub repository issues
- Contact development team

---

**Document Version:** 1.0.0  
**Last Updated:** 2024-01-15  
**Status:** Active
