package com.dashboard.service;

import com.dashboard.model.DataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service for generating sample real-time data
 * This simulates various data sources for demonstration purposes
 */
@Service
public class DataGeneratorService {
    
    @Autowired
    private DataPointService dataPointService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    private final Random random = new Random();
    
    private final String[] categories = {
        "Sales", "Website Traffic", "Server Performance", "User Engagement", 
        "Revenue", "Orders", "CPU Usage", "Memory Usage", "Network Traffic"
    };
    
    private final String[] sources = {
        "Web Analytics", "Sales System", "Monitoring", "CRM", "E-commerce Platform"
    };
    
    /**
     * Generate sample data every 5 seconds
     */
    @Scheduled(fixedRate = 5000)
    public void generateSampleData() {
        try {
            // Generate 1-3 random data points
            int count = ThreadLocalRandom.current().nextInt(1, 4);
            
            for (int i = 0; i < count; i++) {
                DataPoint dataPoint = generateRandomDataPoint();
                dataPointService.saveDataPoint(dataPoint);
            }
            
            // Send system status update
            sendSystemStatusUpdate();
            
        } catch (Exception e) {
            System.err.println("Error generating sample data: " + e.getMessage());
        }
    }
    
    /**
     * Generate website traffic data every 10 seconds
     */
    @Scheduled(fixedRate = 10000)
    public void generateWebsiteTrafficData() {
        try {
            // Page views
            DataPoint pageViews = new DataPoint(
                "Website Traffic",
                (double) ThreadLocalRandom.current().nextInt(100, 1000),
                "Page Views",
                "Web Analytics",
                "Number of page views in the last minute",
                "views/min"
            );
            
            // Unique visitors
            DataPoint uniqueVisitors = new DataPoint(
                "Website Traffic",
                (double) ThreadLocalRandom.current().nextInt(50, 300),
                "Unique Visitors",
                "Web Analytics",
                "Number of unique visitors in the last minute",
                "visitors/min"
            );
            
            // Bounce rate (percentage)
            DataPoint bounceRate = new DataPoint(
                "Website Traffic",
                ThreadLocalRandom.current().nextDouble(20.0, 80.0),
                "Bounce Rate",
                "Web Analytics",
                "Percentage of single-page sessions",
                "%"
            );
            
            dataPointService.saveDataPoint(pageViews);
            dataPointService.saveDataPoint(uniqueVisitors);
            dataPointService.saveDataPoint(bounceRate);
            
        } catch (Exception e) {
            System.err.println("Error generating website traffic data: " + e.getMessage());
        }
    }
    
    /**
     * Generate server performance data every 15 seconds
     */
    @Scheduled(fixedRate = 15000)
    public void generateServerPerformanceData() {
        try {
            // CPU Usage
            DataPoint cpuUsage = new DataPoint(
                "Server Performance",
                ThreadLocalRandom.current().nextDouble(10.0, 95.0),
                "CPU Usage",
                "Monitoring",
                "Current CPU utilization percentage",
                "%"
            );
            
            // Memory Usage
            DataPoint memoryUsage = new DataPoint(
                "Server Performance",
                ThreadLocalRandom.current().nextDouble(30.0, 85.0),
                "Memory Usage",
                "Monitoring",
                "Current memory utilization percentage",
                "%"
            );
            
            // Response Time
            DataPoint responseTime = new DataPoint(
                "Server Performance",
                ThreadLocalRandom.current().nextDouble(50.0, 500.0),
                "Response Time",
                "Monitoring",
                "Average response time for API calls",
                "ms"
            );
            
            dataPointService.saveDataPoint(cpuUsage);
            dataPointService.saveDataPoint(memoryUsage);
            dataPointService.saveDataPoint(responseTime);
            
        } catch (Exception e) {
            System.err.println("Error generating server performance data: " + e.getMessage());
        }
    }
    
    /**
     * Generate sales data every 30 seconds
     */
    @Scheduled(fixedRate = 30000)
    public void generateSalesData() {
        try {
            // Total Sales
            DataPoint totalSales = new DataPoint(
                "Sales",
                ThreadLocalRandom.current().nextDouble(1000.0, 50000.0),
                "Total Sales",
                "Sales System",
                "Total sales amount in the last hour",
                "$"
            );
            
            // Number of Orders
            DataPoint orders = new DataPoint(
                "Orders",
                (double) ThreadLocalRandom.current().nextInt(5, 100),
                "New Orders",
                "E-commerce Platform",
                "Number of new orders in the last hour",
                "orders"
            );
            
            // Conversion Rate
            DataPoint conversionRate = new DataPoint(
                "User Engagement",
                ThreadLocalRandom.current().nextDouble(1.0, 15.0),
                "Conversion Rate",
                "CRM",
                "Percentage of visitors who made a purchase",
                "%"
            );
            
            dataPointService.saveDataPoint(totalSales);
            dataPointService.saveDataPoint(orders);
            dataPointService.saveDataPoint(conversionRate);
            
        } catch (Exception e) {
            System.err.println("Error generating sales data: " + e.getMessage());
        }
    }
    
    /**
     * Generate a random data point
     */
    private DataPoint generateRandomDataPoint() {
        String category = categories[random.nextInt(categories.length)];
        String source = sources[random.nextInt(sources.length)];
        
        DataPoint dataPoint = new DataPoint();
        dataPoint.setCategory(category);
        dataPoint.setSource(source);
        dataPoint.setTimestamp(LocalDateTime.now());
        
        // Generate appropriate data based on category
        switch (category) {
            case "Sales":
                dataPoint.setValue(ThreadLocalRandom.current().nextDouble(100.0, 10000.0));
                dataPoint.setLabel("Sales Amount");
                dataPoint.setUnit("$");
                break;
            case "Website Traffic":
                dataPoint.setValue((double) ThreadLocalRandom.current().nextInt(10, 500));
                dataPoint.setLabel("Visitors");
                dataPoint.setUnit("users");
                break;
            case "Server Performance":
                dataPoint.setValue(ThreadLocalRandom.current().nextDouble(0.0, 100.0));
                dataPoint.setLabel("Resource Usage");
                dataPoint.setUnit("%");
                break;
            case "User Engagement":
                dataPoint.setValue(ThreadLocalRandom.current().nextDouble(0.0, 100.0));
                dataPoint.setLabel("Engagement Score");
                dataPoint.setUnit("%");
                break;
            default:
                dataPoint.setValue(ThreadLocalRandom.current().nextDouble(1.0, 1000.0));
                dataPoint.setLabel("Generic Metric");
                dataPoint.setUnit("units");
        }
        
        dataPoint.setDescription("Auto-generated sample data for " + category);
        
        return dataPoint;
    }
    
    /**
     * Send system status update via WebSocket
     */
    private void sendSystemStatusUpdate() {
        try {
            String status = "{"
                + "\"timestamp\": \"" + LocalDateTime.now() + "\","
                + "\"status\": \"active\","
                + "\"dataGeneration\": true,"
                + "\"activeConnections\": " + ThreadLocalRandom.current().nextInt(10, 100)
                + "}";
                
            messagingTemplate.convertAndSend("/topic/system/status", status);
        } catch (Exception e) {
            System.err.println("Error sending system status: " + e.getMessage());
        }
    }
    
    /**
     * Generate burst of data for testing (manual trigger)
     */
    public void generateDataBurst(int count) {
        for (int i = 0; i < count; i++) {
            DataPoint dataPoint = generateRandomDataPoint();
            dataPointService.saveDataPoint(dataPoint);
        }
    }
}