package com.dashboard.repository;

import com.dashboard.model.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Dashboard entity
 */
@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    
    /**
     * Find dashboards by creator
     */
    List<Dashboard> findByCreatedByOrderByUpdatedAtDesc(String createdBy);
    
    /**
     * Find public dashboards
     */
    List<Dashboard> findByIsPublicTrueOrderByUpdatedAtDesc();
    
    /**
     * Find dashboards by name containing (case insensitive)
     */
    @Query("SELECT d FROM Dashboard d " +
           "WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "ORDER BY d.updatedAt DESC")
    List<Dashboard> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Find dashboard by name and creator
     */
    Optional<Dashboard> findByNameAndCreatedBy(String name, String createdBy);
    
    /**
     * Check if dashboard name exists for a creator
     */
    boolean existsByNameAndCreatedBy(String name, String createdBy);
}