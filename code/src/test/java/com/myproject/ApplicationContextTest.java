package com.myproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Application context test to verify Spring Boot application starts correctly.
 */
@SpringBootTest
@ActiveProfiles("test")
class ApplicationContextTest {

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
        assertTrue(true, "Application context should load without errors");
    }
    
    @Test
    void applicationStarts() {
        // Verify the application can start
        assertTrue(true, "Application should start successfully");
    }
}
