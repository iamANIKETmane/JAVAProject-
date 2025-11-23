package com.dashboard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Entity representing a data point in the dashboard
 * This is the core data model for all visualizations
 */
@Entity
@Table(name = "data_points", indexes = {
    @Index(name = "idx_category", columnList = "category"),
    @Index(name = "idx_timestamp", columnList = "timestamp"),
    @Index(name = "idx_source", columnList = "source")
})
public class DataPoint {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Category is required")
    @Column(nullable = false, length = 100)
    private String category;
    
    @NotNull(message = "Value is required")
    @Positive(message = "Value must be positive")
    @Column(nullable = false)
    private Double value;
    
    @NotBlank(message = "Label is required")
    @Column(nullable = false, length = 200)
    private String label;
    
    @Column(length = 100)
    private String source;
    
    @Column(length = 500)
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(length = 50)
    private String unit;
    
    @Column
    private String metadata; // JSON string for additional data
    
    // Default constructor
    public DataPoint() {
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor with required fields
    public DataPoint(String category, Double value, String label) {
        this();
        this.category = category;
        this.value = value;
        this.label = label;
    }
    
    // Constructor with all fields
    public DataPoint(String category, Double value, String label, String source, String description, String unit) {
        this(category, value, label);
        this.source = source;
        this.description = description;
        this.unit = unit;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Double getValue() {
        return value;
    }
    
    public void setValue(Double value) {
        this.value = value;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getMetadata() {
        return metadata;
    }
    
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
    
    @Override
    public String toString() {
        return "DataPoint{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", value=" + value +
                ", label='" + label + '\'' +
                ", source='" + source + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataPoint)) return false;
        DataPoint dataPoint = (DataPoint) o;
        return id != null && id.equals(dataPoint.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}