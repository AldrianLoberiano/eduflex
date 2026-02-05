<template>
  <div class="subscriptions">
    <div class="page-header">
      <h1>Subscription Management</h1>
      <div class="header-actions">
        <button class="btn btn-primary">
          <i class="fas fa-plus"></i> Create Plan
        </button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <i class="fas fa-users stat-icon"></i>
        <div class="stat-content">
          <div class="stat-value">0</div>
          <div class="stat-label">Active Subscriptions</div>
        </div>
      </div>
      <div class="stat-card">
        <i class="fas fa-dollar-sign stat-icon success"></i>
        <div class="stat-content">
          <div class="stat-value">$0</div>
          <div class="stat-label">Monthly Revenue</div>
        </div>
      </div>
      <div class="stat-card">
        <i class="fas fa-sync stat-icon primary"></i>
        <div class="stat-content">
          <div class="stat-value">0</div>
          <div class="stat-label">Renewals This Month</div>
        </div>
      </div>
      <div class="stat-card">
        <i class="fas fa-exclamation-triangle stat-icon warning"></i>
        <div class="stat-content">
          <div class="stat-value">0</div>
          <div class="stat-label">Expiring Soon</div>
        </div>
      </div>
    </div>

    <div class="plans-grid">
      <div class="plan-card">
        <div class="plan-header">
          <h3>Basic Plan</h3>
          <div class="plan-price">
            <span class="currency">$</span>
            <span class="amount">9.99</span>
            <span class="period">/month</span>
          </div>
        </div>
        <ul class="plan-features">
          <li><i class="fas fa-check"></i> Access to basic courses</li>
          <li><i class="fas fa-check"></i> Community support</li>
          <li><i class="fas fa-check"></i> Certificate of completion</li>
        </ul>
        <div class="plan-stats">
          <div class="stat-item">
            <span class="stat-value">0</span>
            <span class="stat-label">Subscribers</span>
          </div>
        </div>
        <button class="btn-manage">Manage Plan</button>
      </div>

      <div class="plan-card featured">
        <div class="badge">Most Popular</div>
        <div class="plan-header">
          <h3>Premium Plan</h3>
          <div class="plan-price">
            <span class="currency">$</span>
            <span class="amount">29.99</span>
            <span class="period">/month</span>
          </div>
        </div>
        <ul class="plan-features">
          <li><i class="fas fa-check"></i> Access to all courses</li>
          <li><i class="fas fa-check"></i> Priority support</li>
          <li><i class="fas fa-check"></i> Verified certificates</li>
          <li><i class="fas fa-check"></i> Downloadable resources</li>
        </ul>
        <div class="plan-stats">
          <div class="stat-item">
            <span class="stat-value">0</span>
            <span class="stat-label">Subscribers</span>
          </div>
        </div>
        <button class="btn-manage">Manage Plan</button>
      </div>

      <div class="plan-card">
        <div class="plan-header">
          <h3>Enterprise Plan</h3>
          <div class="plan-price">
            <span class="currency">$</span>
            <span class="amount">99.99</span>
            <span class="period">/month</span>
          </div>
        </div>
        <ul class="plan-features">
          <li><i class="fas fa-check"></i> Everything in Premium</li>
          <li><i class="fas fa-check"></i> Custom courses</li>
          <li><i class="fas fa-check"></i> Dedicated support</li>
          <li><i class="fas fa-check"></i> API access</li>
        </ul>
        <div class="plan-stats">
          <div class="stat-item">
            <span class="stat-value">0</span>
            <span class="stat-label">Subscribers</span>
          </div>
        </div>
        <button class="btn-manage">Manage Plan</button>
      </div>
    </div>

    <div class="content-card">
      <h2>Recent Subscriptions</h2>
      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>User</th>
              <th>Plan</th>
              <th>Start Date</th>
              <th>End Date</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td colspan="6" class="empty-state">
                <i class="fas fa-crown fa-3x"></i>
                <p>No subscription records found</p>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
// Placeholder for subscriptions functionality
</script>

<style scoped>
.subscriptions {
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

.plans-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.plan-card {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  position: relative;
}

.plan-card.featured {
  border: 2px solid #4CAF50;
}

.badge {
  position: absolute;
  top: 20px;
  right: 20px;
  background: #4CAF50;
  color: white;
  padding: 5px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
}

.plan-header h3 {
  margin: 0 0 10px 0;
  font-size: 24px;
}

.plan-price {
  margin-bottom: 20px;
}

.currency {
  font-size: 24px;
  vertical-align: top;
}

.amount {
  font-size: 48px;
  font-weight: bold;
  color: #4CAF50;
}

.period {
  font-size: 16px;
  color: #666;
}

.plan-features {
  list-style: none;
  padding: 0;
  margin: 0 0 20px 0;
}

.plan-features li {
  padding: 10px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.plan-features i {
  color: #4CAF50;
}

.plan-stats {
  border-top: 1px solid #eee;
  padding-top: 20px;
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
}

.stat-item .stat-value {
  display: block;
  font-size: 32px;
  font-weight: bold;
  color: #333;
}

.stat-item .stat-label {
  display: block;
  color: #666;
  font-size: 14px;
}

.btn-manage {
  width: 100%;
  padding: 12px;
  border: none;
  background: #4CAF50;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
}

.btn-manage:hover {
  background: #45a049;
}

.content-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  padding: 20px;
}

.content-card h2 {
  margin: 0 0 20px 0;
  font-size: 20px;
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
