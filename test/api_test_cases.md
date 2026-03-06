# API Test Cases - Cart Coupon Service (SCRUM-11693)

## Test Suite Overview

**Feature:** Apply or Remove Coupon in Cart  
**JIRA Issue:** SCRUM-11693  
**Microservice:** cart_coupon_service  
**Test Date:** 2024-01-15  
**Test Environment:** Local Development  
**Base URL:** http://localhost:8080/api  

---

## Test Coverage Summary

| Category | Total Tests | Positive Tests | Negative Tests |
|----------|-------------|----------------|----------------|
| Health Check | 1 | 1 | 0 |
| Apply Coupon | 7 | 1 | 6 |
| Remove Coupon | 4 | 2 | 2 |
| Validate Coupon | 6 | 1 | 5 |
| **TOTAL** | **18** | **5** | **13** |

---

## 1. Health Check Tests

### TC-HC-001: Health Check - Positive

**Test Case ID:** TC-HC-001  
**Endpoint:** GET /api/v1/health  
**Scenario:** Verify application health status  
**Priority:** High  
**Test Type:** Positive  

**Preconditions:**
- Application is running
- No authentication required

**Test Steps:**
1. Send GET request to /api/v1/health
2. Verify response status code
3. Verify response body structure
4. Verify service status is UP

**Expected Result:**
- Status Code: 200 OK
- Response Body:
  ```json
  {
    "status": "UP",
    "timestamp": "2024-01-15T10:30:00",
    "service": "myproject",
    "version": "1.0.0"
  }
  ```
- Response contains all required fields
- Status is "UP"

**Assertions:**
- ✓ Status code is 200
- ✓ Response has correct structure
- ✓ Service status is UP
- ✓ All mandatory fields are present

---

## 2. Apply Coupon Tests

### TC-AC-001: Apply Valid Coupon - Positive

**Test Case ID:** TC-AC-001  
**Endpoint:** POST /api/cart/{cartId}/coupon  
**Scenario:** Apply a valid coupon code to cart  
**Priority:** Critical  
**Test Type:** Positive  

**Preconditions:**
- Valid JWT token is available
- Cart exists with ID 12345
- Cart contains items
- Coupon "SAVE20" exists and is valid
- Coupon is applicable to cart items

**Test Steps:**
1. Send POST request to /api/cart/12345/coupon
2. Include valid JWT token in Authorization header
3. Send request body with couponCode: "SAVE20"
4. Verify response status code is 200
5. Verify response time is < 500ms
6. Verify discount is applied
7. Verify total is less than subtotal

**Request:**
```json
{
  "couponCode": "SAVE20"
}
```

**Expected Result:**
- Status Code: 200 OK
- Response Time: < 500ms
- Response Body:
  ```json
  {
    "cartId": 12345,
    "userId": 67890,
    "items": [
      {
        "productId": 101,
        "productName": "Laptop",
        "quantity": 1,
        "price": 1000.00
      }
    ],
    "subtotal": 1000.00,
    "couponCode": "SAVE20",
    "discount": 200.00,
    "total": 800.00,
    "appliedAt": "2024-01-15T10:30:00Z"
  }
  ```

**Assertions:**
- ✓ Status code is 200
- ✓ Response time < 500ms
- ✓ Response has CartResponse structure
- ✓ Coupon code is "SAVE20"
- ✓ Discount > 0
- ✓ Total < Subtotal
- ✓ appliedAt timestamp is present

---

### TC-AC-002: Apply Coupon - Empty Coupon Code

**Test Case ID:** TC-AC-002  
**Endpoint:** POST /api/cart/{cartId}/coupon  
**Scenario:** Attempt to apply empty coupon code  
**Priority:** High  
**Test Type:** Negative (Validation)  

**Preconditions:**
- Valid JWT token is available
- Cart exists with ID 12345

**Test Steps:**
1. Send POST request to /api/cart/12345/coupon
2. Include valid JWT token
3. Send request body with empty couponCode
4. Verify validation error is returned

