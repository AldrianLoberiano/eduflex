<template>
  <div class="sidebar-wrapper">
    <!-- Sidebar -->
    <aside class="sidebar">
      <!-- Sidebar Header -->
      <div class="sidebar-header">
        <div class="logo">
          <svg width="32" height="32" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
            <defs>
              <linearGradient id="capGradientSidebar" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" style="stop-color:#5B6FE3;stop-opacity:1" />
                <stop offset="100%" style="stop-color:#FB7D5B;stop-opacity:1" />
              </linearGradient>
            </defs>
            <path d="M16 2L2 10v3h28v-3L16 2z" fill="url(#capGradientSidebar)"/>
            <rect x="14" y="13" width="4" height="17" fill="#5B6FE3"/>
            <path d="M8 16h16v2H8z" fill="#FCC43E"/>
          </svg>
          <span class="logo-text">EduFlex</span>
        </div>
      </div>

      <!-- User Info -->
      <div class="user-info" v-if="user">
        <div class="user-avatar">{{ user.name?.charAt(0)?.toUpperCase() || 'U' }}</div>
      </div>

      <!-- Navigation -->
      <nav class="sidebar-nav">
        <!-- Instructor Links -->
        <div class="nav-section" v-if="user?.role === 'INSTRUCTOR'">
          <div class="nav-section-title">Instructor</div>
          <router-link to="/instructor/dashboard" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <rect x="3" y="3" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
              <rect x="14" y="3" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
              <rect x="3" y="14" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
              <rect x="14" y="14" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
            </svg>
            <span class="nav-text">Dashboard</span>
          </router-link>
          <router-link to="/instructor/courses" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">My Courses</span>
          </router-link>
          <router-link to="/instructor/courses/new" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Create Course</span>
          </router-link>
        </div>

        <!-- Student Links -->
        <div class="nav-section" v-if="user?.role === 'STUDENT'">
          <div class="nav-section-title">Student</div>
          <router-link to="/student/dashboard" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <rect x="3" y="3" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
              <rect x="14" y="3" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
              <rect x="3" y="14" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
              <rect x="14" y="14" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
            </svg>
            <span class="nav-text">Dashboard</span>
          </router-link>
          <router-link to="/student/my-learning" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M12 2L2 7l10 5 10-5-10-5z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
              <path d="M2 17l10 5 10-5M2 12l10 5 10-5" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
            </svg>
            <span class="nav-text">My Learning</span>
          </router-link>
        </div>

        <!-- Admin Links - 13 Features -->
        <div class="nav-section" v-if="user?.role === 'ADMIN'">
          <div class="nav-section-title">
            Admin Panel
            <span class="feature-count">13 New Features</span>
          </div>
          
          <!-- 1. Dashboard -->
          <router-link to="/admin/dashboard" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <rect x="3" y="3" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
              <rect x="14" y="3" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
              <rect x="3" y="14" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
              <rect x="14" y="14" width="7" height="7" rx="1" stroke="currentColor" stroke-width="2"/>
            </svg>
            <span class="nav-text">Dashboard</span>
          </router-link>
          
          <!-- 2. User Management -->
          <router-link to="/admin/users" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <circle cx="9" cy="7" r="4" stroke="currentColor" stroke-width="2"/>
              <path d="M23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">User Management</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- Sub: User Approvals -->
          <router-link to="/admin/users/approvals" class="nav-item nav-sub-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="18" height="18" viewBox="0 0 24 24" fill="none">
              <path d="M9 12l2 2 4-4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
            </svg>
            <span class="nav-text">Approve Users</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- 3. Course Management -->
          <router-link to="/admin/courses" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Course Management</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- Sub: Course Categories -->
          <router-link to="/admin/courses/categories" class="nav-item nav-sub-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="18" height="18" viewBox="0 0 24 24" fill="none">
              <path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z" stroke="currentColor" stroke-width="2"/>
            </svg>
            <span class="nav-text">Categories</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- 4. Subject / Module Management -->
          <router-link to="/admin/modules" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <rect x="3" y="3" width="18" height="18" rx="2" stroke="currentColor" stroke-width="2"/>
              <path d="M3 9h18M3 15h18M9 3v18" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Module Management</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- 5. Content Management -->
          <router-link to="/admin/content" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M13 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V9z" stroke="currentColor" stroke-width="2"/>
              <path d="M13 2v7h7" stroke="currentColor" stroke-width="2"/>
              <path d="M8 13h8M8 17h8" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Content Management</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- 6. Enrollment Management -->
          <router-link to="/admin/enrollments" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" stroke="currentColor" stroke-width="2"/>
              <circle cx="8.5" cy="7" r="4" stroke="currentColor" stroke-width="2"/>
              <polyline points="17 11 19 13 23 9" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Enrollment Management</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- 7. Assessment & Grading -->
          <router-link to="/admin/assessments" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M9 11l3 3L22 4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11" stroke="currentColor" stroke-width="2"/>
            </svg>
            <span class="nav-text">Assessment & Grading</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- Sub: Grading Management -->
          <router-link to="/admin/grading" class="nav-item nav-sub-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="18" height="18" viewBox="0 0 24 24" fill="none">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" stroke="currentColor" stroke-width="2"/>
              <path d="M14 2v6h6M16 13H8M16 17H8M10 9H8" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Grading</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- 8. Attendance Management -->
          <router-link to="/admin/attendance" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <rect x="3" y="4" width="18" height="18" rx="2" stroke="currentColor" stroke-width="2"/>
              <path d="M16 2v4M8 2v4M3 10h18" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <path d="M8 14h.01M12 14h.01M16 14h.01M8 18h.01M12 18h.01M16 18h.01" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Attendance Management</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- 9. Communication Tools -->
          <router-link to="/admin/communication" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" stroke="currentColor" stroke-width="2"/>
              <path d="M8 10h8M8 14h4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Communication Tools</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- Sub: Announcements -->
          <router-link to="/admin/announcements" class="nav-item nav-sub-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="18" height="18" viewBox="0 0 24 24" fill="none">
              <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9M13.73 21a2 2 0 0 1-3.46 0" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Announcements</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- 10. Reports & Analytics -->
          <router-link to="/admin/reports" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M3 3v18h18" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <path d="M18 17V9M13 17v-6M8 17v-4" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Reports & Analytics</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- 11. Payment & Subscription -->
          <router-link to="/admin/payments" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <rect x="1" y="4" width="22" height="16" rx="2" stroke="currentColor" stroke-width="2"/>
              <path d="M1 10h22" stroke="currentColor" stroke-width="2"/>
            </svg>
            <span class="nav-text">Payment & Subscription</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- Sub: Subscriptions -->
          <router-link to="/admin/subscriptions" class="nav-item nav-sub-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="18" height="18" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
              <path d="M12 6v6l4 2" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Subscriptions</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- 12. System Settings -->
          <router-link to="/admin/settings" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2"/>
              <path d="M12 1v6m0 6v6M23 12h-6m-6 0H1" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <path d="m20 20-4-4m0 8 4-4M4 20l4-4M8 4 4 8" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">System Settings</span>
            <span class="badge-new">NEW</span>
          </router-link>
          
          <!-- 13. Content Moderation -->
          <router-link to="/admin/moderation" class="nav-item" @click="closeSidebar">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" stroke="currentColor" stroke-width="2"/>
              <path d="M9 12l2 2 4-4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <span class="nav-text">Content Moderation</span>
            <span class="badge-new">NEW</span>
          </router-link>
        </div>

        <!-- Settings Section -->
        <div class="nav-section" v-if="user">
          <div class="nav-section-title">Account</div>
          <a href="#" class="nav-item" @click.prevent="handleLogout">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4M16 17l5-5-5-5M21 12H9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <span class="nav-text">Logout</span>
          </a>
        </div>

        <!-- Guest Links -->
        <div class="nav-section" v-if="!user">
          <div class="nav-section-title">Get Started</div>
          <a href="#" class="nav-item" @click.prevent="$emit('open-login')">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4M10 17l5-5-5-5M15 12H3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <span class="nav-text">Login</span>
          </a>
          <a href="#" class="nav-item" @click.prevent="$emit('open-register')">
            <svg class="nav-icon-svg" width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <circle cx="8.5" cy="7" r="4" stroke="currentColor" stroke-width="2"/>
              <path d="M20 8v6M23 11h-6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <span class="nav-text">Sign Up</span>
          </a>
        </div>
      </nav>
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

