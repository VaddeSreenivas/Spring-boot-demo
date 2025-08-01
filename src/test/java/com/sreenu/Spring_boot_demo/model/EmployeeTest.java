package com.sreenu.Spring_boot_demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Employee Model Tests")
class EmployeeTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
    }

    @Test
    @DisplayName("Should set and get employee ID correctly")
    void testEmployeeIdGetterSetter() {
        // Given
        String expectedId = "EMP001";
        
        // When
        employee.setEmployeeId(expectedId);
        
        // Then
        assertEquals(expectedId, employee.getEmployeeId());
    }

    @Test
    @DisplayName("Should set and get first name correctly")
    void testFirstNameGetterSetter() {
        // Given
        String expectedFirstName = "John";
        
        // When
        employee.setFirstName(expectedFirstName);
        
        // Then
        assertEquals(expectedFirstName, employee.getFirstName());
    }

    @Test
    @DisplayName("Should set and get last name correctly")
    void testLastNameGetterSetter() {
        // Given
        String expectedLastName = "Doe";
        
        // When
        employee.setLastName(expectedLastName);
        
        // Then
        assertEquals(expectedLastName, employee.getLastName());
    }

    @Test
    @DisplayName("Should set and get email ID correctly")
    void testEmailIdGetterSetter() {
        // Given
        String expectedEmail = "john.doe@example.com";
        
        // When
        employee.setEmailId(expectedEmail);
        
        // Then
        assertEquals(expectedEmail, employee.getEmailId());
    }

    @Test
    @DisplayName("Should set and get department ID correctly")
    void testDepartmentIdGetterSetter() {
        // Given
        String expectedDepartmentId = "DEPT001";
        
        // When
        employee.setDepartmentId(expectedDepartmentId);
        
        // Then
        assertEquals(expectedDepartmentId, employee.getDepartmentId());
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // When setting null values
        employee.setEmployeeId(null);
        employee.setFirstName(null);
        employee.setLastName(null);
        employee.setEmailId(null);
        employee.setDepartmentId(null);
        
        // Then all should be null
        assertNull(employee.getEmployeeId());
        assertNull(employee.getFirstName());
        assertNull(employee.getLastName());
        assertNull(employee.getEmailId());
        assertNull(employee.getDepartmentId());
    }

    @Test
    @DisplayName("Should handle empty strings correctly")
    void testEmptyStrings() {
        // When setting empty strings
        employee.setEmployeeId("");
        employee.setFirstName("");
        employee.setLastName("");
        employee.setEmailId("");
        employee.setDepartmentId("");
        
        // Then all should be empty strings
        assertEquals("", employee.getEmployeeId());
        assertEquals("", employee.getFirstName());
        assertEquals("", employee.getLastName());
        assertEquals("", employee.getEmailId());
        assertEquals("", employee.getDepartmentId());
    }

    @Test
    @DisplayName("Should create employee with all fields populated")
    void testCompleteEmployeeCreation() {
        // Given
        String employeeId = "EMP001";
        String firstName = "John";
        String lastName = "Doe";
        String emailId = "john.doe@example.com";
        String departmentId = "DEPT001";
        
        // When
        employee.setEmployeeId(employeeId);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailId(emailId);
        employee.setDepartmentId(departmentId);
        
        // Then
        assertAll("Employee should have all fields set correctly",
            () -> assertEquals(employeeId, employee.getEmployeeId()),
            () -> assertEquals(firstName, employee.getFirstName()),
            () -> assertEquals(lastName, employee.getLastName()),
            () -> assertEquals(emailId, employee.getEmailId()),
            () -> assertEquals(departmentId, employee.getDepartmentId())
        );
    }

    @Test
    @DisplayName("Should handle special characters in fields")
    void testSpecialCharacters() {
        // Given
        String specialName = "José-María O'Connor";
        String specialEmail = "jose.maria+test@example.com";
        
        // When
        employee.setFirstName(specialName);
        employee.setEmailId(specialEmail);
        
        // Then
        assertEquals(specialName, employee.getFirstName());
        assertEquals(specialEmail, employee.getEmailId());
    }

    @Test
    @DisplayName("Should handle long strings")
    void testLongStrings() {
        // Given
        String longString = "a".repeat(1000);
        
        // When
        employee.setFirstName(longString);
        
        // Then
        assertEquals(longString, employee.getFirstName());
        assertEquals(1000, employee.getFirstName().length());
    }
}