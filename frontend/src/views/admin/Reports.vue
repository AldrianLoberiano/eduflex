<template>
  <div class="reports">
    <div class="page-header">
      <h1>Reports & Analytics</h1>
      <div class="header-actions">
        <button class="btn btn-secondary">
          <i class="fas fa-calendar"></i> Date Range
        </button>
        <button class="btn btn-primary">
          <i class="fas fa-download"></i> Export Report
        </button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <i class="fas fa-users stat-icon"></i>
        <div class="stat-content">
          <div class="stat-value">0</div>
          <div class="stat-label">Total Users</div>
        </div>
      </div>
      <div class="stat-card">
        <i class="fas fa-book stat-icon success"></i>
        <div class="stat-content">
          <div class="stat-value">0</div>
          <div class="stat-label">Active Courses</div>
        </div>
      </div>
      <div class="stat-card">
        <i class="fas fa-graduation-cap stat-icon primary"></i>
        <div class="stat-content">
          <div class="stat-value">0</div>
          <div class="stat-label">Completions</div>
        </div>
      </div>
      <div class="stat-card">
        <i class="fas fa-dollar-sign stat-icon warning"></i>
        <div class="stat-content">
          <div class="stat-value">$0</div>
          <div class="stat-label">Revenue</div>
        </div>
      </div>
    </div>

    <div class="reports-grid">
      <div class="report-card">
        <div class="report-icon">
          <i class="fas fa-chart-bar"></i>
        </div>
        <h3>Enrollment Report</h3>
        <p>Track enrollment trends and patterns</p>
        <button class="btn-generate">Generate Report</button>
      </div>

      <div class="report-card">
        <div class="report-icon">
          <i class="fas fa-chart-line"></i>
        </div>
        <h3>Performance Analytics</h3>
        <p>Student performance and progress metrics</p>
        <button class="btn-generate">Generate Report</button>
      </div>

      <div class="report-card">
        <div class="report-icon">
          <i class="fas fa-chart-pie"></i>
        </div>
        <h3>Course Analytics</h3>
        <p>Course completion and engagement rates</p>
        <button class="btn-generate">Generate Report</button>
      </div>

      <div class="report-card">
        <div class="report-icon">
          <i class="fas fa-money-bill-wave"></i>
        </div>
        <h3>Revenue Report</h3>
        <p>Financial performance and transactions</p>
        <button class="btn-generate">Generate Report</button>
      </div>

      <div class="report-card">
        <div class="report-icon">
          <i class="fas fa-user-graduate"></i>
        </div>
        <h3>Student Report</h3>
        <p>Comprehensive student activity data</p>
        <button class="btn-generate">Generate Report</button>
      </div>

      <div class="report-card">
        <div class="report-icon">
          <i class="fas fa-chalkboard-teacher"></i>
        </div>
        <h3>Instructor Report</h3>
        <p>Instructor performance and workload</p>
        <button class="btn-generate">Generate Report</button>
      </div>
    </div>
  </div>
</template>

<script setup>
// Placeholder for reports functionality
</script>

<style scoped>
.reports {
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
  margin-bottom: 30px;
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

.stat-icon.warning {
  color: #FFC107;
}

.stat-icon.primary {
  color: #9C27B0;
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

.reports-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.report-card {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  text-align: center;
}

.report-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 32px;
}

.report-card h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
}

.report-card p {
  margin: 0 0 20px 0;
  color: #666;
  font-size: 14px;
}

.btn-generate {
  width: 100%;
  padding: 10px;
  border: none;
  background: #4CAF50;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-generate:hover {
  background: #45a049;
}
</style>
