package com.attendance.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.attendance.demo.Entity.Employee;
import com.attendance.demo.Repository.EmployeeRepository;

@Controller
@RequestMapping("/admin")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees")
public String viewEmployees(Model model) {
    model.addAttribute("employees", employeeRepository.findAll());
    model.addAttribute("baseUrl", "https://qrattendance-6dxo.onrender.com/mark");
    return "employee-list";
}



    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-form";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee) {
        // Generate unique token using empId + timestamp
        String token = employee.getEmpId() + "_" + System.currentTimeMillis();
        employee.setToken(token);

        employeeRepository.save(employee);
        return "redirect:/admin/employees";
    }
}