.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 260px;
  height: 100vh;
  background: #3D4F6D;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  z-index: 999;
  display: flex;
  flex-direction: column;
  transform: translateX(0);
  overflow-y: auto;
}

.sidebar::-webkit-scrollbar {
  width: 6px;
}

.sidebar::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}

/* Sidebar Header */
.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
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
  font-size: 20px;
  font-weight: 700;
  color: #ffffff;
}

.close-btn {
  background: none;
  border: none;
  font-size: 32px;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: all 0.2s;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #ffffff;
}

/* User Info */
.user-info {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: #5B6FE3;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(91, 111, 227, 0.3);
}

.user-details {
  flex: 1;
}

.user-name {
  font-weight: 600;
  color: #ffffff;
  font-size: 16px;
  margin-bottom: 2px;
}

.user-role {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.7);
}

/* Navigation */
.sidebar-nav {
  flex: 1;
  padding: 10px 0;
  overflow-y: auto;
}

.sidebar-nav::-webkit-scrollbar {
  width: 6px;
}

.sidebar-nav::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar-nav::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}

.nav-section {
  margin-bottom: 20px;
}

.nav-section-title {
  padding: 10px 20px;
  font-size: 12px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.5);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.feature-count {
  background: linear-gradient(135deg, #5B6FE3 0%, #8B5CF6 100%);
  color: #ffffff;
  font-size: 9px;
  padding: 3px 8px;
  border-radius: 10px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.7; }
}

.badge-new {
  background: linear-gradient(135deg, #00ff88 0%, #00cc70 100%);
  color: #003d29;
  font-size: 8px;
  padding: 2px 6px;
  border-radius: 8px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-left: auto;
  animation: shimmer 2s ease-in-out infinite;
}

@keyframes shimmer {
  0%, 100% { filter: brightness(1); }
  50% { filter: brightness(1.3); }
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  transition: all 0.2s;
  cursor: pointer;
  position: relative;
}

.nav-sub-item {
  padding-left: 40px;
  font-size: 13px;
}

.nav-sub-item .nav-icon-svg {
  width: 18px;
  height: 18px;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #ffffff;
}

.nav-item.router-link-active {
  background: rgba(91, 111, 227, 0.2);
  color: #ffffff;
  border-left: 3px solid #5B6FE3;
}

.nav-icon {
  font-size: 20px;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-icon-svg {
  width: 20px;
  height: 20px;
  flex-shrink: 0;
}

.nav-text {
  font-size: 14px;
  font-weight: 500;
}

/* Footer */
.sidebar-footer {
  padding: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  text-align: center;
}

.footer-text {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

/* Mobile Responsive */
@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
  }
  
  .sidebar.mobile-open {
    transform: translateX(0);
  }
}
</style>

