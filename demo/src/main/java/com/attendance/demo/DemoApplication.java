package com.attendance.demo;

import com.google.zxing.WriterException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String empId = "EMP001";
        String token = "abc123";
        String qrText = "http://localhost:8080/mark?empId=" + empId + "&token=" + token;
        String filePath = "C:/Users/Admin/Desktop/qr_emp001.png";

        try {
            QRCodeGenerator.generateQRCodeImage(qrText, 250, 250, filePath);
            System.out.println("✅ QR Code generated at: " + filePath);
        } catch (WriterException | IOException e) {
            System.err.println("❌ Could not generate QR Code: " + e.getMessage());
        }
    }
}
