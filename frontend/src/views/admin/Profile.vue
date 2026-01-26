<template>
  <div class="admin-profile-page">
    <div class="profile-header">
      <h1>Admin Profile</h1>
      <p class="text-muted">Manage your administrator account details</p>
    </div>

    <div class="profile-card card">
      <div class="profile-avatar">
        {{ initials }}
      </div>
      <div class="profile-details">
        <h2>{{ fullName }}</h2>
        <p class="text-muted">{{ authStore.user?.email || 'No email available' }}</p>
        <span class="badge badge-primary">Administrator</span>
      </div>
    </div>

    <div class="profile-grid">
      <div class="card info-card">
        <h3>Account Details</h3>
        <div class="info-row">
          <span class="label">First Name</span>
          <span class="value">{{ authStore.user?.firstName || '—' }}</span>
        </div>
        <div class="info-row">
          <span class="label">Last Name</span>
          <span class="value">{{ authStore.user?.lastName || '—' }}</span>
        </div>
        <div class="info-row">
          <span class="label">Email</span>
          <span class="value">{{ authStore.user?.email || '—' }}</span>
        </div>
        <div class="info-row">
          <span class="label">Role</span>
          <span class="value">Admin</span>
        </div>
      </div>

      <div class="card info-card">
        <h3>Security</h3>
        <p class="text-muted">Keep your account secure by using a strong password.</p>
        <button class="btn btn-secondary" disabled>
          Change Password (Coming soon)
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAuthStore } from '../../stores/auth'

const authStore = useAuthStore()

const fullName = computed(() => {
  const first = authStore.user?.firstName || ''
  const last = authStore.user?.lastName || ''
  return `${first} ${last}`.trim() || 'Admin'
})

const initials = computed(() => {
  const first = authStore.user?.firstName?.charAt(0) || ''
  const last = authStore.user?.lastName?.charAt(0) || ''
  return (first + last).toUpperCase() || 'A'
})
</script>

<style scoped>
.admin-profile-page {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.profile-header h1 {
  margin-bottom: 6px;
}

.profile-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
}

.profile-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 28px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
}

.profile-details h2 {
  margin-bottom: 6px;
}

.profile-grid {
  display: grid;
  gap: 20px;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
}

.info-card {
  padding: 22px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #e5e7eb;
}

.info-row:last-child {
  border-bottom: none;
}

.label {
  color: #6b7280;
}

.value {
  font-weight: 600;
  color: #111827;
}
</style>
