package com.sreenu.Spring_boot_demo.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Employee Not Found Exception Tests")
class EmployeeNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void testExceptionWithMessage() {
        // Given
        String expectedMessage = "Employee not found with Id: EMP001";
        
        // When
        EmployeeNotFoundException exception = new EmployeeNotFoundException(expectedMessage);
        
        // Then
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should create exception with null message")
    void testExceptionWithNullMessage() {
        // When
        EmployeeNotFoundException exception = new EmployeeNotFoundException(null);
        
        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Should create exception with empty message")
    void testExceptionWithEmptyMessage() {
        // Given
        String emptyMessage = "";
        
        // When
        EmployeeNotFoundException exception = new EmployeeNotFoundException(emptyMessage);
        
        // Then
        assertNotNull(exception);
        assertEquals(emptyMessage, exception.getMessage());
        assertTrue(exception.getMessage().isEmpty());
    }

    @Test
    @DisplayName("Should be throwable as RuntimeException")
    void testExceptionIsThrowable() {
        // Given
        String message = "Test employee not found";
        
        // When & Then
        assertThrows(EmployeeNotFoundException.class, () -> {
            throw new EmployeeNotFoundException(message);
        });
    }

    @Test
    @DisplayName("Should maintain inheritance hierarchy")
    void testInheritanceHierarchy() {
        // Given
        EmployeeNotFoundException exception = new EmployeeNotFoundException("Test message");
        
        // Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }

    @Test
    @DisplayName("Should handle special characters in message")
    void testExceptionWithSpecialCharacters() {
        // Given
        String specialMessage = "Employee not found with special chars: àáâãäåæçèéêë & symbols !@#$%^&*()";
        
        // When
        EmployeeNotFoundException exception = new EmployeeNotFoundException(specialMessage);
        
        // Then
        assertEquals(specialMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Should handle long messages")
    void testExceptionWithLongMessage() {
        // Given
        String longMessage = "Employee not found: " + "A".repeat(1000);
        
        // When
        EmployeeNotFoundException exception = new EmployeeNotFoundException(longMessage);
        
        // Then
        assertEquals(longMessage, exception.getMessage());
        assertTrue(exception.getMessage().length() > 1000);
    }

    @Test
    @DisplayName("Should support standard exception operations")
    void testStandardExceptionOperations() {
        // Given
        String message = "Employee not found with Id: EMP999";
        EmployeeNotFoundException exception = new EmployeeNotFoundException(message);
        
        // When & Then
        assertNotNull(exception.toString());
        assertTrue(exception.toString().contains("EmployeeNotFoundException"));
        assertTrue(exception.toString().contains(message));
        
        // Stack trace should be available
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }
}