package com.sreenu.Spring_boot_demo.controller;

import com.sreenu.Spring_boot_demo.model.Employee;
import com.sreenu.Spring_boot_demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/employees")
public class EmployeeV2Controller {
    @Autowired
    @Qualifier("employeeV2ServiceImpl")
    public EmployeeService employeeService;

    @PostMapping
    public Employee save(@RequestBody Employee employee){
        return employeeService.save(employee);
    }
    @GetMapping
    public List<Employee> getAllEmpoyees(){
        return employeeService.getAllEmployees();
    }
    @GetMapping("/{id}")
    public Employee getAllEmployeesById(@PathVariable String id){
        return employeeService.getAllEmployeesById(id);
    }
    @DeleteMapping("/{id}")
    public String deleteEmployeeById(@PathVariable String id){
        return employeeService.deleteEmployeeById(id);
    }
}
