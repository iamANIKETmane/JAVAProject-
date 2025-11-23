package com.dashboard.repository;

import com.dashboard.model.Chart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Chart entity
 */
@Repository
public interface ChartRepository extends JpaRepository<Chart, Long> {
    
    /**
     * Find charts by dashboard ID
     */
    List<Chart> findByDashboardIdOrderByPositionYAscPositionXAsc(Long dashboardId);
    
    /**
     * Find active charts by dashboard ID
     */
    List<Chart> findByDashboardIdAndIsActiveTrueOrderByPositionYAscPositionXAsc(Long dashboardId);
    
    /**
     * Find charts by type
     */
    List<Chart> findByTypeOrderByUpdatedAtDesc(String type);
    
    /**
     * Find charts by dashboard and type
     */
    List<Chart> findByDashboardIdAndType(Long dashboardId, String type);
    
    /**
     * Count charts by dashboard
     */
    long countByDashboardId(Long dashboardId);
    
    /**
     * Count active charts by dashboard
     */
    long countByDashboardIdAndIsActiveTrue(Long dashboardId);
    
    /**
     * Find charts that need refresh (based on refresh interval)
     */
    @Query("SELECT c FROM Chart c " +
           "WHERE c.isActive = true " +
           "AND c.updatedAt <= :cutoffTime")
    List<Chart> findChartsNeedingRefresh(@Param("cutoffTime") java.time.LocalDateTime cutoffTime);
}