// Data Management Page
let dataTable = [];
let currentPage = 1;
const recordsPerPage = 20;

// Initialize the page
document.addEventListener('DOMContentLoaded', () => {
    loadDataStatistics();
    loadCategories();
    loadDataTable();
    setupEventListeners();
});

// Load data statistics
async function loadDataStatistics() {
    try {
        // Get total records
        const allData = await fetch('/api/datapoints').then(r => r.json());
        document.getElementById('totalRecords').textContent = allData.length;
        
        // Get categories
        const categories = await fetch('/api/datapoints/categories').then(r => r.json());
        document.getElementById('totalCategories').textContent = categories.length;
        
        // Get today's records
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        const todayRecords = allData.filter(d => new Date(d.timestamp) >= today);
        document.getElementById('todayRecords').textContent = todayRecords.length;
        
        // Calculate average value
        const avgValue = allData.reduce((sum, d) => sum + d.value, 0) / allData.length;
        document.getElementById('avgValue').textContent = avgValue.toFixed(2);
        
    } catch (error) {
        console.error('Error loading statistics:', error);
    }
}

// Load categories for filter dropdown
async function loadCategories() {
    try {
        const categories = await fetch('/api/datapoints/categories').then(r => r.json());
        const select = document.getElementById('categoryFilter');
        
        categories.forEach(cat => {
            const option = document.createElement('option');
            option.value = cat;
            option.textContent = cat;
            select.appendChild(option);
        });
        
        // Also populate modal category select
        const modalSelect = document.getElementById('dataCategory');
        if (modalSelect) {
            categories.forEach(cat => {
                const option = document.createElement('option');
                option.value = cat;
                option.textContent = cat;
                modalSelect.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error loading categories:', error);
    }
}

// Load data table
async function loadDataTable() {
    try {
        const response = await fetch('/api/datapoints/recent?limit=1000');
        dataTable = await response.json();
        renderTable();
    } catch (error) {
        console.error('Error loading data table:', error);
    }
}

// Render table
function renderTable() {
    const tbody = document.getElementById('dataTableBody');
    if (!tbody) return;
    
    const start = (currentPage - 1) * recordsPerPage;
    const end = start + recordsPerPage;
    const pageData = dataTable.slice(start, end);
    
    tbody.innerHTML = pageData.map(data => `
        <tr>
            <td>${data.id}</td>
            <td><span class="badge bg-primary">${data.category}</span></td>
            <td>${data.label || '-'}</td>
            <td>${data.value.toFixed(2)}</td>
            <td>${data.unit || '-'}</td>
            <td>${data.source || '-'}</td>
            <td>${new Date(data.timestamp).toLocaleString()}</td>
            <td>
                <button class="btn btn-sm btn-info" onclick="viewData(${data.id})">
                    <i class="fas fa-eye"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="deleteData(${data.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
    
    updatePagination();
}

// Update pagination
function updatePagination() {
    const totalPages = Math.ceil(dataTable.length / recordsPerPage);
    const pagination = document.getElementById('pagination');
    if (!pagination) return;
    
    pagination.innerHTML = `
        <li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="changePage(${currentPage - 1}); return false;">Previous</a>
        </li>
        ${Array.from({length: Math.min(5, totalPages)}, (_, i) => {
            const page = i + 1;
            return `
                <li class="page-item ${currentPage === page ? 'active' : ''}">
                    <a class="page-link" href="#" onclick="changePage(${page}); return false;">${page}</a>
                </li>
            `;
        }).join('')}
        <li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="changePage(${currentPage + 1}); return false;">Next</a>
        </li>
    `;
}

// Change page
function changePage(page) {
    const totalPages = Math.ceil(dataTable.length / recordsPerPage);
    if (page < 1 || page > totalPages) return;
    currentPage = page;
    renderTable();
}

// Setup event listeners
function setupEventListeners() {
    // Search input
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', filterData);
    }
    
    // Category filter
    const categoryFilter = document.getElementById('categoryFilter');
    if (categoryFilter) {
        categoryFilter.addEventListener('change', filterData);
    }
    
    // Date filter
    const startDate = document.getElementById('startDate');
    const endDate = document.getElementById('endDate');
    if (startDate && endDate) {
        startDate.addEventListener('change', filterData);
        endDate.addEventListener('change', filterData);
    }
}

// Filter data
function filterData() {
    const search = document.getElementById('searchInput')?.value.toLowerCase() || '';
    const category = document.getElementById('categoryFilter')?.value || '';
    const startDate = document.getElementById('startDate')?.value || '';
    const endDate = document.getElementById('endDate')?.value || '';
    
    loadDataTable().then(() => {
        dataTable = dataTable.filter(data => {
            const matchSearch = !search || 
                data.category.toLowerCase().includes(search) ||
                (data.label && data.label.toLowerCase().includes(search)) ||
                (data.source && data.source.toLowerCase().includes(search));
            
            const matchCategory = !category || data.category === category;
            
            const dataDate = new Date(data.timestamp);
            const matchStartDate = !startDate || dataDate >= new Date(startDate);
            const matchEndDate = !endDate || dataDate <= new Date(endDate);
            
            return matchSearch && matchCategory && matchStartDate && matchEndDate;
        });
        
        currentPage = 1;
        renderTable();
    });
}

// Generate bulk data
async function generateBulkData() {
    const count = prompt('How many sample data points to generate?', '50');
    if (!count) return;
    
    try {
        const response = await fetch(`/api/datapoints/generate/${count}`, {
            method: 'POST'
        });
        
        if (response.ok) {
            alert(`Generated ${count} data points successfully!`);
            loadDataStatistics();
            loadDataTable();
        }
    } catch (error) {
        console.error('Error generating data:', error);
        alert('Failed to generate data');
    }
}

// Export all data
async function exportAllData() {
    try {
        const response = await fetch('/api/datapoints');
        const data = await response.json();
        
        const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `data-export-${new Date().toISOString()}.json`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);
        
        alert('Data exported successfully!');
    } catch (error) {
        console.error('Error exporting data:', error);
        alert('Failed to export data');
    }
}

// View data details
function viewData(id) {
    const data = dataTable.find(d => d.id === id);
    if (!data) return;
    
    alert(`Data Details:\n\n${JSON.stringify(data, null, 2)}`);
}

// Delete data
async function deleteData(id) {
    if (!confirm('Are you sure you want to delete this data point?')) return;
    
    try {
        const response = await fetch(`/api/datapoints/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            alert('Data deleted successfully!');
            loadDataStatistics();
            loadDataTable();
        }
    } catch (error) {
        console.error('Error deleting data:', error);
        alert('Failed to delete data');
    }
}

// Add new data (form submission)
async function submitNewData() {
    const form = document.getElementById('addDataForm');
    if (!form) return;
    
    const formData = {
        category: document.getElementById('dataCategory').value,
        label: document.getElementById('dataLabel').value,
        value: parseFloat(document.getElementById('dataValue').value),
        unit: document.getElementById('dataUnit').value,
        source: document.getElementById('dataSource').value,
        description: document.getElementById('dataDescription').value
    };
    
    try {
        const response = await fetch('/api/datapoints', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });
        
        if (response.ok) {
            alert('Data added successfully!');
            form.reset();
            bootstrap.Modal.getInstance(document.getElementById('addDataModal')).hide();
            loadDataStatistics();
            loadDataTable();
        }
    } catch (error) {
        console.error('Error adding data:', error);
        alert('Failed to add data');
    }
}
