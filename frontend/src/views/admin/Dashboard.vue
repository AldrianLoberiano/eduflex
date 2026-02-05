<template>
  <div class="admin-dashboard">
    <h1>Admin Dashboard</h1>
    <p class="text-muted mb-0">System overview and management</p>

    <div class="stats-grid grid grid-4">
      <div class="stat-card card">
        <div class="stat-icon" style="background: #3498db;">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" stroke="white" stroke-width="2" stroke-linecap="round"/>
            <circle cx="9" cy="7" r="4" stroke="white" stroke-width="2"/>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75" stroke="white" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <h3>Total Users</h3>
          <p class="stat-value">{{ stats.totalUsers }}</p>
          <span class="stat-label">All registered users</span>
        </div>
      </div>

      <div class="stat-card card">
        <div class="stat-icon" style="background: #9b59b6;">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" stroke="white" stroke-width="2" stroke-linecap="round"/>
            <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" stroke="white" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <h3>Total Courses</h3>
          <p class="stat-value">{{ stats.totalCourses }}</p>
          <span class="stat-label">Published & Draft</span>
        </div>
      </div>

      <div class="stat-card card">
        <div class="stat-icon" style="background: #27ae60;">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M9 12l2 2 4-4" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <circle cx="12" cy="12" r="10" stroke="white" stroke-width="2"/>
          </svg>
        </div>
        <div class="stat-info">
          <h3>Active Enrollments</h3>
          <p class="stat-value">{{ stats.activeEnrollments }}</p>
          <span class="stat-label">Students enrolled</span>
        </div>
      </div>

      <div class="stat-card card">
        <div class="stat-icon" style="background: #e74c3c;">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M9 11l3 3L22 4" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11" stroke="white" stroke-width="2"/>
          </svg>
        </div>
        <div class="stat-info">
          <h3>Total Quizzes</h3>
          <p class="stat-value">{{ stats.totalQuizzes }}</p>
          <span class="stat-label">Across all courses</span>
        </div>
      </div>
    </div>

    <div class="dashboard-sections">
      <div class="section card">
        <h2>All Courses</h2>
        <div v-if="loadingCourses" class="loading-small">Loading...</div>
        <div v-else-if="courses.length === 0" class="empty-small">No courses yet</div>
        <table v-else class="data-table">
          <thead>
            <tr>
              <th>Course Title</th>
              <th>Instructor</th>
              <th>Category</th>
              <th>Status</th>
              <th>Enrollments</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="course in courses.slice(0, 10)" :key="course.id">
              <td><strong>{{ course.title }}</strong></td>
              <td>{{ course.instructorName }}</td>
              <td>{{ course.category }}</td>
              <td>
                <span class="badge" :class="course.isPublished ? 'badge-success' : 'badge-warning'">
                  {{ course.isPublished ? 'Published' : 'Draft' }}
                </span>
              </td>
              <td>{{ course.enrollmentCount || 0 }}</td>
              <td>
                <router-link :to="`/courses/${course.id}`" class="btn-link">View</router-link>
              </td>
            </tr>
          </tbody>
        </table>
        <router-link to="/courses" class="btn btn-secondary mt-10">View All Courses</router-link>
      </div>

      <div class="section card">
        <h2>Pending User Approvals</h2>
        <div v-if="loadingUsers" class="loading-small">Loading...</div>
        <div v-else-if="pendingUsers.length === 0" class="empty-small">No pending approvals</div>
        <table v-else class="data-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>Role</th>
              <th>Registered</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in pendingUsers" :key="user.id">
              <td><strong>{{ user.firstName }} {{ user.lastName }}</strong></td>
              <td>{{ user.email }}</td>
              <td>
                <span class="badge badge-primary">{{ user.role }}</span>
              </td>
              <td>{{ formatDate(user.createdAt) }}</td>
              <td>
                <button @click="approveUser(user.id)" class="btn btn-success btn-sm">Approve</button>
                <button @click="rejectUser(user.id)" class="btn btn-danger btn-sm ml-5">Reject</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="section card">
        <h2>Recent Activity</h2>
        <div class="activity-list">
          <div v-if="activities.length === 0" class="empty-small">No recent activity</div>
          <div v-for="activity in activities.slice(0, 10)" :key="activity.id" class="activity-item">
            <div class="activity-icon">{{ activity.icon }}</div>
            <div class="activity-content">
              <p><strong>{{ activity.title }}</strong></p>
              <span class="text-muted">{{ activity.description }} • {{ formatDate(activity.timestamp) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const stats = ref({
  totalUsers: 0,
  totalCourses: 0,
  activeEnrollments: 0,
  totalQuizzes: 0
})

const courses = ref([])
const pendingUsers = ref([])
const activities = ref([])
const loadingCourses = ref(true)
const loadingUsers = ref(true)

onMounted(async () => {
  await Promise.all([
    loadStats(),
    loadCourses(),
    loadPendingUsers(),
    loadActivities()
  ])
})

async function loadStats() {
  try {
    const token = localStorage.getItem('token')
    
    // Get total courses
    const coursesRes = await axios.get('http://localhost:8080/api/courses', {
      headers: { Authorization: `Bearer ${token}` }
    })
    stats.value.totalCourses = coursesRes.data.data.length

    // Get enrollments (if API exists)
    try {
      const enrollRes = await axios.get('http://localhost:8080/api/admin/enrollments', {
        headers: { Authorization: `Bearer ${token}` }
      })
      stats.value.activeEnrollments = enrollRes.data.data.length
    } catch (e) {
      stats.value.activeEnrollments = '—'
    }

    // Mock data for other stats (replace with real API calls when available)
    stats.value.totalUsers = 48
    stats.value.totalQuizzes = 12
  } catch (error) {
    console.error('Error loading stats:', error)
  }
}

async function loadCourses() {
  try {
    const token = localStorage.getItem('token')
    const response = await axios.get('http://localhost:8080/api/courses', {
      headers: { Authorization: `Bearer ${token}` }
    })
    courses.value = response.data.data || []
  } catch (error) {
    console.error('Error loading courses:', error)
  } finally {
    loadingCourses.value = false
  }
}

async function loadPendingUsers() {
  try {
    // Mock pending users (replace with real API call when available)
    pendingUsers.value = []
  } catch (error) {
    console.error('Error loading pending users:', error)
  } finally {
    loadingUsers.value = false
  }
}

async function loadActivities() {
  // Mock activities (replace with real API call when available)
  activities.value = [
    { id: 1, icon: '📚', title: 'New Course Created', description: 'Spring Boot Fundamentals', timestamp: new Date() },
    { id: 2, icon: '👤', title: 'New User Registered', description: 'john.doe@example.com', timestamp: new Date() },
    { id: 3, icon: '✅', title: 'Course Published', description: 'Advanced Java Programming', timestamp: new Date() }
  ]
}

async function approveUser(userId) {
  if (confirm('Approve this user?')) {
    try {
      const token = localStorage.getItem('token')
      await axios.put(`http://localhost:8080/api/admin/users/${userId}/approve`, {}, {
        headers: { Authorization: `Bearer ${token}` }
      })
      alert('User approved successfully')
      loadPendingUsers()
    } catch (error) {
      alert('Failed to approve user: ' + (error.response?.data?.message || 'Unknown error'))
    }
  }
}

async function rejectUser(userId) {
  if (confirm('Reject this user? This action cannot be undone.')) {
    try {
      const token = localStorage.getItem('token')
      await axios.delete(`http://localhost:8080/api/admin/users/${userId}`, {
        headers: { Authorization: `Bearer ${token}` }
      })
      alert('User rejected')
      loadPendingUsers()
    } catch (error) {
      alert('Failed to reject user: ' + (error.response?.data?.message || 'Unknown error'))
    }
  }
}

function formatDate(date) {
  if (!date) return 'N/A'
  return new Date(date).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}
</script>

<style scoped>
.admin-dashboard {
  padding: 0 30px 30px;
  max-width: 1400px;
  margin: 0 auto;
}

.admin-dashboard h1 {
  margin-bottom: 0;
  margin-top: 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 10px;
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}

.stat-card {
  display: flex;
  gap: 20px;
  align-items: center;
}

.stat-icon {
  font-size: 48px;
  width: 70px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f3f4f6;
  border-radius: 12px;
}

.stat-info h3 {
  color: #6b7280;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin: 0;
}
</style>