**Request:**
```json
{
  "couponCode": ""
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:00Z",
    "traceId": "abc-123-def-456",
    "errorCode": "VALIDATION_ERROR",
    "message": "Validation failed for one or more fields",
    "details": [
      {
        "field": "couponCode",
        "issue": "Coupon code must not be blank"
      }
    ]
  }
  ```

**Assertions:**
- ✓ Status code is 400
- ✓ Error response has correct structure
- ✓ Error code is VALIDATION_ERROR
- ✓ Error details mention couponCode field

---

### TC-AC-003: Apply Coupon - Invalid Coupon Code

**Test Case ID:** TC-AC-003  
**Endpoint:** POST /api/cart/{cartId}/coupon  
**Scenario:** Attempt to apply non-existent coupon code  
**Priority:** High  
**Test Type:** Negative (Business Logic)  

**Preconditions:**
- Valid JWT token is available
- Cart exists with ID 12345
- Coupon "INVALID123" does not exist in database

**Test Steps:**
1. Send POST request to /api/cart/12345/coupon
2. Include valid JWT token
3. Send request body with non-existent couponCode
4. Verify error is returned

**Request:**
```json
{
  "couponCode": "INVALID123"
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:00Z",
    "traceId": "abc-123-def-789",
    "errorCode": "INVALID_COUPON",
    "message": "Invalid or expired coupon code",
    "details": [
      {
        "field": "couponCode",
        "issue": "Coupon code does not exist or has expired"
      }
    ]
  }
  ```

**Assertions:**
- ✓ Status code is 400
- ✓ Error message indicates invalid coupon
- ✓ Error code is INVALID_COUPON

---

### TC-AC-004: Apply Coupon - Expired Coupon

**Test Case ID:** TC-AC-004  
**Endpoint:** POST /api/cart/{cartId}/coupon  
**Scenario:** Attempt to apply expired coupon code  
**Priority:** High  
**Test Type:** Negative (Business Logic)  

**Preconditions:**
- Valid JWT token is available
- Cart exists with ID 12345
- Coupon "EXPIRED20" exists but expiry date is in the past

**Test Steps:**
1. Send POST request to /api/cart/12345/coupon
2. Include valid JWT token
3. Send request body with expired couponCode
4. Verify error is returned

**Request:**
```json
{
  "couponCode": "EXPIRED20"
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Error message indicates coupon has expired

**Assertions:**
- ✓ Status code is 400
- ✓ Error message includes "expired"

---

### TC-AC-005: Apply Coupon - Unauthorized

**Test Case ID:** TC-AC-005  
**Endpoint:** POST /api/cart/{cartId}/coupon  
**Scenario:** Attempt to apply coupon without authentication  
**Priority:** Critical  
**Test Type:** Negative (Security)  

**Preconditions:**
- Cart exists with ID 12345
- No authentication token provided

**Test Steps:**
1. Send POST request to /api/cart/12345/coupon
2. Do NOT include Authorization header
3. Send valid request body
4. Verify unauthorized error is returned

**Request:**
```json
{
  "couponCode": "SAVE20"
}
```

**Expected Result:**
- Status Code: 401 Unauthorized
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:00Z",
    "traceId": "abc-123-def-456",
    "errorCode": "UNAUTHORIZED",
    "message": "Authentication token is missing or invalid",
    "details": []
  }
  ```

**Assertions:**
- ✓ Status code is 401
- ✓ Error indicates authentication failure

---

### TC-AC-006: Apply Coupon - Cart Not Found

**Test Case ID:** TC-AC-006  
**Endpoint:** POST /api/cart/{cartId}/coupon  
**Scenario:** Attempt to apply coupon to non-existent cart  
**Priority:** High  
**Test Type:** Negative (Resource Not Found)  

**Preconditions:**
- Valid JWT token is available
- Cart with ID 999999 does not exist

