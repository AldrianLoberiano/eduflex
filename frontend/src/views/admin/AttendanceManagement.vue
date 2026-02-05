<template>
  <div class="attendance-management">
    <div class="page-header">
      <h1>Attendance Management</h1>
      <div class="header-actions">
        <button class="btn btn-secondary">
          <i class="fas fa-filter"></i> Filter
        </button>
        <button class="btn btn-secondary">
          <i class="fas fa-download"></i> Export Report
        </button>
        <button class="btn btn-primary">
          <i class="fas fa-check"></i> Mark Attendance
        </button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <i class="fas fa-users stat-icon"></i>
        <div class="stat-content">
          <div class="stat-value">0%</div>
          <div class="stat-label">Overall Attendance</div>
        </div>
      </div>
      <div class="stat-card">
        <i class="fas fa-user-check stat-icon success"></i>
        <div class="stat-content">
          <div class="stat-value">0</div>
          <div class="stat-label">Present Today</div>
        </div>
      </div>
      <div class="stat-card">
        <i class="fas fa-user-times stat-icon danger"></i>
        <div class="stat-content">
          <div class="stat-value">0</div>
          <div class="stat-label">Absent Today</div>
        </div>
      </div>
      <div class="stat-card">
        <i class="fas fa-exclamation-triangle stat-icon warning"></i>
        <div class="stat-content">
          <div class="stat-value">0</div>
          <div class="stat-label">Low Attendance</div>
        </div>
      </div>
    </div>

    <div class="content-card">
      <div class="search-bar">
        <i class="fas fa-search"></i>
        <input type="text" placeholder="Search by student name, course, or date..." />
      </div>

      <div class="filter-tabs">
        <button class="tab-btn active">All Records</button>
        <button class="tab-btn">Today</button>
        <button class="tab-btn">This Week</button>
        <button class="tab-btn">This Month</button>
        <button class="tab-btn">Low Attendance</button>
      </div>

      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>Student</th>
              <th>Course</th>
              <th>Date</th>
              <th>Status</th>
              <th>Attendance Rate</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td colspan="6" class="empty-state">
                <i class="fas fa-calendar-check fa-3x"></i>
                <p>No attendance records found</p>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
// Placeholder for attendance management functionality
</script>

<style scoped>
.attendance-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-primary {
  background: #4CAF50;
  color: white;
}

.btn-secondary {
  background: #f5f5f5;
  color: #333;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  font-size: 2em;
  color: #2196F3;
}

.stat-icon.success {
  color: #4CAF50;
}

.stat-icon.danger {
  color: #f44336;
}

.stat-icon.warning {
  color: #FFC107;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 2em;
  font-weight: bold;
  color: #333;
}

.stat-label {
  color: #666;
  font-size: 0.9em;
}

.content-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  overflow: hidden;
}

.search-bar {
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  border-bottom: 1px solid #eee;
}

.search-bar i {
  color: #999;
}

.search-bar input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 14px;
}

.filter-tabs {
  display: flex;
  border-bottom: 1px solid #eee;
}

.tab-btn {
  padding: 15px 20px;
  border: none;
  background: none;
  cursor: pointer;
  border-bottom: 2px solid transparent;
}

.tab-btn.active {
  color: #4CAF50;
  border-bottom-color: #4CAF50;
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table thead {
  background: #f5f5f5;
}

.data-table th,
.data-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty-state i {
  color: #ddd;
  margin-bottom: 20px;
}

.empty-state p {
  margin: 10px 0 0 0;
  font-size: 1.1em;
}
</style>
