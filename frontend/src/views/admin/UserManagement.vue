<template>
  <div class="admin-page">
    <div class="page-header">
      <h1>User Management</h1>
      <p class="text-muted">Manage all users, roles, and permissions</p>
    </div>

    <div class="actions-bar">
      <div class="search-box">
        <input type="text" placeholder="Search users..." v-model="searchQuery" />
      </div>
      <div class="action-buttons">
        <button class="btn btn-secondary">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" style="margin-right: 8px;">
            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4M17 8l-5-5-5 5M12 3v12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
          Bulk Upload (CSV)
        </button>
        <button class="btn btn-primary">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" style="margin-right: 8px;">
            <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          Add User
        </button>
      </div>
    </div>

    <div class="filter-tabs">
      <button class="tab" :class="{ active: activeTab === 'all' }" @click="activeTab = 'all'">
        All Users ({{ stats.total }})
      </button>
      <button class="tab" :class="{ active: activeTab === 'students' }" @click="activeTab = 'students'">
        Students ({{ stats.students }})
      </button>
      <button class="tab" :class="{ active: activeTab === 'instructors' }" @click="activeTab = 'instructors'">
        Instructors ({{ stats.instructors }})
      </button>
      <button class="tab" :class="{ active: activeTab === 'admins' }" @click="activeTab = 'admins'">
        Admins ({{ stats.admins }})
      </button>
      <button class="tab" :class="{ active: activeTab === 'inactive' }" @click="activeTab = 'inactive'">
        Inactive ({{ stats.inactive }})
      </button>
    </div>

    <div class="card">
      <table class="data-table">
        <thead>
          <tr>
            <th><input type="checkbox" /></th>
            <th>User</th>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
            <th>Registered</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td colspan="7" class="text-center" style="padding: 40px;">
              <svg width="64" height="64" viewBox="0 0 24 24" fill="none" style="margin: 0 auto 16px; opacity: 0.5;">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                <circle cx="9" cy="7" r="4" stroke="currentColor" stroke-width="2"/>
                <path d="M23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
              <p style="color: #6b7280;">User management interface coming soon</p>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const searchQuery = ref('')
const activeTab = ref('all')
const stats = ref({
  total: 0,
  students: 0,
  instructors: 0,
  admins: 0,
  inactive: 0
})
</script>

<style scoped>
.admin-page {
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 32px;
  margin-bottom: 8px;
  color: #e0e0e0;
}

.text-muted {
  color: #9ca3af;
  font-size: 16px;
}

.actions-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  gap: 20px;
}

.search-box {
  flex: 1;
  max-width: 400px;
}

.search-box input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 8px;
  background: rgba(26, 26, 26, 0.8);
  color: #e0e0e0;
  font-size: 14px;
}

.search-box input:focus {
  outline: none;
  border-color: #00ff88;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
}

.btn-primary {
  background: linear-gradient(135deg, #00ff88 0%, #00d4ff 100%);
  color: #0a0a0a;
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.1);
  color: #e0e0e0;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.btn:hover {
  transform: translateY(-2px);
}

.filter-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  border-bottom: 2px solid rgba(0, 212, 255, 0.2);
  padding-bottom: -2px;
}

.tab {
  padding: 12px 24px;
  background: none;
  border: none;
  border-bottom: 3px solid transparent;
  color: #9ca3af;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.tab:hover {
  color: #e0e0e0;
}

.tab.active {
  color: #00ff88;
  border-bottom-color: #00ff88;
}

.card {
  background: linear-gradient(135deg, #1a1a1a 0%, #2a2a2a 100%);
  border-radius: 16px;
  border: 1px solid rgba(0, 212, 255, 0.2);
  overflow: hidden;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.data-table th {
  background: rgba(0, 0, 0, 0.3);
  font-weight: 600;
  color: #9ca3af;
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.data-table td {
  color: #e0e0e0;
}

.text-center {
  text-align: center;
}
</style>
