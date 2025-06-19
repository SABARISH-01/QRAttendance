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

    // View all employees
    @GetMapping("/employees")
    public String viewEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("baseUrl", "https://qrattendance-6dxo.onrender.com/mark?token=");
        return "employee-list";
    }

    // Show form to add new employee
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-form";
    }

    // Add new employee
    @PostMapping("/add")
    public String addEmployee(@ModelAttribute Employee employee) {
        String token = employee.getEmpId() + "_" + System.currentTimeMillis();
        employee.setToken(token);
        employeeRepository.save(employee);
        return "redirect:/admin/employees";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String empId, Model model) {
        Employee employee = employeeRepository.findById(empId).orElse(null);
        if (employee == null) {
            return "redirect:/admin/employees";
        }
        model.addAttribute("employee", employee);
        return "employee-form";
    }

    // Update employee
    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee employee) {
        Employee existing = employeeRepository.findById(employee.getEmpId()).orElse(null);
        if (existing != null) {
            existing.setName(employee.getName());
            employeeRepository.save(existing);
        }
        return "redirect:/admin/employees";
    }

    // Delete employee
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") String empId) {
        if (employeeRepository.existsById(empId)) {
            employeeRepository.deleteById(empId);
        }
        return "redirect:/admin/employees";
    }
}
