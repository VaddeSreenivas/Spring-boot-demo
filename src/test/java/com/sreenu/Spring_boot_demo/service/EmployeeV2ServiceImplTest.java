package com.sreenu.Spring_boot_demo.service;

import com.sreenu.Spring_boot_demo.entity.EmployeeEntity;
import com.sreenu.Spring_boot_demo.model.Employee;
import com.sreenu.Spring_boot_demo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Employee V2 Service Implementation Tests")
class EmployeeV2ServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeV2ServiceImpl employeeV2Service;

    private Employee testEmployee;
    private EmployeeEntity testEmployeeEntity;

    @BeforeEach
    void setUp() {
        testEmployee = createTestEmployee();
        testEmployeeEntity = createTestEmployeeEntity();
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

    private EmployeeEntity createTestEmployeeEntity() {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setEmployeeId("EMP001");
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setEmailId("john.doe@example.com");
        entity.setDepartmentId("DEPT001");
        return entity;
    }

    @Test
    @DisplayName("Should save employee with existing ID")
    void testSaveEmployeeWithExistingId() {
        // Given
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(testEmployeeEntity);

        // When
        Employee savedEmployee = employeeV2Service.save(testEmployee);

        // Then
        assertNotNull(savedEmployee);
        assertEquals("EMP001", savedEmployee.getEmployeeId());
        assertEquals("John", savedEmployee.getFirstName());
        assertEquals("Doe", savedEmployee.getLastName());
        assertEquals("john.doe@example.com", savedEmployee.getEmailId());
        assertEquals("DEPT001", savedEmployee.getDepartmentId());

        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    @DisplayName("Should generate UUID when employee ID is null")
    void testSaveEmployeeWithNullId() {
        // Given
        testEmployee.setEmployeeId(null);
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(testEmployeeEntity);

        // When
        Employee savedEmployee = employeeV2Service.save(testEmployee);

        // Then
        assertNotNull(savedEmployee.getEmployeeId());
        assertFalse(savedEmployee.getEmployeeId().isEmpty());
        assertEquals("John", savedEmployee.getFirstName());

        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    @DisplayName("Should generate UUID when employee ID is empty")
    void testSaveEmployeeWithEmptyId() {
        // Given
        testEmployee.setEmployeeId("");
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(testEmployeeEntity);

        // When
        Employee savedEmployee = employeeV2Service.save(testEmployee);

        // Then
        assertNotNull(savedEmployee.getEmployeeId());
        assertFalse(savedEmployee.getEmployeeId().isEmpty());
        assertEquals("John", savedEmployee.getFirstName());

        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    @DisplayName("Should handle repository save exception")
    void testSaveEmployeeWithRepositoryException() {
        // Given
        when(employeeRepository.save(any(EmployeeEntity.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> employeeV2Service.save(testEmployee)
        );

        assertEquals("Database connection failed", exception.getMessage());
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    @DisplayName("Should save employee with minimal data")
    void testSaveEmployeeWithMinimalData() {
        // Given
        Employee minimalEmployee = new Employee();
        minimalEmployee.setFirstName("Jane");

        EmployeeEntity minimalEntity = new EmployeeEntity();
        minimalEntity.setEmployeeId("EMP002");
        minimalEntity.setFirstName("Jane");

        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(minimalEntity);

        // When
        Employee savedEmployee = employeeV2Service.save(minimalEmployee);

        // Then
        assertNotNull(savedEmployee.getEmployeeId());
        assertEquals("Jane", savedEmployee.getFirstName());
        assertNull(savedEmployee.getLastName());
        assertNull(savedEmployee.getEmailId());
        assertNull(savedEmployee.getDepartmentId());

        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    @DisplayName("Should return all employees successfully")
    void testGetAllEmployeesSuccess() {
        // Given
        EmployeeEntity entity2 = createTestEmployeeEntity();
        entity2.setEmployeeId("EMP002");
        entity2.setFirstName("Jane");

        List<EmployeeEntity> entities = Arrays.asList(testEmployeeEntity, entity2);
        when(employeeRepository.findAll()).thenReturn(entities);

        // When
        List<Employee> employees = employeeV2Service.getAllEmployees();

        // Then
        assertNotNull(employees);
        assertEquals(2, employees.size());
        
        Employee emp1 = employees.get(0);
        assertEquals("EMP001", emp1.getEmployeeId());
        assertEquals("John", emp1.getFirstName());
        
        Employee emp2 = employees.get(1);
        assertEquals("EMP002", emp2.getEmployeeId());
        assertEquals("Jane", emp2.getFirstName());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no employees exist")
    void testGetAllEmployeesEmpty() {
        // Given
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Employee> employees = employeeV2Service.getAllEmployees();

        // Then
        assertNotNull(employees);
        assertTrue(employees.isEmpty());
        assertEquals(0, employees.size());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle repository findAll exception")
    void testGetAllEmployeesWithRepositoryException() {
        // Given
        when(employeeRepository.findAll())
                .thenThrow(new RuntimeException("Database query failed"));

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> employeeV2Service.getAllEmployees()
        );

        assertEquals("Database query failed", exception.getMessage());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find employee by ID successfully")
    void testGetEmployeeByIdSuccess() {
        // Given
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(testEmployeeEntity));

        // When
        Employee foundEmployee = employeeV2Service.getAllEmployeesById("EMP001");

        // Then
        assertNotNull(foundEmployee);
        assertEquals("EMP001", foundEmployee.getEmployeeId());
        assertEquals("John", foundEmployee.getFirstName());
        assertEquals("Doe", foundEmployee.getLastName());
        assertEquals("john.doe@example.com", foundEmployee.getEmailId());
        assertEquals("DEPT001", foundEmployee.getDepartmentId());

        verify(employeeRepository, times(1)).findById("EMP001");
    }

    @Test
    @DisplayName("Should throw exception when employee not found by ID")
    void testGetEmployeeByIdNotFound() {
        // Given
        when(employeeRepository.findById("NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        NoSuchElementException exception = assertThrows(
            NoSuchElementException.class,
            () -> employeeV2Service.getAllEmployeesById("NONEXISTENT")
        );

        verify(employeeRepository, times(1)).findById("NONEXISTENT");
    }

    @Test
    @DisplayName("Should handle repository findById exception")
    void testGetEmployeeByIdWithRepositoryException() {
        // Given
        when(employeeRepository.findById("EMP001"))
                .thenThrow(new RuntimeException("Database connection lost"));

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> employeeV2Service.getAllEmployeesById("EMP001")
        );

        assertEquals("Database connection lost", exception.getMessage());
        verify(employeeRepository, times(1)).findById("EMP001");
    }

    @Test
    @DisplayName("Should delete employee by ID successfully")
    void testDeleteEmployeeByIdSuccess() {
        // Given
        doNothing().when(employeeRepository).deleteById("EMP001");

        // When
        String result = employeeV2Service.deleteEmployeeById("EMP001");

        // Then
        assertEquals("Employee deleted with id: EMP001", result);
        verify(employeeRepository, times(1)).deleteById("EMP001");
    }

    @Test
    @DisplayName("Should handle repository deleteById exception")
    void testDeleteEmployeeByIdWithRepositoryException() {
        // Given
        doThrow(new RuntimeException("Delete operation failed"))
                .when(employeeRepository).deleteById("EMP001");

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> employeeV2Service.deleteEmployeeById("EMP001")
        );

        assertEquals("Delete operation failed", exception.getMessage());
        verify(employeeRepository, times(1)).deleteById("EMP001");
    }

    @Test
    @DisplayName("Should delete non-existent employee without error")
    void testDeleteNonExistentEmployee() {
        // Given
        doNothing().when(employeeRepository).deleteById("NONEXISTENT");

        // When
        String result = employeeV2Service.deleteEmployeeById("NONEXISTENT");

        // Then
        assertEquals("Employee deleted with id: NONEXISTENT", result);
        verify(employeeRepository, times(1)).deleteById("NONEXISTENT");
    }

    @Test
    @DisplayName("Should handle null and empty parameters gracefully")
    void testNullAndEmptyParameters() {
        // Test save with null employee
        assertThrows(NullPointerException.class, () -> employeeV2Service.save(null));

        // Test findById with null ID
        when(employeeRepository.findById(null)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> employeeV2Service.getAllEmployeesById(null));

        // Test deleteById with null ID
        doNothing().when(employeeRepository).deleteById(null);
        String result = employeeV2Service.deleteEmployeeById(null);
        assertEquals("Employee deleted with id: null", result);
    }

    @Test
    @DisplayName("Should properly copy properties between Employee and EmployeeEntity")
    void testPropertyCopyingIntegrity() {
        // Given
        Employee employeeWithAllFields = new Employee();
        employeeWithAllFields.setEmployeeId("EMP999");
        employeeWithAllFields.setFirstName("Alice");
        employeeWithAllFields.setLastName("Smith");
        employeeWithAllFields.setEmailId("alice.smith@test.com");
        employeeWithAllFields.setDepartmentId("DEPT999");

        EmployeeEntity savedEntity = new EmployeeEntity();
        savedEntity.setEmployeeId("EMP999");
        savedEntity.setFirstName("Alice");
        savedEntity.setLastName("Smith");
        savedEntity.setEmailId("alice.smith@test.com");
        savedEntity.setDepartmentId("DEPT999");

        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(savedEntity);

        // When
        Employee result = employeeV2Service.save(employeeWithAllFields);

        // Then
        assertEquals(employeeWithAllFields.getEmployeeId(), result.getEmployeeId());
        assertEquals(employeeWithAllFields.getFirstName(), result.getFirstName());
        assertEquals(employeeWithAllFields.getLastName(), result.getLastName());
        assertEquals(employeeWithAllFields.getEmailId(), result.getEmailId());
        assertEquals(employeeWithAllFields.getDepartmentId(), result.getDepartmentId());

        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    @DisplayName("Should handle special characters in employee data")
    void testSpecialCharactersInEmployeeData() {
        // Given
        Employee specialEmployee = new Employee();
        specialEmployee.setEmployeeId("EMP-SPECIAL_001");
        specialEmployee.setFirstName("José-María");
        specialEmployee.setLastName("O'Connor");
        specialEmployee.setEmailId("jose.maria+test@example.com");
        specialEmployee.setDepartmentId("DEPT-001_SPECIAL");

        EmployeeEntity specialEntity = new EmployeeEntity();
        specialEntity.setEmployeeId("EMP-SPECIAL_001");
        specialEntity.setFirstName("José-María");
        specialEntity.setLastName("O'Connor");
        specialEntity.setEmailId("jose.maria+test@example.com");
        specialEntity.setDepartmentId("DEPT-001_SPECIAL");

        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(specialEntity);

        // When
        Employee result = employeeV2Service.save(specialEmployee);

        // Then
        assertEquals("EMP-SPECIAL_001", result.getEmployeeId());
        assertEquals("José-María", result.getFirstName());
        assertEquals("O'Connor", result.getLastName());
        assertEquals("jose.maria+test@example.com", result.getEmailId());
        assertEquals("DEPT-001_SPECIAL", result.getDepartmentId());

        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    @DisplayName("Should handle concurrent operations properly")
    void testConcurrentOperations() {
        // Given
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(testEmployeeEntity));
        when(employeeRepository.findById("EMP001")).thenReturn(Optional.of(testEmployeeEntity));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(testEmployeeEntity);
        doNothing().when(employeeRepository).deleteById("EMP001");

        // When - Simulate concurrent operations
        List<Employee> allEmployees = employeeV2Service.getAllEmployees();
        Employee foundEmployee = employeeV2Service.getAllEmployeesById("EMP001");
        Employee savedEmployee = employeeV2Service.save(testEmployee);
        String deleteResult = employeeV2Service.deleteEmployeeById("EMP001");

        // Then - All operations should complete successfully
        assertNotNull(allEmployees);
        assertEquals(1, allEmployees.size());
        assertNotNull(foundEmployee);
        assertNotNull(savedEmployee);
        assertEquals("Employee deleted with id: EMP001", deleteResult);

        // Verify all repository methods were called
        verify(employeeRepository, times(1)).findAll();
        verify(employeeRepository, times(1)).findById("EMP001");
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
        verify(employeeRepository, times(1)).deleteById("EMP001");
    }
}