**Test Steps:**
1. Send POST request to /api/cart/999999/coupon
2. Include valid JWT token
3. Send valid request body
4. Verify not found error is returned

**Request:**
```json
{
  "couponCode": "SAVE20"
}
```

**Expected Result:**
- Status Code: 404 Not Found
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:00Z",
    "traceId": "abc-123-def-999",
    "errorCode": "CART_NOT_FOUND",
    "message": "Cart not found",
    "details": [
      {
        "field": "cartId",
        "issue": "No cart exists with the provided ID"
      }
    ]
  }
  ```

**Assertions:**
- ✓ Status code is 404
- ✓ Error code is CART_NOT_FOUND
- ✓ Error message indicates cart not found

---

### TC-AC-007: Apply Coupon - Not Applicable to Cart Items

**Test Case ID:** TC-AC-007  
**Endpoint:** POST /api/cart/{cartId}/coupon  
**Scenario:** Attempt to apply coupon that is not applicable to cart items  
**Priority:** High  
**Test Type:** Negative (Business Logic)  

**Preconditions:**
- Valid JWT token is available
- Cart exists with ID 12345
- Cart contains clothing items
- Coupon "ELECTRONICS10" is only applicable to electronics

**Test Steps:**
1. Send POST request to /api/cart/12345/coupon
2. Include valid JWT token
3. Send request body with coupon not applicable to cart items
4. Verify error is returned

**Request:**
```json
{
  "couponCode": "ELECTRONICS10"
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:00Z",
    "traceId": "abc-123-def-789",
    "errorCode": "COUPON_NOT_APPLICABLE",
    "message": "Coupon is not applicable to items in cart",
    "details": [
      {
        "field": "couponCode",
        "issue": "This coupon cannot be applied to the current cart items"
      }
    ]
  }
  ```

**Assertions:**
- ✓ Status code is 400
- ✓ Error code is COUPON_NOT_APPLICABLE
- ✓ Error message indicates coupon not applicable

---

## 3. Remove Coupon Tests

### TC-RC-001: Remove Coupon - Positive

**Test Case ID:** TC-RC-001  
**Endpoint:** DELETE /api/cart/{cartId}/coupon  
**Scenario:** Remove applied coupon from cart  
**Priority:** Critical  
**Test Type:** Positive  

**Preconditions:**
- Valid JWT token is available
- Cart exists with ID 12345
- Coupon "SAVE20" is already applied to the cart
- Cart has discount applied

**Test Steps:**
1. Send DELETE request to /api/cart/12345/coupon
2. Include valid JWT token in Authorization header
3. Verify response status code is 200
4. Verify response time is < 500ms
5. Verify coupon is removed
6. Verify discount is 0
7. Verify total equals subtotal

**Expected Result:**
- Status Code: 200 OK
- Response Time: < 500ms
- Response Body:
  ```json
  {
    "cartId": 12345,
    "userId": 67890,
    "items": [
      {
        "productId": 101,
        "productName": "Laptop",
        "quantity": 1,
        "price": 1000.00
      }
    ],
    "subtotal": 1000.00,
    "couponCode": null,
    "discount": 0.00,
    "total": 1000.00,
    "appliedAt": null
  }
  ```

**Assertions:**
- ✓ Status code is 200
- ✓ Response time < 500ms
- ✓ Response has CartResponse structure
- ✓ couponCode is null
- ✓ discount is 0
- ✓ total equals subtotal
- ✓ appliedAt is null

---

### TC-RC-002: Remove Coupon - Idempotent (No Coupon Applied)

**Test Case ID:** TC-RC-002  
**Endpoint:** DELETE /api/cart/{cartId}/coupon  
**Scenario:** Remove coupon when no coupon is applied (idempotent operation)  
**Priority:** Medium  
**Test Type:** Positive (Edge Case)  

**Preconditions:**
- Valid JWT token is available
- Cart exists with ID 12345
- No coupon is currently applied to the cart

**Test Steps:**
1. Send DELETE request to /api/cart/12345/coupon
2. Include valid JWT token
3. Verify operation succeeds
4. Verify response indicates no coupon applied

**Expected Result:**
- Status Code: 200 OK
- Response Body shows couponCode as null and discount as 0
- Operation is idempotent (safe to call multiple times)

**Assertions:**
- ✓ Status code is 200
- ✓ Operation is idempotent
- ✓ couponCode is null
- ✓ discount is 0

---

### TC-RC-003: Remove Coupon - Unauthorized

**Test Case ID:** TC-RC-003  
**Endpoint:** DELETE /api/cart/{cartId}/coupon  
**Scenario:** Attempt to remove coupon without authentication  
**Priority:** Critical  
**Test Type:** Negative (Security)  

**Preconditions:**
- Cart exists with ID 12345
- No authentication token provided

**Test Steps:**
1. Send DELETE request to /api/cart/12345/coupon
2. Do NOT include Authorization header
3. Verify unauthorized error is returned

**Expected Result:**
- Status Code: 401 Unauthorized
- Error indicates authentication failure

**Assertions:**
- ✓ Status code is 401
- ✓ Error indicates authentication failure

---

### TC-RC-004: Remove Coupon - Cart Not Found

**Test Case ID:** TC-RC-004  
**Endpoint:** DELETE /api/cart/{cartId}/coupon  
**Scenario:** Attempt to remove coupon from non-existent cart  
**Priority:** High  
**Test Type:** Negative (Resource Not Found)  

**Preconditions:**
- Valid JWT token is available
- Cart with ID 999999 does not exist

**Test Steps:**
1. Send DELETE request to /api/cart/999999/coupon
2. Include valid JWT token
3. Verify not found error is returned

**Expected Result:**
- Status Code: 404 Not Found
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:00Z",
    "traceId": "abc-123-def-999",
    "errorCode": "CART_NOT_FOUND",
    "message": "Cart not found",
    "details": []
  }
  ```

