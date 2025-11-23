/**
 * Real-Time Dashboard JavaScript
 * Handles WebSocket connections, data visualization, and user interactions
 */

class RealTimeDashboard {
    constructor() {
        this.stompClient = null;
        this.isConnected = false;
        this.charts = {};
        this.dataCache = {
            recent: [],
            categories: [],
            sources: []
        };
        this.updateCounter = 0;
        
        // Initialize dashboard
        this.init();
    }
    
    init() {
        console.log('Initializing Real-Time Dashboard...');
        
        // Connect to WebSocket
        this.connectWebSocket();
        
        // Initialize charts
        this.initializeCharts();
        
        // Load initial data
        this.loadInitialData();
        
        // Set up periodic updates
        this.setupPeriodicUpdates();
        
        // Set up event listeners
        this.setupEventListeners();
        
        console.log('Dashboard initialization complete');
    }
    
    // WebSocket Connection
    connectWebSocket() {
        const socket = new SockJS('/ws');
        this.stompClient = Stomp.over(socket);
        
        // Disable debug logging in production
        this.stompClient.debug = (str) => {
            // console.log(str);
        };
        
        this.stompClient.connect({}, (frame) => {
            console.log('Connected to WebSocket:', frame);
            this.isConnected = true;
            this.updateConnectionStatus(true);
            
            // Subscribe to data point updates
            this.stompClient.subscribe('/topic/datapoints', (message) => {
                const dataPoint = JSON.parse(message.body);
                this.handleNewDataPoint(dataPoint);
            });
            
            // Subscribe to batch updates
            this.stompClient.subscribe('/topic/datapoints/batch', (message) => {
                const dataPoints = JSON.parse(message.body);
                this.handleBatchDataPoints(dataPoints);
            });
            
            // Subscribe to system status
            this.stompClient.subscribe('/topic/system/status', (message) => {
                const status = JSON.parse(message.body);
                this.handleSystemStatus(status);
            });
            
            // Subscribe to notifications
            this.stompClient.subscribe('/topic/notifications', (message) => {
                this.showNotification(message.body, 'info');
            });
            
        }, (error) => {
            console.error('WebSocket connection error:', error);
            this.isConnected = false;
            this.updateConnectionStatus(false);
            
            // Attempt to reconnect after 5 seconds
            setTimeout(() => this.connectWebSocket(), 5000);
        });
    }
    
    // Update connection status indicator
    updateConnectionStatus(connected) {
        const statusIndicator = document.getElementById('connectionStatus');
        const statusText = document.getElementById('connectionText');
        
        if (connected) {
            statusIndicator.className = 'status-indicator status-active real-time-indicator';
            statusText.textContent = 'Connected';
        } else {
            statusIndicator.className = 'status-indicator status-inactive';
            statusText.textContent = 'Disconnected';
        }
    }
    
    // Initialize all charts
    initializeCharts() {
        this.initTrendChart();
        this.initPieChart();
        this.initBarChart();
        this.initAreaChart();
    }
    
