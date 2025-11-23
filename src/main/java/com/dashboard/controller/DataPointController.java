package com.dashboard.controller;

import com.dashboard.model.DataPoint;
import com.dashboard.service.DataPointService;
import com.dashboard.service.DataGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for DataPoint operations
 * Provides API endpoints for managing dashboard data
 */
@RestController
@RequestMapping("/api/datapoints")
@CrossOrigin(origins = "*")
public class DataPointController {
    
    @Autowired
    private DataPointService dataPointService;
    
    @Autowired
    private DataGeneratorService dataGeneratorService;
    
    /**
     * Get all data points
     */
    @GetMapping
    public ResponseEntity<List<DataPoint>> getAllDataPoints() {
        List<DataPoint> dataPoints = dataPointService.getAllDataPoints();
        return ResponseEntity.ok(dataPoints);
    }
    
    /**
     * Get data point by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DataPoint> getDataPointById(@PathVariable Long id) {
        Optional<DataPoint> dataPoint = dataPointService.getDataPointById(id);
        return dataPoint.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new data point
     */
    @PostMapping
    public ResponseEntity<DataPoint> createDataPoint(@Valid @RequestBody DataPoint dataPoint) {
        try {
            DataPoint savedDataPoint = dataPointService.saveDataPoint(dataPoint);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDataPoint);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Create multiple data points
     */
    @PostMapping("/batch")
    public ResponseEntity<List<DataPoint>> createDataPoints(@Valid @RequestBody List<DataPoint> dataPoints) {
        try {
            List<DataPoint> savedDataPoints = dataPointService.saveDataPoints(dataPoints);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDataPoints);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Update data point
     */
    @PutMapping("/{id}")
    public ResponseEntity<DataPoint> updateDataPoint(@PathVariable Long id, 
                                                    @Valid @RequestBody DataPoint dataPoint) {
        try {
            DataPoint updatedDataPoint = dataPointService.updateDataPoint(id, dataPoint);
            return ResponseEntity.ok(updatedDataPoint);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Delete data point
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataPoint(@PathVariable Long id) {
        try {
            dataPointService.deleteDataPoint(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get data points by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<DataPoint>> getDataPointsByCategory(@PathVariable String category) {
        List<DataPoint> dataPoints = dataPointService.getDataPointsByCategory(category);
        return ResponseEntity.ok(dataPoints);
    }
    
    /**
     * Get recent data points
     */
    @GetMapping("/recent")
    public ResponseEntity<List<DataPoint>> getRecentDataPoints() {
        List<DataPoint> dataPoints = dataPointService.getRecentDataPoints();
        return ResponseEntity.ok(dataPoints);
    }
    
    /**
     * Get recent data points by category
     */
    @GetMapping("/recent/{category}")
    public ResponseEntity<List<DataPoint>> getRecentDataPointsByCategory(@PathVariable String category) {
        List<DataPoint> dataPoints = dataPointService.getRecentDataPointsByCategory(category);
        return ResponseEntity.ok(dataPoints);
    }
    
    /**
     * Get data points within time range
     */
    @GetMapping("/timerange")
    public ResponseEntity<List<DataPoint>> getDataPointsInTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        List<DataPoint> dataPoints = dataPointService.getDataPointsInTimeRange(startTime, endTime);
        return ResponseEntity.ok(dataPoints);
    }
    
    /**
     * Get data points by category within time range
     */
    @GetMapping("/timerange/{category}")
    public ResponseEntity<List<DataPoint>> getDataPointsByCategoryInTimeRange(
            @PathVariable String category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        List<DataPoint> dataPoints = dataPointService.getDataPointsByCategoryInTimeRange(category, startTime, endTime);
        return ResponseEntity.ok(dataPoints);
    }
    
    /**
     * Get distinct categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getDistinctCategories() {
        List<String> categories = dataPointService.getDistinctCategories();
        return ResponseEntity.ok(categories);
    }
    
    /**
     * Get distinct sources
     */
    @GetMapping("/sources")
    public ResponseEntity<List<String>> getDistinctSources() {
        List<String> sources = dataPointService.getDistinctSources();
        return ResponseEntity.ok(sources);
    }
    
    /**
     * Get aggregated data by category
     */
    @GetMapping("/aggregated")
    public ResponseEntity<List<Object[]>> getAggregatedDataByCategory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime) {
        
        if (startTime == null) {
            startTime = LocalDateTime.now().minusDays(1); // Default to last 24 hours
        }
        
        List<Object[]> aggregatedData = dataPointService.getAggregatedDataByCategory(startTime);
        return ResponseEntity.ok(aggregatedData);
    }
    
    /**
     * Get hourly aggregated data for a category
     */
    @GetMapping("/hourly/{category}")
    public ResponseEntity<List<Object[]>> getHourlyAggregatedData(
            @PathVariable String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime) {
        
        if (startTime == null) {
            startTime = LocalDateTime.now().minusHours(24); // Default to last 24 hours
        }
        
        List<Object[]> hourlyData = dataPointService.getHourlyAggregatedData(category, startTime);
        return ResponseEntity.ok(hourlyData);
    }
    
    /**
     * Search data points
     */
    @GetMapping("/search")
    public ResponseEntity<List<DataPoint>> searchDataPoints(@RequestParam String q) {
        List<DataPoint> dataPoints = dataPointService.searchDataPoints(q);
        return ResponseEntity.ok(dataPoints);
    }
    
    /**
     * Get latest data point for each category
     */
    @GetMapping("/latest")
    public ResponseEntity<List<DataPoint>> getLatestByCategory() {
        List<DataPoint> latestPoints = dataPointService.getLatestByCategory();
        return ResponseEntity.ok(latestPoints);
    }
    
    /**
     * Get count by category
     */
    @GetMapping("/count")
    public ResponseEntity<List<Object[]>> getCountByCategory() {
        List<Object[]> counts = dataPointService.getCountByCategory();
        return ResponseEntity.ok(counts);
    }
    
    /**
     * Generate sample data burst (for testing)
     */
    @PostMapping("/generate/{count}")
    public ResponseEntity<String> generateDataBurst(@PathVariable int count) {
        try {
            if (count > 1000) {
                return ResponseEntity.badRequest().body("Maximum count is 1000");
            }
            
            dataGeneratorService.generateDataBurst(count);
            return ResponseEntity.ok("Generated " + count + " data points");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error generating data: " + e.getMessage());
        }
    }
    
    /**
     * Clean up old data points
     */
    @DeleteMapping("/cleanup/{days}")
    public ResponseEntity<String> cleanupOldData(@PathVariable int days) {
        try {
            if (days < 1) {
                return ResponseEntity.badRequest().body("Days must be greater than 0");
            }
            
            dataPointService.cleanupOldDataPoints(days);
            return ResponseEntity.ok("Cleaned up data older than " + days + " days");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error cleaning up data: " + e.getMessage());
        }
    }
}