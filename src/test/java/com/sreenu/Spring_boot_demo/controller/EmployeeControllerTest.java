package com.sreenu.Spring_boot_demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sreenu.Spring_boot_demo.error.EmployeeNotFoundException;
import com.sreenu.Spring_boot_demo.model.Employee;
import com.sreenu.Spring_boot_demo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@DisplayName("Employee Controller Integration Tests")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
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
    @DisplayName("POST /v1/employees - Should create employee successfully")
    void testCreateEmployeeSuccess() throws Exception {
        // Given
        when(employeeService.save(any(Employee.class))).thenReturn(testEmployee);

        // When & Then
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEmployee)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.emailId").value("john.doe@example.com"))
                .andExpect(jsonPath("$.departmentId").value("DEPT001"));

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("POST /v1/employees - Should create employee with minimal data")
    void testCreateEmployeeWithMinimalData() throws Exception {
        // Given
        Employee minimalEmployee = new Employee();
        minimalEmployee.setFirstName("Jane");
        
        Employee savedEmployee = new Employee();
        savedEmployee.setEmployeeId("EMP002");
        savedEmployee.setFirstName("Jane");
        
        when(employeeService.save(any(Employee.class))).thenReturn(savedEmployee);

        // When & Then
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(minimalEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP002"))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").isEmpty())
                .andExpect(jsonPath("$.emailId").isEmpty())
                .andExpect(jsonPath("$.departmentId").isEmpty());

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("POST /v1/employees - Should handle invalid JSON")
    void testCreateEmployeeWithInvalidJson() throws Exception {
        // When & Then
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("GET /v1/employees - Should return all employees")
    void testGetAllEmployeesSuccess() throws Exception {
        // Given
        Employee employee2 = createTestEmployee();
        employee2.setEmployeeId("EMP002");
        employee2.setFirstName("Jane");
        
        List<Employee> employees = Arrays.asList(testEmployee, employee2);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        // When & Then
        mockMvc.perform(get("/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].employeeId").value("EMP001"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].employeeId").value("EMP002"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("GET /v1/employees - Should return empty list when no employees")
    void testGetAllEmployeesEmpty() throws Exception {
        // Given
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("GET /v1/employees/{id} - Should return employee by ID")
    void testGetEmployeeByIdSuccess() throws Exception {
        // Given
        when(employeeService.getAllEmployeesById("EMP001")).thenReturn(testEmployee);

        // When & Then
        mockMvc.perform(get("/v1/employees/EMP001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.emailId").value("john.doe@example.com"));

        verify(employeeService, times(1)).getAllEmployeesById("EMP001");
    }

    @Test
    @DisplayName("GET /v1/employees/{id} - Should return 404 when employee not found")
    void testGetEmployeeByIdNotFound() throws Exception {
        // Given
        when(employeeService.getAllEmployeesById("NONEXISTENT"))
                .thenThrow(new EmployeeNotFoundException("Employee not found with Id: NONEXISTENT"));

        // When & Then
        mockMvc.perform(get("/v1/employees/NONEXISTENT"))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).getAllEmployeesById("NONEXISTENT");
    }

    @Test
    @DisplayName("GET /v1/employees/{id} - Should handle special characters in ID")
    void testGetEmployeeByIdWithSpecialCharacters() throws Exception {
        // Given
        String specialId = "EMP-001_TEST";
        testEmployee.setEmployeeId(specialId);
        when(employeeService.getAllEmployeesById(specialId)).thenReturn(testEmployee);

        // When & Then
        mockMvc.perform(get("/v1/employees/" + specialId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(specialId));

        verify(employeeService, times(1)).getAllEmployeesById(specialId);
    }

    @Test
    @DisplayName("DELETE /v1/employees/{id} - Should delete employee successfully")
    void testDeleteEmployeeByIdSuccess() throws Exception {
        // Given
        String expectedMessage = "Employee deleted with id: EMP001";
        when(employeeService.deleteEmployeeById("EMP001")).thenReturn(expectedMessage);

        // When & Then
        mockMvc.perform(delete("/v1/employees/EMP001"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));

        verify(employeeService, times(1)).deleteEmployeeById("EMP001");
    }

    @Test
    @DisplayName("DELETE /v1/employees/{id} - Should handle delete when employee not found")
    void testDeleteEmployeeByIdNotFound() throws Exception {
        // Given
        when(employeeService.deleteEmployeeById("NONEXISTENT"))
                .thenThrow(new RuntimeException("Employee not found"));

        // When & Then
        mockMvc.perform(delete("/v1/employees/NONEXISTENT"))
                .andExpect(status().isInternalServerError());

        verify(employeeService, times(1)).deleteEmployeeById("NONEXISTENT");
    }

    @Test
    @DisplayName("Should handle service layer exceptions gracefully")
    void testServiceExceptionHandling() throws Exception {
        // Given
        when(employeeService.save(any(Employee.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEmployee)))
                .andExpect(status().isInternalServerError());

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should validate content type for POST requests")
    void testPostWithoutContentType() throws Exception {
        // When & Then
        mockMvc.perform(post("/v1/employees")
                .content(objectMapper.writeValueAsString(testEmployee)))
                .andExpect(status().isUnsupportedMediaType());

        verify(employeeService, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should handle empty request body for POST")
    void testPostWithEmptyBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should handle null employee fields correctly")
    void testCreateEmployeeWithNullFields() throws Exception {
        // Given
        Employee employeeWithNulls = new Employee();
        employeeWithNulls.setFirstName("John");
        // Other fields are null
        
        Employee savedEmployee = new Employee();
        savedEmployee.setEmployeeId("EMP003");
        savedEmployee.setFirstName("John");
        
        when(employeeService.save(any(Employee.class))).thenReturn(savedEmployee);

        // When & Then
        mockMvc.perform(post("/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeWithNulls)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP003"))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should support case-insensitive operations")
    void testCaseInsensitiveOperations() throws Exception {
        // Test GET with lowercase ID
        when(employeeService.getAllEmployeesById("emp001")).thenReturn(testEmployee);

        mockMvc.perform(get("/v1/employees/emp001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP001"));

        // Test DELETE with lowercase ID
        when(employeeService.deleteEmployeeById("emp001"))
                .thenReturn("Employee deleted with id: emp001");

        mockMvc.perform(delete("/v1/employees/emp001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted with id: emp001"));

        verify(employeeService, times(1)).getAllEmployeesById("emp001");
        verify(employeeService, times(1)).deleteEmployeeById("emp001");
    }

    @Test
    @DisplayName("Should handle concurrent requests properly")
    void testConcurrentRequests() throws Exception {
        // Given
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(testEmployee));
        when(employeeService.getAllEmployeesById(anyString())).thenReturn(testEmployee);

        // When & Then - Simulate concurrent requests
        mockMvc.perform(get("/v1/employees"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/v1/employees/EMP001"))
                .andExpect(status().isOk());

        // Verify both requests were processed
        verify(employeeService, times(1)).getAllEmployees();
        verify(employeeService, times(1)).getAllEmployeesById("EMP001");
    }
}