package com.dashboard.service;

import com.dashboard.model.DataPoint;
import com.dashboard.repository.DataPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing DataPoint operations
 * Handles business logic for data points and real-time updates
 */
@Service
@Transactional
public class DataPointService {
    
    @Autowired
    private DataPointRepository dataPointRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    /**
     * Save a new data point and broadcast update
     */
    public DataPoint saveDataPoint(DataPoint dataPoint) {
        DataPoint savedPoint = dataPointRepository.save(dataPoint);
        
        // Broadcast real-time update via WebSocket
        messagingTemplate.convertAndSend("/topic/datapoints", savedPoint);
        messagingTemplate.convertAndSend("/topic/datapoints/" + savedPoint.getCategory(), savedPoint);
        
        return savedPoint;
    }
    
    /**
     * Save multiple data points
     */
    public List<DataPoint> saveDataPoints(List<DataPoint> dataPoints) {
        List<DataPoint> savedPoints = dataPointRepository.saveAll(dataPoints);
        
        // Broadcast batch update
        messagingTemplate.convertAndSend("/topic/datapoints/batch", savedPoints);
        
        return savedPoints;
    }
    
    /**
     * Get data point by ID
     */
    @Transactional(readOnly = true)
    public Optional<DataPoint> getDataPointById(Long id) {
        return dataPointRepository.findById(id);
    }
    
    /**
     * Get all data points
     */
    @Transactional(readOnly = true)
    public List<DataPoint> getAllDataPoints() {
        return dataPointRepository.findAll();
    }
    
    /**
     * Get data points by category
     */
    @Transactional(readOnly = true)
    public List<DataPoint> getDataPointsByCategory(String category) {
        return dataPointRepository.findByCategoryOrderByTimestampDesc(category);
    }
    
    /**
     * Get recent data points (last 100)
     */
    @Transactional(readOnly = true)
    public List<DataPoint> getRecentDataPoints() {
        return dataPointRepository.findTop100ByOrderByTimestampDesc();
    }
    
    /**
     * Get recent data points by category (last 50)
     */
    @Transactional(readOnly = true)
    public List<DataPoint> getRecentDataPointsByCategory(String category) {
        return dataPointRepository.findTop50ByCategoryOrderByTimestampDesc(category);
    }
    
    /**
     * Get data points within time range
     */
    @Transactional(readOnly = true)
    public List<DataPoint> getDataPointsInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return dataPointRepository.findByTimestampBetweenOrderByTimestampDesc(startTime, endTime);
    }
    
    /**
     * Get data points by category within time range
     */
    @Transactional(readOnly = true)
    public List<DataPoint> getDataPointsByCategoryInTimeRange(String category, 
                                                             LocalDateTime startTime, 
                                                             LocalDateTime endTime) {
        return dataPointRepository.findByCategoryAndTimestampBetweenOrderByTimestampDesc(
                category, startTime, endTime);
    }
    
    /**
     * Get distinct categories
     */
    @Transactional(readOnly = true)
    public List<String> getDistinctCategories() {
        return dataPointRepository.findDistinctCategories();
    }
    
    /**
     * Get distinct sources
     */
    @Transactional(readOnly = true)
    public List<String> getDistinctSources() {
        return dataPointRepository.findDistinctSources();
    }
    
    /**
     * Get aggregated data by category
     */
    @Transactional(readOnly = true)
    public List<Object[]> getAggregatedDataByCategory(LocalDateTime startTime) {
        return dataPointRepository.getAggregatedDataByCategory(startTime);
    }
    
    /**
     * Get hourly aggregated data for a category
     */
    @Transactional(readOnly = true)
    public List<Object[]> getHourlyAggregatedData(String category, LocalDateTime startTime) {
        return dataPointRepository.getHourlyAggregatedData(category, startTime);
    }
    
    /**
     * Search data points by label or description
     */
    @Transactional(readOnly = true)
    public List<DataPoint> searchDataPoints(String searchTerm) {
        return dataPointRepository.searchByLabelOrDescription(searchTerm);
    }
    
    /**
     * Delete data point by ID
     */
    public void deleteDataPoint(Long id) {
        dataPointRepository.deleteById(id);
        
        // Broadcast deletion
        messagingTemplate.convertAndSend("/topic/datapoints/deleted", id);
    }
    
    /**
     * Update data point
     */
    public DataPoint updateDataPoint(Long id, DataPoint updatedDataPoint) {
        return dataPointRepository.findById(id)
                .map(dataPoint -> {
                    dataPoint.setCategory(updatedDataPoint.getCategory());
                    dataPoint.setValue(updatedDataPoint.getValue());
                    dataPoint.setLabel(updatedDataPoint.getLabel());
                    dataPoint.setSource(updatedDataPoint.getSource());
                    dataPoint.setDescription(updatedDataPoint.getDescription());
                    dataPoint.setUnit(updatedDataPoint.getUnit());
                    dataPoint.setMetadata(updatedDataPoint.getMetadata());
                    
                    DataPoint saved = dataPointRepository.save(dataPoint);
                    
                    // Broadcast update
                    messagingTemplate.convertAndSend("/topic/datapoints/updated", saved);
                    
                    return saved;
                })
                .orElseThrow(() -> new RuntimeException("DataPoint not found with id: " + id));
    }
    
    /**
     * Clean up old data points (older than specified days)
     */
    public void cleanupOldDataPoints(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        dataPointRepository.deleteByTimestampBefore(cutoffDate);
    }
    
    /**
     * Get latest data point for each category
     */
    @Transactional(readOnly = true)
    public List<DataPoint> getLatestByCategory() {
        return dataPointRepository.findLatestByCategory();
    }
    
    /**
     * Get count by category
     */
    @Transactional(readOnly = true)
    public List<Object[]> getCountByCategory() {
        return dataPointRepository.countByCategory();
    }
}