    // Initialize trend chart (line chart)
    initTrendChart() {
        const ctx = document.getElementById('trendChart').getContext('2d');
        this.charts.trend = new Chart(ctx, {
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
                },
                plugins: {
                    legend: {
                        display: true
                    }
                },
                scales: {
                    x: {
                        display: true,
                        title: {
                            display: true,
                            text: 'Time'
                        }
                    },
                    y: {
                        display: true,
                        title: {
                            display: true,
                            text: 'Value'
                        }
                    }
                }
            }
        });
    }
    
    // Initialize pie chart
    initPieChart() {
        const ctx = document.getElementById('pieChart').getContext('2d');
        this.charts.pie = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: [],
                datasets: [{
                    data: [],
                    backgroundColor: [
                        '#FF6384',
                        '#36A2EB',
                        '#FFCE56',
                        '#4BC0C0',
                        '#9966FF',
                        '#FF9F40',
                        '#FF6384',
                        '#C9CBCF'
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
    
    // Initialize bar chart
    initBarChart() {
        const ctx = document.getElementById('barChart').getContext('2d');
        this.charts.bar = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: [],
                datasets: [{
                    label: 'Total Values',
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
    
    // Initialize area chart
    initAreaChart() {
        const ctx = document.getElementById('areaChart').getContext('2d');
        this.charts.area = new Chart(ctx, {
            type: 'line',
            data: {
                labels: [],
                datasets: [{
                    label: 'Hourly Average',
                    data: [],
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 2,
                    fill: true,
                    tension: 0.4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                elements: {
                    point: {
                        radius: 3
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
    
    // Load initial data
    async loadInitialData() {
        try {
            // Load recent data points
            await this.loadRecentData();
            
            // Load categories
            await this.loadCategories();
            
            // Load aggregated data
            await this.loadAggregatedData();
            
            // Load hourly data
            await this.loadHourlyData();
            
            // Update statistics
            this.updateStatistics();
            
        } catch (error) {
            console.error('Error loading initial data:', error);
            this.showNotification('Error loading initial data', 'error');
        }
    }
    
    // Load recent data points
    async loadRecentData() {
        try {
            const response = await fetch('/api/datapoints/recent');
            const data = await response.json();
            
            this.dataCache.recent = data;
            this.updateRecentDataTable(data);
            this.updateTrendChart(data);
            
        } catch (error) {
            console.error('Error loading recent data:', error);
        }
    }
    
    // Load categories
    async loadCategories() {
        try {
            const response = await fetch('/api/datapoints/categories');
            const categories = await response.json();
            
            this.dataCache.categories = categories;
            this.updateCategorySelect(categories);
            
        } catch (error) {
            console.error('Error loading categories:', error);
        }
    }
    
    // Load aggregated data
    async loadAggregatedData() {
        try {
            const response = await fetch('/api/datapoints/aggregated');
            const aggregatedData = await response.json();
            
            this.updatePieChart(aggregatedData);
            this.updateBarChart(aggregatedData);
            
        } catch (error) {
            console.error('Error loading aggregated data:', error);
        }
    }
    
    // Load hourly data
    async loadHourlyData() {
        try {
            if (this.dataCache.categories.length > 0) {
                const category = this.dataCache.categories[0];
                const response = await fetch(`/api/datapoints/hourly/${category}`);
                const hourlyData = await response.json();
                
                this.updateAreaChart(hourlyData);
            }
        } catch (error) {
            console.error('Error loading hourly data:', error);
        }
    }
    
    // Handle new data point from WebSocket
    handleNewDataPoint(dataPoint) {
        console.log('Received new data point:', dataPoint);
        
        // Add to cache
        this.dataCache.recent.unshift(dataPoint);
        if (this.dataCache.recent.length > 100) {
            this.dataCache.recent.pop();
        }
        
        // Update charts and table
        this.updateTrendChart(this.dataCache.recent);
        this.updateRecentDataTable(this.dataCache.recent);
        
        // Update counter
        this.updateCounter++;
        document.getElementById('realtimeUpdates').textContent = this.updateCounter;
        
        // Reload aggregated data periodically
        if (this.updateCounter % 10 === 0) {
            this.loadAggregatedData();
        }
    }
    
    // Handle batch data points
    handleBatchDataPoints(dataPoints) {
        console.log('Received batch data points:', dataPoints.length);
        
        dataPoints.forEach(dataPoint => {
            this.dataCache.recent.unshift(dataPoint);
        });
        
        // Keep only latest 100 points
        this.dataCache.recent = this.dataCache.recent.slice(0, 100);
        
        // Update visualizations
        this.updateTrendChart(this.dataCache.recent);
        this.updateRecentDataTable(this.dataCache.recent);
        
        this.updateCounter += dataPoints.length;
        document.getElementById('realtimeUpdates').textContent = this.updateCounter;
    }
    
    // Handle system status updates
    handleSystemStatus(status) {
        console.log('System status update:', status);
        // You can add system status indicators here
    }
    
    // Update trend chart
    updateTrendChart(data) {
        if (!this.charts.trend || !data || data.length === 0) return;
        
        const chart = this.charts.trend;
        const latest50 = data.slice(0, 50).reverse(); // Show latest 50 points
        
        chart.data.labels = latest50.map(point => 
            new Date(point.timestamp).toLocaleTimeString()
        );
        chart.data.datasets[0].data = latest50.map(point => point.value);
        chart.update('none');
    }
    
    // Update pie chart
    updatePieChart(aggregatedData) {
        if (!this.charts.pie || !aggregatedData || aggregatedData.length === 0) return;
        
        const chart = this.charts.pie;
        chart.data.labels = aggregatedData.map(item => item[0]); // category
        chart.data.datasets[0].data = aggregatedData.map(item => item[1]); // total value
        chart.update();
    }
    
    // Update bar chart
    updateBarChart(aggregatedData) {
        if (!this.charts.bar || !aggregatedData || aggregatedData.length === 0) return;
        
        const chart = this.charts.bar;
        chart.data.labels = aggregatedData.map(item => item[0]); // category
        chart.data.datasets[0].data = aggregatedData.map(item => item[1]); // total value
        chart.update();
    }
    
    // Update area chart
    updateAreaChart(hourlyData) {
        if (!this.charts.area || !hourlyData || hourlyData.length === 0) return;
        
        const chart = this.charts.area;
        chart.data.labels = hourlyData.map(item => {
            const hour = new Date(item[0]).toLocaleTimeString('en-US', {
                hour: '2-digit',
                minute: '2-digit'
            });
            return hour;
        });
        chart.data.datasets[0].data = hourlyData.map(item => item[1]); // avg value
        chart.update();
    }
    
    // Update recent data table
    updateRecentDataTable(data) {
        const tbody = document.getElementById('recentDataBody');
        if (!tbody || !data) return;
        
        tbody.innerHTML = '';
        
        data.slice(0, 20).forEach(point => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${new Date(point.timestamp).toLocaleString()}</td>
                <td><span class="badge bg-primary">${point.category}</span></td>
                <td>${point.label}</td>
                <td><strong>${point.value.toFixed(2)}</strong> ${point.unit || ''}</td>
                <td>${point.source || '-'}</td>
                <td>
                    <button class="btn btn-sm btn-outline-secondary" onclick="dashboard.viewDataPoint(${point.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        });
    }
    
    // Update category select dropdown
    updateCategorySelect(categories) {
        const select = document.getElementById('trendCategorySelect');
        if (!select) return;
        
        // Clear existing options (except "All Categories")
        select.innerHTML = '<option value="">All Categories</option>';
        
        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category;
            option.textContent = category;
            select.appendChild(option);
        });
    }
    
    // Update statistics
    updateStatistics() {
        // Total data points
        document.getElementById('totalDataPoints').textContent = 
            this.dataCache.recent.length;
        
        // Active categories
        document.getElementById('activeCategories').textContent = 
            this.dataCache.categories.length;
        
        // Real-time updates counter is updated in handleNewDataPoint
    }
    
    // Setup periodic updates
    setupPeriodicUpdates() {
        // Refresh aggregated data every 30 seconds
        setInterval(() => {
            this.loadAggregatedData();
        }, 30000);
        
        // Refresh categories every 60 seconds
        setInterval(() => {
            this.loadCategories();
        }, 60000);
        
        // Update statistics every 10 seconds
        setInterval(() => {
            this.updateStatistics();
        }, 10000);
    }
    
    // Setup event listeners
    setupEventListeners() {
        // Window beforeunload event to clean up WebSocket connection
        window.addEventListener('beforeunload', () => {
            if (this.stompClient && this.isConnected) {
                this.stompClient.disconnect();
            }
        });
        
        // Handle visibility change to pause/resume updates
        document.addEventListener('visibilitychange', () => {
            if (document.hidden) {
                console.log('Page hidden, pausing updates');
            } else {
                console.log('Page visible, resuming updates');
                this.loadRecentData(); // Refresh data when page becomes visible
            }
        });
    }
    
    // Public methods for UI interaction
    viewDataPoint(id) {
        // Implement data point detail view
        console.log('View data point:', id);
        this.showNotification(`Viewing data point ${id}`, 'info');
    }
    
    refreshDashboard() {
        console.log('Refreshing dashboard...');
        this.loadInitialData();
        this.showNotification('Dashboard refreshed', 'success');
    }
    
    generateSampleData() {
        if (!this.isConnected) {
            this.showNotification('WebSocket not connected', 'error');
            return;
        }
        
        fetch('/api/datapoints/generate/10', { method: 'POST' })
            .then(response => response.text())
            .then(message => {
                this.showNotification(message, 'success');
            })
            .catch(error => {
                console.error('Error generating sample data:', error);
                this.showNotification('Error generating sample data', 'error');
            });
    }
    
    exportData() {
        // Implement data export functionality
        const csvContent = this.dataCache.recent.map(point => 
            `${point.timestamp},${point.category},${point.label},${point.value},${point.source || ''}`
        ).join('\\n');
        
        const csvHeader = 'Timestamp,Category,Label,Value,Source\\n';
        const csvData = csvHeader + csvContent;
        
        const blob = new Blob([csvData], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `dashboard-data-${new Date().toISOString().split('T')[0]}.csv`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
        
        this.showNotification('Data exported successfully', 'success');
    }
    
    updateTrendChart() {
        // This method can be called from the UI to update trend chart for specific category
        const selectedCategory = document.getElementById('trendCategorySelect').value;
        let filteredData = this.dataCache.recent;
        
        if (selectedCategory) {
            filteredData = this.dataCache.recent.filter(point => 
                point.category === selectedCategory
            );
        }
        
        this.updateTrendChart(filteredData);
    }
    
    // Utility method to show notifications
    showNotification(message, type = 'info') {
        // Create notification element
        const notification = document.createElement('div');
        notification.className = `alert alert-${type === 'error' ? 'danger' : type === 'success' ? 'success' : 'info'} alert-dismissible fade show position-fixed`;
        notification.style.top = '70px';
        notification.style.right = '20px';
        notification.style.zIndex = '9999';
        notification.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        document.body.appendChild(notification);
        
        // Auto remove after 5 seconds
        setTimeout(() => {
            if (notification.parentNode) {
                notification.remove();
            }
        }, 5000);
    }
}

// Global dashboard instance
let dashboard;

// Initialize dashboard when page loads
document.addEventListener('DOMContentLoaded', function() {
    dashboard = new RealTimeDashboard();
});

// Global functions for HTML onclick handlers
function generateSampleData() {
    dashboard.generateSampleData();
}

function refreshDashboard() {
    dashboard.refreshDashboard();
}

function exportData() {
    dashboard.exportData();
}

function loadRecentData() {
    dashboard.loadRecentData();
}

function updateTrendChart() {
    dashboard.updateTrendChart();
}