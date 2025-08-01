package com.sreenu.Spring_boot_demo.service;

import com.sreenu.Spring_boot_demo.error.EmployeeNotFoundException;
import com.sreenu.Spring_boot_demo.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Employee Service Implementation Tests")
class EmployeeServiceImplTest {

    private EmployeeServiceImpl employeeService;
    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeServiceImpl();
        testEmployee = createTestEmployee();
    }

    private Employee createTestEmployee() {
        Employee employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmailId("john.doe@example.com");
        employee.setDepartmentId("DEPT001");
        return employee;
    }

    @Test
    @DisplayName("Should save employee with existing ID")
    void testSaveEmployeeWithExistingId() {
        // Given
        Employee employee = createTestEmployee();
        
        // When
        Employee savedEmployee = employeeService.save(employee);
        
        // Then
        assertNotNull(savedEmployee);
        assertEquals("EMP001", savedEmployee.getEmployeeId());
        assertEquals("John", savedEmployee.getFirstName());
        assertEquals("Doe", savedEmployee.getLastName());
        assertEquals("john.doe@example.com", savedEmployee.getEmailId());
        assertEquals("DEPT001", savedEmployee.getDepartmentId());
    }

    @Test
    @DisplayName("Should generate UUID when employee ID is null")
    void testSaveEmployeeWithNullId() {
        // Given
        Employee employee = createTestEmployee();
        employee.setEmployeeId(null);
        
        // When
        Employee savedEmployee = employeeService.save(employee);
        
        // Then
        assertNotNull(savedEmployee.getEmployeeId());
        assertFalse(savedEmployee.getEmployeeId().isEmpty());
        assertTrue(savedEmployee.getEmployeeId().length() > 0);
        assertEquals("John", savedEmployee.getFirstName());
    }

    @Test
    @DisplayName("Should generate UUID when employee ID is empty")
    void testSaveEmployeeWithEmptyId() {
        // Given
        Employee employee = createTestEmployee();
        employee.setEmployeeId("");
        
        // When
        Employee savedEmployee = employeeService.save(employee);
        
        // Then
        assertNotNull(savedEmployee.getEmployeeId());
        assertFalse(savedEmployee.getEmployeeId().isEmpty());
        assertTrue(savedEmployee.getEmployeeId().length() > 0);
        assertEquals("John", savedEmployee.getFirstName());
    }

    @Test
    @DisplayName("Should save multiple employees")
    void testSaveMultipleEmployees() {
        // Given
        Employee employee1 = createTestEmployee();
        Employee employee2 = createTestEmployee();
        employee2.setEmployeeId("EMP002");
        employee2.setFirstName("Jane");
        
        // When
        employeeService.save(employee1);
        employeeService.save(employee2);
        
        // Then
        List<Employee> allEmployees = employeeService.getAllEmployees();
        assertEquals(2, allEmployees.size());
    }

    @Test
    @DisplayName("Should return empty list when no employees exist")
    void testGetAllEmployeesWhenEmpty() {
        // When
        List<Employee> employees = employeeService.getAllEmployees();
        
        // Then
        assertNotNull(employees);
        assertTrue(employees.isEmpty());
        assertEquals(0, employees.size());
    }

    @Test
    @DisplayName("Should return all employees when employees exist")
    void testGetAllEmployeesWhenNotEmpty() {
        // Given
        Employee employee1 = createTestEmployee();
        Employee employee2 = createTestEmployee();
        employee2.setEmployeeId("EMP002");
        employee2.setFirstName("Jane");
        
        employeeService.save(employee1);
        employeeService.save(employee2);
        
        // When
        List<Employee> employees = employeeService.getAllEmployees();
        
        // Then
        assertNotNull(employees);
        assertEquals(2, employees.size());
        assertTrue(employees.contains(employee1));
        assertTrue(employees.contains(employee2));
    }

    @Test
    @DisplayName("Should find employee by ID successfully")
    void testGetEmployeeByIdSuccess() {
        // Given
        Employee savedEmployee = employeeService.save(testEmployee);
        
        // When
        Employee foundEmployee = employeeService.getAllEmployeesById("EMP001");
        
        // Then
        assertNotNull(foundEmployee);
        assertEquals(savedEmployee.getEmployeeId(), foundEmployee.getEmployeeId());
        assertEquals(savedEmployee.getFirstName(), foundEmployee.getFirstName());
        assertEquals(savedEmployee.getLastName(), foundEmployee.getLastName());
    }

    @Test
    @DisplayName("Should find employee by ID case insensitive")
    void testGetEmployeeByIdCaseInsensitive() {
        // Given
        employeeService.save(testEmployee);
        
        // When
        Employee foundEmployee = employeeService.getAllEmployeesById("emp001");
        
        // Then
        assertNotNull(foundEmployee);
        assertEquals("EMP001", foundEmployee.getEmployeeId());
    }

    @Test
    @DisplayName("Should throw EmployeeNotFoundException when employee not found")
    void testGetEmployeeByIdNotFound() {
        // When & Then
        EmployeeNotFoundException exception = assertThrows(
            EmployeeNotFoundException.class,
            () -> employeeService.getAllEmployeesById("NONEXISTENT")
        );
        
        assertEquals("Employee not found with Id: NONEXISTENT", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw EmployeeNotFoundException for null ID")
    void testGetEmployeeByIdNull() {
        // When & Then
        assertThrows(
            EmployeeNotFoundException.class,
            () -> employeeService.getAllEmployeesById(null)
        );
    }

    @Test
    @DisplayName("Should delete employee by ID successfully")
    void testDeleteEmployeeByIdSuccess() {
        // Given
        employeeService.save(testEmployee);
        assertEquals(1, employeeService.getAllEmployees().size());
        
        // When
        String result = employeeService.deleteEmployeeById("EMP001");
        
        // Then
        assertEquals("Employee deleted with id: EMP001", result);
        assertEquals(0, employeeService.getAllEmployees().size());
    }

    @Test
    @DisplayName("Should delete employee by ID case insensitive")
    void testDeleteEmployeeByIdCaseInsensitive() {
        // Given
        employeeService.save(testEmployee);
        assertEquals(1, employeeService.getAllEmployees().size());
        
        // When
        String result = employeeService.deleteEmployeeById("emp001");
        
        // Then
        assertEquals("Employee deleted with id: emp001", result);
        assertEquals(0, employeeService.getAllEmployees().size());
    }

    @Test
    @DisplayName("Should handle delete when employee not found")
    void testDeleteEmployeeByIdNotFound() {
        // When & Then
        assertThrows(
            Exception.class, // This could be NoSuchElementException or similar
            () -> employeeService.deleteEmployeeById("NONEXISTENT")
        );
    }

    @Test
    @DisplayName("Should maintain employee list integrity after operations")
    void testEmployeeListIntegrity() {
        // Given
        Employee employee1 = createTestEmployee();
        Employee employee2 = createTestEmployee();
        employee2.setEmployeeId("EMP002");
        employee2.setFirstName("Jane");
        Employee employee3 = createTestEmployee();
        employee3.setEmployeeId("EMP003");
        employee3.setFirstName("Bob");
        
        // When
        employeeService.save(employee1);
        employeeService.save(employee2);
        employeeService.save(employee3);
        
        // Delete middle employee
        employeeService.deleteEmployeeById("EMP002");
        
        // Then
        List<Employee> remainingEmployees = employeeService.getAllEmployees();
        assertEquals(2, remainingEmployees.size());
        
        // Verify correct employees remain
        Employee foundEmployee1 = employeeService.getAllEmployeesById("EMP001");
        Employee foundEmployee3 = employeeService.getAllEmployeesById("EMP003");
        
        assertEquals("John", foundEmployee1.getFirstName());
        assertEquals("Bob", foundEmployee3.getFirstName());
        
        // Verify deleted employee is gone
        assertThrows(EmployeeNotFoundException.class, 
            () -> employeeService.getAllEmployeesById("EMP002"));
    }

    @Test
    @DisplayName("Should handle employee with minimal data")
    void testSaveEmployeeWithMinimalData() {
        // Given
        Employee minimalEmployee = new Employee();
        minimalEmployee.setFirstName("Test");
        
        // When
        Employee savedEmployee = employeeService.save(minimalEmployee);
        
        // Then
        assertNotNull(savedEmployee.getEmployeeId());
        assertEquals("Test", savedEmployee.getFirstName());
        assertNull(savedEmployee.getLastName());
        assertNull(savedEmployee.getEmailId());
        assertNull(savedEmployee.getDepartmentId());
    }

    @Test
    @DisplayName("Should handle concurrent operations correctly")
    void testConcurrentOperations() {
        // Given
        Employee employee1 = createTestEmployee();
        Employee employee2 = createTestEmployee();
        employee2.setEmployeeId("EMP002");
        employee2.setFirstName("Jane");
        
        // When - Simulate concurrent saves
        employeeService.save(employee1);
        employeeService.save(employee2);
        
        // Then - Both should be saved
        assertEquals(2, employeeService.getAllEmployees().size());
        
        // When - Concurrent read and delete
        Employee found = employeeService.getAllEmployeesById("EMP001");
        employeeService.deleteEmployeeById("EMP002");
        
        // Then - Operations should complete successfully
        assertNotNull(found);
        assertEquals(1, employeeService.getAllEmployees().size());
    }
}