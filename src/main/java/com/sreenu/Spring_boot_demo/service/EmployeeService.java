package com.sreenu.Spring_boot_demo.service;

import com.sreenu.Spring_boot_demo.model.Employee;

import java.util.List;

public interface EmployeeService {

     Employee save(Employee employee);

    List<Employee> getAllEmployees();

    Employee getAllEmployeesById(String id);

    String deleteEmployeeById(String id);
}
