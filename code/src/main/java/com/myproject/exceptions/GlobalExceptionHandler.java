package com.myproject.exceptions;

import com.myproject.models.dtos.ErrorResponse;
import com.myproject.models.dtos.ErrorDetail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Global exception handler for the application.
 * Handles validation errors and other exceptions with standardized error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        List<ErrorDetail> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            ErrorDetail detail = new ErrorDetail();
            detail.setField(error.getField());
            detail.setIssue(error.getDefaultMessage());
            details.add(detail);
        }
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setTraceId(UUID.randomUUID().toString());
        errorResponse.setErrorCode("VALIDATION_ERROR");
        errorResponse.setMessage("Validation failed for one or more fields");
        errorResponse.setDetails(details);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        
        List<ErrorDetail> details = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            ErrorDetail detail = new ErrorDetail();
            detail.setField(violation.getPropertyPath().toString());
            detail.setIssue(violation.getMessage());
            details.add(detail);
        }
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setTraceId(UUID.randomUUID().toString());
        errorResponse.setErrorCode("CONSTRAINT_VIOLATION");
        errorResponse.setMessage("Constraint violation occurred");
        errorResponse.setDetails(details);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setTraceId(UUID.randomUUID().toString());
        errorResponse.setErrorCode("RESOURCE_NOT_FOUND");
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setDetails(new ArrayList<>());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setTraceId(UUID.randomUUID().toString());
        errorResponse.setErrorCode("INTERNAL_SERVER_ERROR");
        errorResponse.setMessage("An unexpected error occurred: " + ex.getMessage());
        errorResponse.setDetails(new ArrayList<>());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