**Assertions:**
- ✓ Status code is 404
- ✓ Error code is CART_NOT_FOUND

---

## 4. Validate Coupon Tests

### TC-VC-001: Validate Valid Coupon - Positive

**Test Case ID:** TC-VC-001  
**Endpoint:** POST /api/coupon/validate  
**Scenario:** Validate a valid coupon code against cart  
**Priority:** High  
**Test Type:** Positive  

**Preconditions:**
- Valid JWT token is available
- Cart exists with ID 12345
- Coupon "SAVE20" exists and is valid
- Coupon is applicable to cart items

**Test Steps:**
1. Send POST request to /api/coupon/validate
2. Include valid JWT token
3. Send request body with valid couponCode and cartId
4. Verify response indicates coupon is valid
5. Verify response time is < 500ms

**Request:**
```json
{
  "couponCode": "SAVE20",
  "cartId": 12345
}
```

**Expected Result:**
- Status Code: 200 OK
- Response Time: < 500ms
- Response Body:
  ```json
  {
    "valid": true,
    "couponCode": "SAVE20",
    "discountType": "FIXED",
    "discountValue": 200.00,
    "expiryDate": "2024-12-31",
    "applicableItems": [101, 102, 103],
    "message": "Coupon is valid and applicable"
  }
  ```

**Assertions:**
- ✓ Status code is 200
- ✓ Response time < 500ms
- ✓ Response has CouponValidationResponse structure
- ✓ valid is true
- ✓ discountType is FIXED or PERCENTAGE
- ✓ discountValue > 0

---

### TC-VC-002: Validate Invalid Coupon

**Test Case ID:** TC-VC-002  
**Endpoint:** POST /api/coupon/validate  
**Scenario:** Validate a non-existent coupon code  
**Priority:** High  
**Test Type:** Negative (Business Logic)  

**Preconditions:**
- Valid JWT token is available
- Cart exists with ID 12345
- Coupon "INVALID123" does not exist

