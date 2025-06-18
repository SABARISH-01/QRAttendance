package com.attendance.demo.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.attendance.demo.Entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
    
}


