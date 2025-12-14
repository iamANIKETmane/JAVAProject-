package com.dashboard.repository;

import com.dashboard.model.DataPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for DataPoint entity
 * Provides data access methods for dashboard data points
 */
@Repository
public interface DataPointRepository extends JpaRepository<DataPoint, Long> {
    
    /**
     * Find data points by category
     */
    List<DataPoint> findByCategoryOrderByTimestampDesc(String category);
    
    /**
     * Find data points within a time range
     */
    List<DataPoint> findByTimestampBetweenOrderByTimestampDesc(
            LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find data points by category within a time range
     */
    List<DataPoint> findByCategoryAndTimestampBetweenOrderByTimestampDesc(
            String category, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find data points by source
     */
    List<DataPoint> findBySourceOrderByTimestampDesc(String source);
    
    /**
     * Find recent data points (last N records)
     */
    List<DataPoint> findTop100ByOrderByTimestampDesc();
    
    /**
     * Find recent data points by category
     */
    List<DataPoint> findTop50ByCategoryOrderByTimestampDesc(String category);
    
    /**
     * Get distinct categories
     */
    @Query("SELECT DISTINCT d.category FROM DataPoint d ORDER BY d.category")
    List<String> findDistinctCategories();
    
    /**
     * Get distinct sources
     */
    @Query("SELECT DISTINCT d.source FROM DataPoint d ORDER BY d.source")
    List<String> findDistinctSources();
    
    /**
     * Get aggregated data by category (sum of values)
     */
    @Query("SELECT d.category, SUM(d.value) as totalValue, COUNT(d) as count " +
           "FROM DataPoint d " +
           "WHERE d.timestamp >= :startTime " +
           "GROUP BY d.category " +
           "ORDER BY totalValue DESC")
    List<Object[]> getAggregatedDataByCategory(@Param("startTime") LocalDateTime startTime);
    
    /**
     * Get hourly aggregated data for a category
     */
    @Query(value = "SELECT " +
           "FORMATDATETIME(timestamp, 'yyyy-MM-dd HH:00:00') as hour, " +
           "AVG(\"value\") as avgValue, " +
           "MIN(\"value\") as minValue, " +
           "MAX(\"value\") as maxValue, " +
           "COUNT(*) as count " +
           "FROM data_points " +
           "WHERE category = :category " +
           "AND timestamp >= :startTime " +
           "GROUP BY FORMATDATETIME(timestamp, 'yyyy-MM-dd HH:00:00') " +
           "ORDER BY hour", nativeQuery = true)
    List<Object[]> getHourlyAggregatedData(@Param("category") String category, 
                                          @Param("startTime") LocalDateTime startTime);
    
    /**
     * Delete old data points (older than specified date)
     */
    void deleteByTimestampBefore(LocalDateTime cutoffDate);
    
    /**
     * Count data points by category
     */
    @Query("SELECT d.category, COUNT(d) FROM DataPoint d GROUP BY d.category")
    List<Object[]> countByCategory();
    
    /**
     * Get latest data point for each category
     */
    @Query("SELECT d1 FROM DataPoint d1 " +
           "WHERE d1.timestamp = (" +
           "    SELECT MAX(d2.timestamp) FROM DataPoint d2 " +
           "    WHERE d2.category = d1.category" +
           ")")
    List<DataPoint> findLatestByCategory();
    
    /**
     * Search data points by label or description
     */
    @Query("SELECT d FROM DataPoint d " +
           "WHERE LOWER(d.label) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY d.timestamp DESC")
    List<DataPoint> searchByLabelOrDescription(@Param("searchTerm") String searchTerm);
}