**Test Steps:**
1. Send POST request to /api/coupon/validate
2. Include valid JWT token
3. Send request body with non-existent couponCode
4. Verify response indicates coupon is invalid

**Request:**
```json
{
  "couponCode": "INVALID123",
  "cartId": 12345
}
```

**Expected Result:**
- Status Code: 200 OK
- Response Body:
  ```json
  {
    "valid": false,
    "couponCode": "INVALID123",
    "discountType": null,
    "discountValue": 0.00,
    "expiryDate": null,
    "applicableItems": [],
    "message": "Coupon code does not exist"
  }
  ```

**Assertions:**
- ✓ Status code is 200
- ✓ valid is false
- ✓ Message indicates coupon does not exist

---

### TC-VC-003: Validate Expired Coupon

**Test Case ID:** TC-VC-003  
**Endpoint:** POST /api/coupon/validate  
**Scenario:** Validate an expired coupon code  
**Priority:** High  
**Test Type:** Negative (Business Logic)  

**Preconditions:**
- Valid JWT token is available
- Cart exists with ID 12345
- Coupon "EXPIRED20" exists but expiry date is in the past

**Test Steps:**
1. Send POST request to /api/coupon/validate
2. Include valid JWT token
3. Send request body with expired couponCode
4. Verify response indicates coupon is invalid

**Request:**
```json
{
  "couponCode": "EXPIRED20",
  "cartId": 12345
}
```

**Expected Result:**
- Status Code: 200 OK
- Response Body:
  ```json
  {
    "valid": false,
    "couponCode": "EXPIRED20",
    "discountType": "PERCENTAGE",
    "discountValue": 20.00,
    "expiryDate": "2023-12-31",
    "applicableItems": [],
    "message": "Coupon has expired"
  }
  ```

**Assertions:**
- ✓ Status code is 200
- ✓ valid is false
- ✓ Message indicates coupon has expired

---

### TC-VC-004: Validate Coupon - Missing Required Fields

**Test Case ID:** TC-VC-004  
**Endpoint:** POST /api/coupon/validate  
**Scenario:** Validate coupon without providing cartId  
**Priority:** High  
**Test Type:** Negative (Validation)  

**Preconditions:**
- Valid JWT token is available

**Test Steps:**
1. Send POST request to /api/coupon/validate
2. Include valid JWT token
3. Send request body without cartId
4. Verify validation error is returned

**Request:**
```json
{
  "couponCode": "SAVE20"
}
```

**Expected Result:**
- Status Code: 400 Bad Request
- Error indicates validation failure for missing cartId

**Assertions:**
- ✓ Status code is 400
- ✓ Error indicates validation failure

---

### TC-VC-005: Validate Coupon - Unauthorized

**Test Case ID:** TC-VC-005  
**Endpoint:** POST /api/coupon/validate  
**Scenario:** Attempt to validate coupon without authentication  
**Priority:** Critical  
**Test Type:** Negative (Security)  

**Preconditions:**
- No authentication token provided

**Test Steps:**
1. Send POST request to /api/coupon/validate
2. Do NOT include Authorization header
3. Send valid request body
4. Verify unauthorized error is returned

**Request:**
```json
{
  "couponCode": "SAVE20",
  "cartId": 12345
}
```

**Expected Result:**
- Status Code: 401 Unauthorized

**Assertions:**
- ✓ Status code is 401

---

### TC-VC-006: Validate Coupon - Cart Not Found

**Test Case ID:** TC-VC-006  
**Endpoint:** POST /api/coupon/validate  
**Scenario:** Validate coupon for non-existent cart  
**Priority:** High  
**Test Type:** Negative (Resource Not Found)  

**Preconditions:**
- Valid JWT token is available
- Cart with ID 999999 does not exist

**Test Steps:**
1. Send POST request to /api/coupon/validate
2. Include valid JWT token
3. Send request body with non-existent cartId
4. Verify not found error is returned

