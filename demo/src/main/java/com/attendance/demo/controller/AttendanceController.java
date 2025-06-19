package com.attendance.demo.controller;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.attendance.demo.Entity.Attendance;
import com.attendance.demo.Entity.Employee;
import com.attendance.demo.Repository.AttendanceRepository;
import com.attendance.demo.Repository.EmployeeRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping("/mark")
    public String markAttendance(@RequestParam String token, Model model) {
        Employee employee = employeeRepo.findByToken(token);

        if (employee == null) {
            return "invalid";
        }

        ZonedDateTime istTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        Attendance entry = new Attendance();
        entry.setEmpId(employee.getEmpId());
        entry.setEmpName(employee.getName()); 
        entry.setToken(token);
        entry.setTimestamp(istTime.toLocalDateTime());

        attendanceRepo.save(entry);

        model.addAttribute("name", employee.getName());
        model.addAttribute("empId", employee.getEmpId());
        model.addAttribute("time", istTime.toLocalDateTime());

        return "attendance_marked";
    }

    @GetMapping("/admin/attendance")
    public String viewAttendance(Model model) {
        List<Attendance> logs = attendanceRepo.findAll();

        List<Map<String, Object>> formattedLogs = logs.stream().map(log -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id", log.getId());
            row.put("empId", log.getEmpId());
            row.put("token", log.getToken());

            Employee emp = employeeRepo.findById(log.getEmpId()).orElse(null);
            row.put("name", emp != null ? emp.getName() : "Unknown");

            LocalDateTime timestamp = log.getTimestamp();
            row.put("date", timestamp.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            row.put("time", timestamp.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));

            return row;
        }).toList();

        model.addAttribute("logs", formattedLogs);
        return "attendance_logs";
    }

    @GetMapping("/admin/attendance/download")
    public void downloadAttendanceCsv(HttpServletResponse response) throws Exception {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=attendance_logs.csv");

        List<Attendance> logs = attendanceRepo.findAll();

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Employee ID,Name,Token,Date,Time");

            for (Attendance log : logs) {
                String empId = log.getEmpId();
                String token = log.getToken();
                LocalDateTime time = log.getTimestamp();

                Employee emp = employeeRepo.findById(empId).orElse(null);
                String name = emp != null ? emp.getName() : "Unknown";

                String date = time.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String timePart = time.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));

                writer.printf("%s,%s,%s,%s,%s%n", empId, name, token, date, timePart);
            }
        }
    }

    @PostMapping("/admin/attendance/delete/{id}")
public String deleteAttendance(@PathVariable("id") Long id) {
    attendanceRepo.deleteById(id);
    return "redirect:/admin/attendance";
}



}
