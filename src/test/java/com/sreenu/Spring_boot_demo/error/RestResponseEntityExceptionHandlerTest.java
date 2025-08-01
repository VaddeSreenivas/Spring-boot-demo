package com.sreenu.Spring_boot_demo.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Rest Response Entity Exception Handler Tests")
class RestResponseEntityExceptionHandlerTest {

    private RestResponseEntityExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new RestResponseEntityExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    @Test
    @DisplayName("Should handle EmployeeNotFoundException correctly")
    void testEmployeeNotFoundHandler() {
        // Given
        String expectedMessage = "Employee not found with Id: EMP001";
        EmployeeNotFoundException exception = new EmployeeNotFoundException(expectedMessage);

        // When
        ResponseEntity<ErrorMessage> response = exceptionHandler.employeeNotFoundHandler(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorMessage errorMessage = response.getBody();
        assertNotNull(errorMessage);
        assertEquals(HttpStatus.NOT_FOUND, errorMessage.getStatus());
        assertEquals(expectedMessage, errorMessage.getMessage());
    }

    @Test
    @DisplayName("Should handle EmployeeNotFoundException with null message")
    void testEmployeeNotFoundHandlerWithNullMessage() {
        // Given
        EmployeeNotFoundException exception = new EmployeeNotFoundException(null);

        // When
        ResponseEntity<ErrorMessage> response = exceptionHandler.employeeNotFoundHandler(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorMessage errorMessage = response.getBody();
        assertNotNull(errorMessage);
        assertEquals(HttpStatus.NOT_FOUND, errorMessage.getStatus());
        assertNull(errorMessage.getMessage());
    }

    @Test
    @DisplayName("Should handle EmployeeNotFoundException with empty message")
    void testEmployeeNotFoundHandlerWithEmptyMessage() {
        // Given
        String emptyMessage = "";
        EmployeeNotFoundException exception = new EmployeeNotFoundException(emptyMessage);

        // When
        ResponseEntity<ErrorMessage> response = exceptionHandler.employeeNotFoundHandler(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorMessage errorMessage = response.getBody();
        assertNotNull(errorMessage);
        assertEquals(HttpStatus.NOT_FOUND, errorMessage.getStatus());
        assertEquals(emptyMessage, errorMessage.getMessage());
        assertTrue(errorMessage.getMessage().isEmpty());
    }

    @Test
    @DisplayName("Should handle EmployeeNotFoundException with long message")
    void testEmployeeNotFoundHandlerWithLongMessage() {
        // Given
        String longMessage = "Employee not found: " + "A".repeat(500);
        EmployeeNotFoundException exception = new EmployeeNotFoundException(longMessage);

        // When
        ResponseEntity<ErrorMessage> response = exceptionHandler.employeeNotFoundHandler(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorMessage errorMessage = response.getBody();
        assertNotNull(errorMessage);
        assertEquals(HttpStatus.NOT_FOUND, errorMessage.getStatus());
        assertEquals(longMessage, errorMessage.getMessage());
        assertTrue(errorMessage.getMessage().length() > 500);
    }

    @Test
    @DisplayName("Should handle EmployeeNotFoundException with special characters")
    void testEmployeeNotFoundHandlerWithSpecialCharacters() {
        // Given
        String specialMessage = "Employee not found: José-María O'Connor with ID: EMP-001_SPECIAL!@#$%";
        EmployeeNotFoundException exception = new EmployeeNotFoundException(specialMessage);

        // When
        ResponseEntity<ErrorMessage> response = exceptionHandler.employeeNotFoundHandler(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ErrorMessage errorMessage = response.getBody();
        assertNotNull(errorMessage);
        assertEquals(HttpStatus.NOT_FOUND, errorMessage.getStatus());
        assertEquals(specialMessage, errorMessage.getMessage());
    }

    @Test
    @DisplayName("Should handle multiple EmployeeNotFoundException instances")
    void testMultipleEmployeeNotFoundExceptions() {
        // Given
        EmployeeNotFoundException exception1 = new EmployeeNotFoundException("Employee EMP001 not found");
        EmployeeNotFoundException exception2 = new EmployeeNotFoundException("Employee EMP002 not found");
        EmployeeNotFoundException exception3 = new EmployeeNotFoundException("Employee EMP003 not found");

        // When
        ResponseEntity<ErrorMessage> response1 = exceptionHandler.employeeNotFoundHandler(exception1, webRequest);
        ResponseEntity<ErrorMessage> response2 = exceptionHandler.employeeNotFoundHandler(exception2, webRequest);
        ResponseEntity<ErrorMessage> response3 = exceptionHandler.employeeNotFoundHandler(exception3, webRequest);

        // Then
        assertAll("All responses should be properly formatted",
            () -> {
                assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
                assertEquals("Employee EMP001 not found", response1.getBody().getMessage());
            },
            () -> {
                assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
                assertEquals("Employee EMP002 not found", response2.getBody().getMessage());
            },
            () -> {
                assertEquals(HttpStatus.NOT_FOUND, response3.getStatusCode());
                assertEquals("Employee EMP003 not found", response3.getBody().getMessage());
            }
        );
    }

    @Test
    @DisplayName("Should handle concurrent exception processing")
    void testConcurrentExceptionHandling() {
        // Given
        EmployeeNotFoundException exception1 = new EmployeeNotFoundException("Concurrent test 1");
        EmployeeNotFoundException exception2 = new EmployeeNotFoundException("Concurrent test 2");

        // When - Simulate concurrent exception handling
        ResponseEntity<ErrorMessage> response1 = exceptionHandler.employeeNotFoundHandler(exception1, webRequest);
        ResponseEntity<ErrorMessage> response2 = exceptionHandler.employeeNotFoundHandler(exception2, webRequest);

        // Then
        assertNotNull(response1);
        assertNotNull(response2);
        assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertEquals("Concurrent test 1", response1.getBody().getMessage());
        assertEquals("Concurrent test 2", response2.getBody().getMessage());
    }

    @Test
    @DisplayName("Should maintain thread safety")
    void testThreadSafety() {
        // Given
        EmployeeNotFoundException exception = new EmployeeNotFoundException("Thread safety test");

        // When - Multiple calls to the same handler
        ResponseEntity<ErrorMessage> response1 = exceptionHandler.employeeNotFoundHandler(exception, webRequest);
        ResponseEntity<ErrorMessage> response2 = exceptionHandler.employeeNotFoundHandler(exception, webRequest);
        ResponseEntity<ErrorMessage> response3 = exceptionHandler.employeeNotFoundHandler(exception, webRequest);

        // Then - All responses should be identical and independent
        assertAll("All responses should be consistent",
            () -> assertEquals(response1.getStatusCode(), response2.getStatusCode()),
            () -> assertEquals(response2.getStatusCode(), response3.getStatusCode()),
            () -> assertEquals(response1.getBody().getMessage(), response2.getBody().getMessage()),
            () -> assertEquals(response2.getBody().getMessage(), response3.getBody().getMessage()),
            () -> assertEquals(response1.getBody().getStatus(), response2.getBody().getStatus()),
            () -> assertEquals(response2.getBody().getStatus(), response3.getBody().getStatus())
        );
    }

    @Test
    @DisplayName("Should create proper ErrorMessage structure")
    void testErrorMessageStructure() {
        // Given
        String testMessage = "Test employee not found";
        EmployeeNotFoundException exception = new EmployeeNotFoundException(testMessage);

        // When
        ResponseEntity<ErrorMessage> response = exceptionHandler.employeeNotFoundHandler(exception, webRequest);

        // Then
        ErrorMessage errorMessage = response.getBody();
        assertNotNull(errorMessage);
        
        // Test ErrorMessage properties
        assertEquals(HttpStatus.NOT_FOUND, errorMessage.getStatus());
        assertEquals(testMessage, errorMessage.getMessage());
        
        // Ensure ErrorMessage has proper toString representation
        String errorString = errorMessage.toString();
        assertNotNull(errorString);
        assertTrue(errorString.contains("NOT_FOUND") || errorString.contains("404"));
        assertTrue(errorString.contains(testMessage));
    }
}