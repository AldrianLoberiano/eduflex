<template>
  <div class="sidebar-wrapper" :class="{ 'sidebar-open': isOpen }">
    <!-- Overlay for mobile -->
    <div class="sidebar-overlay" @click="closeSidebar" v-if="isOpen"></div>
    
    <!-- Sidebar -->
    <aside class="sidebar" :class="{ 'sidebar-open': isOpen }">
      <!-- Sidebar Header -->
      <div class="sidebar-header">
        <div class="logo">
          <span class="logo-icon">🎓</span>
          <span class="logo-text">EduFlex</span>
        </div>
        <button class="close-btn" @click="closeSidebar">×</button>
      </div>

      <!-- User Info -->
      <div class="user-info" v-if="user">
        <div class="user-avatar">{{ user.name?.charAt(0)?.toUpperCase() || 'U' }}</div>
        <div class="user-details">
          <div class="user-name">{{ user.name }}</div>
          <div class="user-role">{{ getRoleLabel(user.role) }}</div>
        </div>
      </div>

      <!-- Navigation -->
      <nav class="sidebar-nav">
        <!-- Common Links -->
        <div class="nav-section">
          <div class="nav-section-title">Main</div>
          <router-link to="/" class="nav-item" @click="closeSidebar">
            <span class="nav-icon">🏠</span>
            <span class="nav-text">Home</span>
          </router-link>
          <router-link to="/courses" class="nav-item" @click="closeSidebar">
            <span class="nav-icon">📚</span>
            <span class="nav-text">All Courses</span>
          </router-link>
        </div>

        <!-- Instructor Links -->
        <div class="nav-section" v-if="user?.role === 'INSTRUCTOR'">
          <div class="nav-section-title">Instructor</div>
          <router-link to="/instructor/dashboard" class="nav-item" @click="closeSidebar">
            <span class="nav-icon">📊</span>
            <span class="nav-text">Dashboard</span>
          </router-link>
          <router-link to="/instructor/courses/new" class="nav-item" @click="closeSidebar">
            <span class="nav-icon">➕</span>
            <span class="nav-text">Create Course</span>
          </router-link>
        </div>

        <!-- Student Links -->
        <div class="nav-section" v-if="user?.role === 'STUDENT'">
          <div class="nav-section-title">Student</div>
          <router-link to="/student/my-learning" class="nav-item" @click="closeSidebar">
            <span class="nav-icon">🎯</span>
            <span class="nav-text">My Learning</span>
          </router-link>
        </div>

        <!-- Admin Links -->
        <div class="nav-section" v-if="user?.role === 'ADMIN'">
          <div class="nav-section-title">Admin</div>
          <router-link to="/admin/dashboard" class="nav-item" @click="closeSidebar">
            <span class="nav-icon">⚙️</span>
            <span class="nav-text">Admin Dashboard</span>
          </router-link>
          <router-link to="/instructor/dashboard" class="nav-item" @click="closeSidebar">
            <span class="nav-icon">📊</span>
            <span class="nav-text">Instructor View</span>
          </router-link>
          <router-link to="/student/my-learning" class="nav-item" @click="closeSidebar">
            <span class="nav-icon">🎯</span>
            <span class="nav-text">Student View</span>
          </router-link>
        </div>

        <!-- Settings Section -->
        <div class="nav-section" v-if="user">
          <div class="nav-section-title">Account</div>
          <a href="#" class="nav-item" @click.prevent="handleLogout">
            <span class="nav-icon">🚪</span>
            <span class="nav-text">Logout</span>
          </a>
        </div>

        <!-- Guest Links -->
        <div class="nav-section" v-if="!user">
          <div class="nav-section-title">Get Started</div>
          <a href="#" class="nav-item" @click.prevent="$emit('open-login')">
            <span class="nav-icon">🔑</span>
            <span class="nav-text">Login</span>
          </a>
          <a href="#" class="nav-item" @click.prevent="$emit('open-register')">
            <span class="nav-icon">✨</span>
            <span class="nav-text">Sign Up</span>
          </a>
        </div>
      </nav>

      <!-- Footer -->
      <div class="sidebar-footer">
        <div class="footer-text">© 2026 EduFlex</div>
      </div>
    </aside>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '../../services'

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close', 'open-login', 'open-register'])
const router = useRouter()

