package com.attendance.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.attendance.demo.Entity.Attendance;
import com.attendance.demo.Entity.Employee;
import com.attendance.demo.Repository.AttendanceRepository;
import com.attendance.demo.Repository.EmployeeRepository;

@Controller
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    // http://localhost:8080/mark?token=abcXYZ
    @GetMapping("/mark")
    public String markAttendance(@RequestParam String token, Model model) {
    Employee employee = employeeRepo.findByToken(token);

    if (employee == null) {
        return "invalid"; // Create a simple error page too
    }

    Attendance entry = new Attendance();
    entry.setEmpId(employee.getEmpId());
    entry.setToken(token);
    entry.setTimestamp(LocalDateTime.now());

    attendanceRepo.save(entry);

    model.addAttribute("name", employee.getName());
    model.addAttribute("empId", employee.getEmpId());
    model.addAttribute("time", LocalDateTime.now());

    return "success";
}


    // Admin view
    @GetMapping("/admin/attendance")
    public String viewAttendance(Model model) {
        List<Attendance> logs = attendanceRepo.findAll();
        model.addAttribute("logs", logs);
        return "attendance_logs"; // thymeleaf template
    }
}
