package com.dashboard.config;

import com.dashboard.model.DataPoint;
import com.dashboard.service.DataPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Initializes the database with test data on application startup
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DataPointService dataPointService;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("========================================");
        System.out.println("  Loading Initial Test Dataset...");
        System.out.println("========================================");

        List<DataPoint> initialData = new ArrayList<>();

        // Generate historical data for the last 24 hours
        LocalDateTime now = LocalDateTime.now();
        
        // Sales data - last 24 hours (hourly)
        for (int i = 24; i >= 0; i--) {
            LocalDateTime timestamp = now.minusHours(i);
            
            initialData.add(createDataPoint(
                "Sales",
                "Hourly Revenue",
                5000 + random.nextDouble() * 15000,
                "$",
                "E-commerce Platform",
                "Total sales revenue for the hour",
                timestamp
            ));
            
            initialData.add(createDataPoint(
                "Orders",
                "Order Count",
                (double) (50 + random.nextInt(150)),
                "orders",
                "E-commerce Platform",
                "Number of orders placed",
                timestamp
            ));
        }

        // Website Traffic - last 24 hours (every 2 hours)
        for (int i = 24; i >= 0; i -= 2) {
            LocalDateTime timestamp = now.minusHours(i);
            
            initialData.add(createDataPoint(
                "Website Traffic",
                "Page Views",
                (double) (5000 + random.nextInt(10000)),
                "views",
                "Web Analytics",
                "Total page views",
                timestamp
            ));
            
            initialData.add(createDataPoint(
                "Website Traffic",
                "Unique Visitors",
                (double) (2000 + random.nextInt(5000)),
                "visitors",
                "Web Analytics",
                "Unique visitor count",
                timestamp
            ));
            
            initialData.add(createDataPoint(
                "Website Traffic",
                "Bounce Rate",
                30 + random.nextDouble() * 40,
                "%",
                "Web Analytics",
                "Percentage of single-page sessions",
                timestamp
            ));
        }

        // Server Performance - last 24 hours (every hour)
        for (int i = 24; i >= 0; i--) {
            LocalDateTime timestamp = now.minusHours(i);
            
            initialData.add(createDataPoint(
                "Server Performance",
                "CPU Usage",
                20 + random.nextDouble() * 60,
                "%",
                "Monitoring",
                "CPU utilization percentage",
                timestamp
            ));
            
            initialData.add(createDataPoint(
                "Server Performance",
                "Memory Usage",
                40 + random.nextDouble() * 40,
                "%",
                "Monitoring",
                "Memory utilization percentage",
                timestamp
            ));
            
            initialData.add(createDataPoint(
                "Server Performance",
                "Response Time",
                50 + random.nextDouble() * 200,
                "ms",
                "Monitoring",
                "Average API response time",
                timestamp
            ));
        }

        // User Engagement - last 7 days (daily)
        for (int i = 7; i >= 0; i--) {
            LocalDateTime timestamp = now.minusDays(i);
            
            initialData.add(createDataPoint(
                "User Engagement",
                "Active Users",
                (double) (10000 + random.nextInt(20000)),
                "users",
                "CRM",
                "Daily active users",
                timestamp
            ));
            
            initialData.add(createDataPoint(
                "User Engagement",
                "Session Duration",
                5 + random.nextDouble() * 10,
                "minutes",
                "CRM",
                "Average session duration",
                timestamp
            ));
            
            initialData.add(createDataPoint(
                "User Engagement",
                "Conversion Rate",
                2 + random.nextDouble() * 8,
                "%",
                "CRM",
                "User conversion rate",
                timestamp
            ));
        }

        // Network Traffic - last 24 hours (every 3 hours)
        for (int i = 24; i >= 0; i -= 3) {
            LocalDateTime timestamp = now.minusHours(i);
            
            initialData.add(createDataPoint(
                "Network Traffic",
                "Bandwidth Usage",
                100 + random.nextDouble() * 900,
                "Mbps",
                "Network Monitoring",
                "Network bandwidth utilization",
                timestamp
            ));
            
            initialData.add(createDataPoint(
                "Network Traffic",
                "Request Count",
                (double) (50000 + random.nextInt(150000)),
                "requests",
                "Network Monitoring",
                "Total HTTP requests",
                timestamp
            ));
        }

        // Revenue by Product Category - last 7 days
        String[] productCategories = {"Electronics", "Clothing", "Books", "Home & Garden", "Sports"};
        for (int i = 7; i >= 0; i--) {
            LocalDateTime timestamp = now.minusDays(i);
            
            for (String category : productCategories) {
                initialData.add(createDataPoint(
                    "Revenue",
                    category + " Sales",
                    10000 + random.nextDouble() * 50000,
                    "$",
                    "Sales System",
                    "Revenue from " + category,
                    timestamp
                ));
            }
        }

        // Customer Satisfaction - last 30 days (every 3 days)
        for (int i = 30; i >= 0; i -= 3) {
            LocalDateTime timestamp = now.minusDays(i);
            
            initialData.add(createDataPoint(
                "Customer Satisfaction",
                "NPS Score",
                50 + random.nextDouble() * 40,
                "score",
                "Customer Feedback",
                "Net Promoter Score",
                timestamp
            ));
            
            initialData.add(createDataPoint(
                "Customer Satisfaction",
                "Support Tickets",
                (double) (100 + random.nextInt(400)),
                "tickets",
                "Support System",
                "Daily support tickets",
                timestamp
            ));
        }

        // Save all initial data
        System.out.println("  Inserting " + initialData.size() + " test data points...");
        dataPointService.saveDataPoints(initialData);
        
        System.out.println("  ✓ Initial test dataset loaded successfully!");
        System.out.println("  ✓ Data spans: Last 30 days");
        System.out.println("  ✓ Categories: " + getUniqueCategories(initialData).size());
        System.out.println("========================================");
    }

    private DataPoint createDataPoint(String category, String label, Double value, 
                                     String unit, String source, String description, 
                                     LocalDateTime timestamp) {
        DataPoint dataPoint = new DataPoint();
        dataPoint.setCategory(category);
        dataPoint.setLabel(label);
        dataPoint.setValue(value);
        dataPoint.setUnit(unit);
        dataPoint.setSource(source);
        dataPoint.setDescription(description);
        dataPoint.setTimestamp(timestamp);
        return dataPoint;
    }

    private java.util.Set<String> getUniqueCategories(List<DataPoint> dataPoints) {
        return dataPoints.stream()
                .map(DataPoint::getCategory)
                .collect(java.util.stream.Collectors.toSet());
    }
}