const user = computed(() => authService.getCurrentUser())

const closeSidebar = () => {
  emit('close')
}

const getRoleLabel = (role) => {
  const labels = {
    'INSTRUCTOR': 'Instructor',
    'STUDENT': 'Student',
    'ADMIN': 'Administrator'
  }
  return labels[role] || role
}

const handleLogout = () => {
  authService.logout()
  closeSidebar()
  router.push('/')
}
</script>

<style scoped>
.sidebar-wrapper {
  position: relative;
}

.sidebar-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  z-index: 998;
  opacity: 0;
  visibility: hidden;
  transition: opacity 0.3s, visibility 0.3s;
}

.sidebar-wrapper.sidebar-open .sidebar-overlay {
  opacity: 1;
  visibility: visible;
}

.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 280px;
  height: 100vh;
  background: #ffffff;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  z-index: 999;
  display: flex;
  flex-direction: column;
  transform: translateX(-100%);
  transition: transform 0.3s ease;
  overflow-y: auto;
}

.sidebar.sidebar-open {
  transform: translateX(0);
}

/* Sidebar Header */
.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  border-bottom: 1px solid #e5e7eb;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon {
  font-size: 28px;
}

.logo-text {
  font-size: 24px;
  font-weight: 700;
  color: #00ff88;
}

.close-btn {
  background: none;
  border: none;
  font-size: 32px;
  color: #6b7280;
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: background 0.2s;
}

.close-btn:hover {
  background: #f3f4f6;
}

/* User Info */
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px;
  border-bottom: 1px solid #e5e7eb;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 600;
}

.user-details {
  flex: 1;
}

.user-name {
  font-weight: 600;
  color: #111827;
  font-size: 16px;
  margin-bottom: 2px;
}

.user-role {
  font-size: 13px;
  color: #6b7280;
}

/* Navigation */
.sidebar-nav {
  flex: 1;
  padding: 10px 0;
  overflow-y: auto;
}

.nav-section {
  margin-bottom: 20px;
}

.nav-section-title {
  padding: 10px 20px;
  font-size: 12px;
  font-weight: 600;
  color: #9ca3af;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  color: #374151;
  text-decoration: none;
  transition: all 0.2s;
  position: relative;
}

.nav-item:hover {
  background: #f9fafb;
  color: #00ff88;
}

.nav-item.router-link-active {
  background: #eef2ff;
  color: #00ff88;
  font-weight: 600;
}

.nav-item.router-link-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: #00ff88;
}

.nav-icon {
  font-size: 20px;
  width: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-text {
  font-size: 15px;
}

/* Sidebar Footer */
.sidebar-footer {
  padding: 20px;
  border-top: 1px solid #e5e7eb;
  text-align: center;
}

.footer-text {
  font-size: 12px;
  color: #9ca3af;
}

/* Desktop: Show sidebar by default */
@media (min-width: 1024px) {
  .sidebar {
    transform: translateX(0);
  }
  
  .sidebar-overlay {
    display: none;
  }
  
  .close-btn {
    display: none;
  }
}

/* Mobile: Hide sidebar by default */
@media (max-width: 1023px) {
  .sidebar {
    box-shadow: 2px 0 20px rgba(0, 0, 0, 0.15);
  }
}

/* Scrollbar Styling */
.sidebar::-webkit-scrollbar {
  width: 6px;
}

.sidebar::-webkit-scrollbar-track {
  background: #f3f4f6;
}

.sidebar::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 3px;
}

.sidebar::-webkit-scrollbar-thumb:hover {
  background: #9ca3af;
}
</style>

