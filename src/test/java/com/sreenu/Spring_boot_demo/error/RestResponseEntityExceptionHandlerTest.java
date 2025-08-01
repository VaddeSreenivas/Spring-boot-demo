package com.sreenu.Spring_boot_demo.error;

import com.sreenu.Spring_boot_demo.model.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Rest Response Entity Exception Handler Tests")
class RestResponseEntityExceptionHandlerTest {

    private RestResponseEntityExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new RestResponseEntityExceptionHandler();
    }

    @Test
    @DisplayName("Should handle EmployeeNotFoundException correctly")
    void testEmployeeNotFoundHandler() {
        // Given
        String expectedMessage = "Employee not found with Id: EMP001";
        EmployeeNotFoundException exception = new EmployeeNotFoundException(expectedMessage);

        // When
        ErrorMessage errorMessage = exceptionHandler.employeeNotFoundHandler(exception);

        // Then
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
        ErrorMessage errorMessage = exceptionHandler.employeeNotFoundHandler(exception);

        // Then
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
        ErrorMessage errorMessage = exceptionHandler.employeeNotFoundHandler(exception);

        // Then
        assertNotNull(errorMessage);
        assertEquals(HttpStatus.NOT_FOUND, errorMessage.getStatus());
        assertEquals(emptyMessage, errorMessage.getMessage());
        assertTrue(errorMessage.getMessage().isEmpty());
    }

    @Test
    @DisplayName("Should handle generic Exception correctly")
    void testGenericExceptionHandler() {
        // Given
        String expectedMessage = "Internal server error occurred";
        Exception exception = new RuntimeException(expectedMessage);

        // When
        ErrorMessage errorMessage = exceptionHandler.genericExceptionHandler(exception);

        // Then
        assertNotNull(errorMessage);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage.getStatus());
        assertEquals(expectedMessage, errorMessage.getMessage());
    }

    @Test
    @DisplayName("Should handle EmployeeNotFoundException with special characters")
    void testEmployeeNotFoundHandlerWithSpecialCharacters() {
        // Given
        String specialMessage = "Employee not found: José-María O'Connor with ID: EMP-001_SPECIAL!@#$%";
        EmployeeNotFoundException exception = new EmployeeNotFoundException(specialMessage);

        // When
        ErrorMessage errorMessage = exceptionHandler.employeeNotFoundHandler(exception);

        // Then
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
        ErrorMessage errorMessage1 = exceptionHandler.employeeNotFoundHandler(exception1);
        ErrorMessage errorMessage2 = exceptionHandler.employeeNotFoundHandler(exception2);
        ErrorMessage errorMessage3 = exceptionHandler.employeeNotFoundHandler(exception3);

        // Then
        assertAll("All responses should be properly formatted",
            () -> {
                assertEquals(HttpStatus.NOT_FOUND, errorMessage1.getStatus());
                assertEquals("Employee EMP001 not found", errorMessage1.getMessage());
            },
            () -> {
                assertEquals(HttpStatus.NOT_FOUND, errorMessage2.getStatus());
                assertEquals("Employee EMP002 not found", errorMessage2.getMessage());
            },
            () -> {
                assertEquals(HttpStatus.NOT_FOUND, errorMessage3.getStatus());
                assertEquals("Employee EMP003 not found", errorMessage3.getMessage());
            }
        );
    }

    @Test
    @DisplayName("Should create proper ErrorMessage structure")
    void testErrorMessageStructure() {
        // Given
        String testMessage = "Test employee not found";
        EmployeeNotFoundException exception = new EmployeeNotFoundException(testMessage);

        // When
        ErrorMessage errorMessage = exceptionHandler.employeeNotFoundHandler(exception);

        // Then
        assertNotNull(errorMessage);
        
        // Test ErrorMessage properties
        assertEquals(HttpStatus.NOT_FOUND, errorMessage.getStatus());
        assertEquals(testMessage, errorMessage.getMessage());
        
        // Ensure ErrorMessage has proper toString representation (if implemented)
        assertNotNull(errorMessage.toString());
    }
}