package com.myproject.controllers;

import com.myproject.models.dtos.HealthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HealthController.
 */
@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    @InjectMocks
    private HealthController healthController;

    @Test
    void testHealthCheck_ReturnsOkStatus() {
        // When
        ResponseEntity<HealthResponse> response = healthController.healthCheck();
        
        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().getStatus());
        assertEquals("myproject", response.getBody().getService());
        assertEquals("1.0.0", response.getBody().getVersion());
        assertNotNull(response.getBody().getTimestamp());
    }
}
