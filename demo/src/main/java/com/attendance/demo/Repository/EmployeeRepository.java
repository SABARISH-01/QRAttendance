package com.attendance.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attendance.demo.Entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,String>{
    Employee findByToken(String token);
}