**Request:**
```json
{
  "couponCode": "SAVE20",
  "cartId": 999999
}
```

**Expected Result:**
- Status Code: 404 Not Found
- Response Body:
  ```json
  {
    "timestamp": "2024-01-15T10:30:00Z",
    "traceId": "abc-123-def-999",
    "errorCode": "CART_NOT_FOUND",
    "message": "Cart not found",
    "details": []
  }
  ```

**Assertions:**
- ✓ Status code is 404
- ✓ Error code is CART_NOT_FOUND

---

## Test Execution Instructions

### Prerequisites
1. Install Postman or Newman CLI
2. Import collection: `test/postman/cart_coupon_collection.json`
3. Import environment: `test/postman/environment.json`
4. Ensure application is running on http://localhost:8080
5. Update environment variables:
   - `auth_token`: Valid JWT token
   - `cartId`: Existing cart ID

### Running Tests with Postman
1. Open Postman
2. Import collection and environment
3. Select "Cart Coupon Service Environment"
4. Run collection using Collection Runner
5. Review test results

### Running Tests with Newman
```bash
# Install Newman
npm install -g newman

# Run collection
newman run test/postman/cart_coupon_collection.json \
  -e test/postman/environment.json \
  --reporters cli,html \
  --reporter-html-export test/reports/newman-report.html
```

### Test Data Setup

**Required Test Data:**

1. **Valid Coupons:**
   - SAVE20 (Fixed discount: $200)
   - PERCENT10 (Percentage discount: 10%)

2. **Invalid/Expired Coupons:**
   - INVALID123 (Does not exist)
   - EXPIRED20 (Expired on 2023-12-31)
   - ELECTRONICS10 (Only for electronics, not applicable to test cart)

3. **Test Cart:**
   - Cart ID: 12345
   - User ID: 67890
   - Items: Laptop ($1000)

4. **Authentication:**
   - Valid JWT token with user claims

---

## Test Coverage Matrix

| Requirement | Test Cases | Coverage |
|-------------|------------|----------|
| Apply valid coupon | TC-AC-001 | ✓ |
| Remove applied coupon | TC-RC-001 | ✓ |
| Validate coupon | TC-VC-001 | ✓ |
| Empty coupon validation | TC-AC-002 | ✓ |
| Invalid coupon handling | TC-AC-003, TC-VC-002 | ✓ |
| Expired coupon handling | TC-AC-004, TC-VC-003 | ✓ |
| Authentication required | TC-AC-005, TC-RC-003, TC-VC-005 | ✓ |
| Cart not found handling | TC-AC-006, TC-RC-004, TC-VC-006 | ✓ |
| Coupon applicability | TC-AC-007 | ✓ |
| Idempotent operations | TC-RC-002 | ✓ |
| Response time < 500ms | TC-AC-001, TC-RC-001, TC-VC-001 | ✓ |
| Discount calculation | TC-AC-001 | ✓ |
| Total recalculation | TC-AC-001, TC-RC-001 | ✓ |

---

## Non-Functional Test Cases

### Performance Tests

**NFT-001: Response Time Validation**
- All API calls must complete within 500ms
- Tested in: TC-AC-001, TC-RC-001, TC-VC-001
- Performance target: < 500ms

**NFT-002: Concurrent User Load**
- System must handle 10,000 concurrent users
- Load testing required (not covered in this suite)
- Tool: JMeter or Gatling

### Security Tests

**NFT-003: Authentication Enforcement**
- All endpoints require valid JWT token
- Tested in: TC-AC-005, TC-RC-003, TC-VC-005
- Unauthorized access returns 401

**NFT-004: Input Validation**
- All inputs are validated
- Tested in: TC-AC-002, TC-VC-004
- Invalid inputs return 400 with details

### Data Integrity Tests

**NFT-005: Discount Calculation Accuracy**
- Discount must be calculated correctly
- Total = Subtotal - Discount
- Tested in: TC-AC-001

