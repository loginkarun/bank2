package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error detail DTO for field-level validation errors.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {
    
    private String field;
    
    private String issue;
}
