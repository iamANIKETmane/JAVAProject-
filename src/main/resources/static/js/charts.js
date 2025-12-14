/**
 * Advanced Charts Module for Real-Time Dashboard
 * Handles complex chart interactions, configurations, and advanced visualizations
 */

class AdvancedCharts {
    constructor() {
        this.charts = {};
        this.chartConfigs = {};
        this.currentFilters = {
            category: '',
            timeRange: '24h',
            aggregation: 'sum',
            chartType: 'line'
        };
        this.statistics = {};
        
        // Initialize immediately - don't wait for dashboard
        this.init();
    }
    
    init() {
        console.log('Initializing Advanced Charts...');
        
        // Load categories for filter
        this.loadCategories();
        
        // Initialize all charts
        this.initializeCharts();
        
        // Load initial data
        this.loadChartData();
        
        // Setup event listeners
        this.setupEventListeners();
        
        console.log('Advanced Charts initialized successfully');
    }
    
    // Load categories for filter dropdown
    async loadCategories() {
        try {
            const response = await fetch('/api/datapoints/categories');
            const categories = await response.json();
            
            const categorySelect = document.getElementById('chartCategory');
            if (categorySelect) {
                categorySelect.innerHTML = '<option value="">All Categories</option>';
                categories.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category;
                    option.textContent = category;
                    categorySelect.appendChild(option);
                });
            }
        } catch (error) {
            console.error('Error loading categories:', error);
        }
    }
    
    // Initialize all chart instances
    initializeCharts() {
        this.initPrimaryChart();
        this.initDistributionChart();
        this.initComparisonChart();
        this.initTrendChart();
        this.initHeatmapChart();
        this.initGaugeChart();
    }
    
    // Initialize primary chart (main interactive chart)
    initPrimaryChart() {
        const ctx = document.getElementById('primaryChart').getContext('2d');
        
        this.chartConfigs.primary = {
            type: 'line',
            data: {
                labels: [],
                datasets: [{
                    label: 'Data Values',
                    data: [],
                    borderColor: 'rgb(75, 192, 192)',
                    backgroundColor: 'rgba(75, 192, 192, 0.1)',
                    tension: 0.4,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                interaction: {
                    intersect: false,
                    mode: 'index'
                },
                plugins: {
                    legend: {
                        display: true,
                        position: 'top'
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return `${context.dataset.label}: ${context.parsed.y.toFixed(2)}`;
                            }
                        }
                    },
                    zoom: {
                        zoom: {
                            wheel: {
                                enabled: true,
                            },
                            pinch: {
                                enabled: true
                            },
                            mode: 'x',
                        },
                        pan: {
                            enabled: true,
                            mode: 'x',
                        }
                    }
                },
                scales: {
                    x: {
                        type: 'time',
                        time: {
                            parser: 'YYYY-MM-DD HH:mm:ss',
                            tooltipFormat: 'MMM DD, HH:mm',
                            displayFormats: {
                                minute: 'HH:mm',
                                hour: 'MMM DD HH:mm',
                                day: 'MMM DD'
                            }
                        },
                        title: {
                            display: true,
                            text: 'Time'
                        }
                    },
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Value'
                        }
                    }
                }
            }
        };
        
        this.charts.primary = new Chart(ctx, this.chartConfigs.primary);
    }
    
    // Initialize distribution chart (pie/doughnut)
    initDistributionChart() {
        const ctx = document.getElementById('distributionChart').getContext('2d');
        
        this.charts.distribution = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: [],
                datasets: [{
                    data: [],
                    backgroundColor: [
                        '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0',
                        '#9966FF', '#FF9F40', '#FF6384', '#C9CBCF'
                    ]
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    }
    
    // Initialize comparison chart (bar chart)
    initComparisonChart() {
        const ctx = document.getElementById('comparisonChart').getContext('2d');
        
        this.charts.comparison = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: [],
                datasets: [{
                    label: 'Category Values',
                    data: [],
                    backgroundColor: 'rgba(54, 162, 235, 0.8)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
    
    // Initialize trend analysis chart
    initTrendChart() {
        const ctx = document.getElementById('trendChart').getContext('2d');
        
        this.charts.trend = new Chart(ctx, {
            type: 'line',
            data: {
                labels: [],
                datasets: [{
                    label: 'Moving Average',
                    data: [],
                    borderColor: 'rgba(255, 99, 132, 1)',
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    tension: 0.4,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
    
    // Initialize heatmap chart (using scatter plot)
    initHeatmapChart() {
        const ctx = document.getElementById('heatmapChart').getContext('2d');
        
        this.charts.heatmap = new Chart(ctx, {
            type: 'scatter',
            data: {
                datasets: [{
                    label: 'Data Distribution',
                    data: [],
                    backgroundColor: function(context) {
                        const value = context.parsed.y;
                        const alpha = Math.min(value / 100, 1);
                        return `rgba(255, 99, 132, ${alpha})`;
                    },
                    pointRadius: 5
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        type: 'linear',
                        position: 'bottom',
                        title: {
                            display: true,
                            text: 'Time (Hours)'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Value'
                        }
                    }
                }
            }
        });
    }
    
    // Initialize gauge chart (using doughnut)
    initGaugeChart() {
        const ctx = document.getElementById('gaugeChart').getContext('2d');
        
        this.charts.gauge = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Used', 'Available'],
                datasets: [{
                    data: [65, 35],
                    backgroundColor: ['#FF6384', '#E7E9ED'],
                    borderWidth: 0,
                    circumference: 180,
                    rotation: 270
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '75%',
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        enabled: false
                    }
                }
            },
            plugins: [{
                beforeDraw: function(chart) {
                    const width = chart.width,
                          height = chart.height,
                          ctx = chart.ctx;
                    
                    ctx.restore();
                    const fontSize = (height / 114).toFixed(2);
                    ctx.font = fontSize + "em sans-serif";
                    ctx.textBaseline = "middle";
                    
                    const text = chart.data.datasets[0].data[0] + "%",
                          textX = Math.round((width - ctx.measureText(text).width) / 2),
                          textY = height / 2;
                    
                    ctx.fillText(text, textX, textY);
                    ctx.save();
                }
            }]
        });
    }
    
    // Load chart data based on current filters
    async loadChartData() {
        try {
            // Build query parameters
            const params = new URLSearchParams();
            if (this.currentFilters.category) {
                params.append('category', this.currentFilters.category);
            }
            params.append('timeRange', this.currentFilters.timeRange);
            params.append('aggregation', this.currentFilters.aggregation);
            
            // Load data for different charts
            await Promise.all([
                this.loadPrimaryChartData(params),
                this.loadDistributionData(params),
                this.loadComparisonData(params),
                this.loadTrendData(params),
                this.loadHeatmapData(params)
            ]);
            
            // Update statistics
            this.updateStatistics();
            
        } catch (error) {
            console.error('Error loading chart data:', error);
        }
    }
    
    // Load primary chart data
    async loadPrimaryChartData(params) {
        try {
            const response = await fetch(`/api/datapoints/time-series?${params}`);
            const data = await response.json();
            
            this.updatePrimaryChart(data);
        } catch (error) {
            console.error('Error loading primary chart data:', error);
        }
    }
    
    // Load distribution data
    async loadDistributionData(params) {
        try {
            const response = await fetch(`/api/datapoints/aggregated?${params}`);
            const data = await response.json();
            
            this.updateDistributionChart(data);
        } catch (error) {
            console.error('Error loading distribution data:', error);
        }
    }
    
    // Load comparison data
    async loadComparisonData(params) {
        try {
            const response = await fetch(`/api/datapoints/aggregated?${params}`);
            const data = await response.json();
            
            this.updateComparisonChart(data);
        } catch (error) {
            console.error('Error loading comparison data:', error);
        }
    }
    
    // Load trend data
    async loadTrendData(params) {
        try {
            const response = await fetch(`/api/datapoints/moving-average?${params}`);
            const data = await response.json();
            
            this.updateTrendChart(data);
        } catch (error) {
            console.error('Error loading trend data:', error);
        }
    }
    
    // Load heatmap data
    async loadHeatmapData(params) {
        try {
            const response = await fetch(`/api/datapoints/scatter?${params}`);
            const data = await response.json();
            
            this.updateHeatmapChart(data);
        } catch (error) {
            console.error('Error loading heatmap data:', error);
        }
    }
    
    // Update primary chart
    updatePrimaryChart(data) {
        if (!this.charts.primary || !data) return;
        
        const chart = this.charts.primary;
        chart.data.labels = data.map(item => new Date(item.timestamp));
        chart.data.datasets[0].data = data.map(item => item.value);
        chart.update();
    }
    
    // Update distribution chart
    updateDistributionChart(data) {
        if (!this.charts.distribution || !data) return;
        
        const chart = this.charts.distribution;
        chart.data.labels = data.map(item => item[0]);
        chart.data.datasets[0].data = data.map(item => item[1]);
        chart.update();
    }
    
    // Update comparison chart
    updateComparisonChart(data) {
        if (!this.charts.comparison || !data) return;
        
        const chart = this.charts.comparison;
        chart.data.labels = data.map(item => item[0]);
        chart.data.datasets[0].data = data.map(item => item[1]);
        chart.update();
    }
    
    // Update trend chart
    updateTrendChart(data) {
        if (!this.charts.trend || !data) return;
        
        const chart = this.charts.trend;
        chart.data.labels = data.map(item => new Date(item.timestamp));
        chart.data.datasets[0].data = data.map(item => item.movingAverage);
        chart.update();
    }
    
    // Update heatmap chart
    updateHeatmapChart(data) {
        if (!this.charts.heatmap || !data) return;
        
        const chart = this.charts.heatmap;
        chart.data.datasets[0].data = data.map(item => ({
            x: item.hour,
            y: item.value
        }));
        chart.update();
    }
    
    // Calculate and update statistics
    updateStatistics() {
        if (!window.dashboard || !window.dashboard.dataCache.recent) return;
        
        const data = window.dashboard.dataCache.recent;
        if (data.length === 0) return;
        
        const values = data.map(item => item.value);
        
        this.statistics = {
            totalPoints: data.length,
            avgValue: values.reduce((a, b) => a + b, 0) / values.length,
            minValue: Math.min(...values),
            maxValue: Math.max(...values),
            stdDev: this.calculateStandardDeviation(values),
            lastUpdate: new Date().toLocaleTimeString()
        };
        
        // Update DOM elements
        document.getElementById('totalPoints').textContent = this.statistics.totalPoints;
        document.getElementById('avgValue').textContent = this.statistics.avgValue.toFixed(2);
        document.getElementById('minValue').textContent = this.statistics.minValue.toFixed(2);
        document.getElementById('maxValue').textContent = this.statistics.maxValue.toFixed(2);
        document.getElementById('stdDev').textContent = this.statistics.stdDev.toFixed(2);
        document.getElementById('lastUpdate').textContent = this.statistics.lastUpdate;
        
        // Update gauge chart
        const utilizationPercent = Math.min((this.statistics.avgValue / this.statistics.maxValue) * 100, 100);
        if (this.charts.gauge) {
            this.charts.gauge.data.datasets[0].data = [utilizationPercent, 100 - utilizationPercent];
            this.charts.gauge.update();
        }
    }
    
    // Calculate standard deviation
    calculateStandardDeviation(values) {
        const avg = values.reduce((a, b) => a + b, 0) / values.length;
        const squareDiffs = values.map(value => Math.pow(value - avg, 2));
        const avgSquareDiff = squareDiffs.reduce((a, b) => a + b, 0) / values.length;
        return Math.sqrt(avgSquareDiff);
    }
    
    // Setup event listeners
    setupEventListeners() {
        // Subscribe to WebSocket updates if dashboard is available
        if (window.dashboard && window.dashboard.stompClient && window.dashboard.isConnected) {
            // Update charts when new data arrives
            const originalHandler = window.dashboard.handleNewDataPoint;
            window.dashboard.handleNewDataPoint = (dataPoint) => {
                originalHandler.call(window.dashboard, dataPoint);
                this.handleNewDataPoint(dataPoint);
            };
        }
    }
    
    // Handle new data point for chart updates
    handleNewDataPoint(dataPoint) {
        // Update primary chart with new data point
        if (this.charts.primary && (!this.currentFilters.category || dataPoint.category === this.currentFilters.category)) {
            const chart = this.charts.primary;
            chart.data.labels.push(new Date(dataPoint.timestamp));
            chart.data.datasets[0].data.push(dataPoint.value);
            
            // Keep only last 50 points for performance
            if (chart.data.labels.length > 50) {
                chart.data.labels.shift();
                chart.data.datasets[0].data.shift();
            }
            
            chart.update('none');
        }
        
        // Update statistics every 10 data points
        if (window.dashboard && window.dashboard.updateCounter % 10 === 0) {
            this.updateStatistics();
        }
    }
    
    // Public methods for UI controls
    updateChartType() {
        const chartType = document.getElementById('chartType').value;
        this.currentFilters.chartType = chartType;
        
        if (this.charts.primary) {
            this.charts.primary.config.type = chartType;
            this.charts.primary.update();
        }
    }
    
    filterByCategory() {
        const category = document.getElementById('chartCategory').value;
        this.currentFilters.category = category;
        this.loadChartData();
    }
    
    updateTimeRange() {
        const timeRange = document.getElementById('timeRange').value;
        this.currentFilters.timeRange = timeRange;
        this.loadChartData();
    }
    
    updateAggregation() {
        const aggregation = document.getElementById('aggregation').value;
        this.currentFilters.aggregation = aggregation;
        this.loadChartData();
    }
    
    refreshCharts() {
        console.log('Refreshing all charts...');
        this.loadChartData();
    }
    
    exportCharts() {
        // Export all charts as images
        const charts = Object.keys(this.charts);
        charts.forEach(chartName => {
            const chart = this.charts[chartName];
            const url = chart.toBase64Image();
            const link = document.createElement('a');
            link.download = `${chartName}-chart-${new Date().toISOString().split('T')[0]}.png`;
            link.href = url;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        });
        
        if (window.dashboard) {
            window.dashboard.showNotification('Charts exported successfully', 'success');
        }
    }
    
    zoomIn() {
        if (this.charts.primary && this.charts.primary.zoom) {
            this.charts.primary.zoom.zoom(1.1);
        }
    }
    
    zoomOut() {
        if (this.charts.primary && this.charts.primary.zoom) {
            this.charts.primary.zoom.zoom(0.9);
        }
    }
    
    resetZoom() {
        if (this.charts.primary && this.charts.primary.resetZoom) {
            this.charts.primary.resetZoom();
        }
    }
}

// Global charts instance
let advancedCharts;

// Initialize charts when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    // Wait a bit for dashboard to initialize
    setTimeout(() => {
        advancedCharts = new AdvancedCharts();
    }, 1000);
});

// Global functions for HTML onclick handlers
function updateChartType() {
    if (advancedCharts) advancedCharts.updateChartType();
}

function filterByCategory() {
    if (advancedCharts) advancedCharts.filterByCategory();
}

function updateTimeRange() {
    if (advancedCharts) advancedCharts.updateTimeRange();
}

function updateAggregation() {
    if (advancedCharts) advancedCharts.updateAggregation();
}

function refreshCharts() {
    if (advancedCharts) advancedCharts.refreshCharts();
}

function exportCharts() {
    if (advancedCharts) advancedCharts.exportCharts();
}

function zoomIn() {
    if (advancedCharts) advancedCharts.zoomIn();
}

function zoomOut() {
    if (advancedCharts) advancedCharts.zoomOut();
}

function resetZoom() {
    if (advancedCharts) advancedCharts.resetZoom();
}