package com.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Real-Time Data Visualization Dashboard
 * 
 * This application provides:
 * - Real-time data collection and processing
 * - Interactive web-based dashboard with multiple chart types
 * - WebSocket communication for live updates
 * - RESTful APIs for data management
 * - Database integration for data persistence
 * 
 * @author Aniket Mane
 * @version 1.0
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class DashboardApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DashboardApplication.class, args);
        System.out.println("=======================================================");
        System.out.println("  Real-Time Data Visualization Dashboard Started!");
        System.out.println("  Access the dashboard at: http://localhost:8080");
        System.out.println("  API Documentation: http://localhost:8080/api/docs");
        System.out.println("=======================================================");
    }
}