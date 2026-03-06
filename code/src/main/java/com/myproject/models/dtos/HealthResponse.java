package com.myproject.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Health response DTO for health check endpoint.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthResponse {
    
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String service;
    
    private String version;
}
