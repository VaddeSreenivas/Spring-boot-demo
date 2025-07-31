package com.sreenu.Spring_boot_demo.repository;

import com.sreenu.Spring_boot_demo.entity.EmployeeEntity;
import com.sreenu.Spring_boot_demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,String> {
}