**NFT-006: Idempotency**
- Remove coupon operation is idempotent
- Tested in: TC-RC-002
- Safe to call multiple times

---

## Edge Cases and Boundary Tests

### Edge Case Tests

**EC-001: Maximum Discount Value**
- Test coupon with discount > cart total
- Expected: Discount capped at cart total

**EC-002: Minimum Cart Value**
- Test coupon on cart with $0 value
- Expected: Coupon not applicable

**EC-003: Multiple Coupon Attempts**
- Apply coupon when one is already applied
- Expected: Replace existing coupon

**EC-004: Concurrent Coupon Operations**
- Apply and remove coupon simultaneously
- Expected: Last operation wins

---

## Test Environment Configuration

### Local Development Environment
- **Base URL:** http://localhost:8080/api
- **Database:** H2 In-Memory
- **Authentication:** JWT (mock tokens for testing)

### Staging Environment
- **Base URL:** https://staging-api.example.com/v1
- **Database:** MySQL (staging)
- **Authentication:** JWT (staging auth service)

### Production Environment
- **Base URL:** https://api.example.com/v1
- **Database:** MySQL (production)
- **Authentication:** JWT (production auth service)

---

## Test Execution Schedule

| Phase | Tests | Frequency |
|-------|-------|----------|
| Unit Tests | All | On every commit |
| Integration Tests | API Tests | On every PR |
| Regression Tests | Full Suite | Daily |
| Performance Tests | Load Tests | Weekly |
| Security Tests | Security Suite | Monthly |

---

## Defect Tracking

### Defect Template

**Defect ID:** DEF-XXXX  
**Test Case ID:** TC-XX-XXX  
**Severity:** Critical/High/Medium/Low  
**Status:** Open/In Progress/Resolved/Closed  

**Description:**  
[Detailed description of the defect]

**Steps to Reproduce:**  
1. Step 1
2. Step 2
3. Step 3

**Expected Result:**  
[What should happen]

**Actual Result:**  
[What actually happened]

**Environment:**  
[Test environment details]

**Attachments:**  
[Screenshots, logs, etc.]

---

## Test Metrics

### Key Metrics to Track

1. **Test Coverage:** 100% of functional requirements
2. **Pass Rate:** Target > 95%
3. **Defect Density:** < 5 defects per 1000 lines of code
4. **Response Time:** < 500ms for all endpoints
5. **Availability:** 99.9% uptime

### Success Criteria

- ✓ All positive test cases pass
- ✓ All negative test cases return appropriate errors
- ✓ Response times meet performance targets
- ✓ Security tests pass (authentication enforced)
- ✓ No critical or high severity defects
- ✓ Code coverage > 80%

---

## Appendix

### A. Test Data

**Coupon Test Data:**
```sql
INSERT INTO coupon (code, discount_type, discount_value, expiry_date) VALUES
('SAVE20', 'FIXED', 200.00, '2024-12-31'),
('PERCENT10', 'PERCENTAGE', 10.00, '2024-12-31'),
('EXPIRED20', 'PERCENTAGE', 20.00, '2023-12-31'),
('ELECTRONICS10', 'PERCENTAGE', 10.00, '2024-12-31');
```

**Cart Test Data:**
```sql
INSERT INTO cart (id, user_id, total) VALUES
(12345, 67890, 1000.00);

INSERT INTO cart_item (cart_id, product_id, quantity, price) VALUES
(12345, 101, 1, 1000.00);
```

### B. API Response Schemas

Refer to OpenAPI specification: `api/openapi_cart_coupon_service.yaml`

### C. Glossary

- **JWT:** JSON Web Token for authentication
- **Idempotent:** Operation that can be called multiple times with same result
- **NFR:** Non-Functional Requirement
- **SLA:** Service Level Agreement

---

**Document Version:** 1.0  
**Last Updated:** 2024-01-15  
**Author:** QA Automation Team  
**Approved By:** QA Lead
