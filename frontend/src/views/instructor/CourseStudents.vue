<template>
  <div class="course-students-page">
    <div class="page-header">
      <div>
        <router-link to="/instructor/dashboard" class="back-link">← Back to Dashboard</router-link>
        <h1>Enrolled Students</h1>
        <p v-if="courseTitle" class="text-muted">{{ courseTitle }}</p>
      </div>
    </div>

    <div v-if="loading" class="loading">Loading enrolled students...</div>

    <div v-else-if="error" class="alert alert-error">
      {{ error }}
    </div>

    <div v-else-if="enrollments.length === 0" class="empty-state">
      <h3>No students enrolled yet</h3>
      <p>Share your course to get students enrolled!</p>
    </div>

    <div v-else>
      <div class="stats-summary">
        <div class="stat-item">
          <span class="stat-label">Total Students:</span>
          <span class="stat-value">{{ enrollments.length }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">Completed:</span>
          <span class="stat-value">{{ completedCount }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">In Progress:</span>
          <span class="stat-value">{{ inProgressCount }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">Average Progress:</span>
          <span class="stat-value">{{ averageProgress }}%</span>
        </div>
      </div>

      <div class="students-table">
        <table>
          <thead>
            <tr>
              <th>Student Name</th>
              <th>Email/ID</th>
              <th>Enrolled Date</th>
              <th>Progress</th>
              <th>Status</th>
              <th>Completed Date</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="enrollment in sortedEnrollments" :key="enrollment.id">
              <td>
                <strong>{{ enrollment.studentName }}</strong>
              </td>
              <td class="text-muted">
                ID: {{ enrollment.studentId }}
              </td>
              <td>{{ formatDate(enrollment.enrolledAt) }}</td>
              <td>
                <div class="progress-cell">
                  <div class="progress-bar">
                    <div 
                      class="progress-fill" 
                      :style="{ width: enrollment.progressPercentage + '%' }"
                    ></div>
                  </div>
                  <span class="progress-text">{{ enrollment.progressPercentage }}%</span>
                </div>
              </td>
              <td>
                <span 
                  class="badge" 
                  :class="enrollment.isCompleted ? 'badge-success' : 'badge-primary'"
                >
                  {{ enrollment.isCompleted ? 'Completed' : 'In Progress' }}
                </span>
              </td>
              <td>
                {{ enrollment.completedAt ? formatDate(enrollment.completedAt) : '-' }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { enrollmentService, courseService } from '../../services'

const route = useRoute()
const enrollments = ref([])
const courseTitle = ref('')
const loading = ref(true)
const error = ref('')

const completedCount = computed(() => {
  return enrollments.value.filter(e => e.isCompleted).length
})

const inProgressCount = computed(() => {
  return enrollments.value.filter(e => !e.isCompleted).length
})

const averageProgress = computed(() => {
  if (enrollments.value.length === 0) return 0
  const total = enrollments.value.reduce((sum, e) => sum + e.progressPercentage, 0)
  return Math.round(total / enrollments.value.length)
})

const sortedEnrollments = computed(() => {
  return [...enrollments.value].sort((a, b) => {
    // Sort by progress (descending), then by enrolled date (most recent first)
    if (b.progressPercentage !== a.progressPercentage) {
      return b.progressPercentage - a.progressPercentage
    }
    return new Date(b.enrolledAt) - new Date(a.enrolledAt)
  })
})

onMounted(async () => {
  const courseId = route.params.id
  
  try {
    // Fetch course details
    const courseResponse = await courseService.getCourseById(courseId)
    courseTitle.value = courseResponse.data.title
    
    // Fetch enrollments
    const enrollmentResponse = await enrollmentService.getCourseEnrollments(courseId)
    enrollments.value = enrollmentResponse.data || []
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to load enrolled students'
  } finally {
    loading.value = false
  }
})

function formatDate(dateString) {
  if (!dateString) return '-'
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}
</script>

<style scoped>
.course-students-page {
  padding: 30px;
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 30px;
}

.back-link {
  display: inline-block;
  color: #00ff88;
  text-decoration: none;
  margin-bottom: 10px;
  font-size: 14px;
}

.back-link:hover {
  text-decoration: underline;
}

h1 {
  font-size: 32px;
  margin-bottom: 5px;
  color: #fff;
}

.text-muted {
  color: #888;
  font-size: 16px;
}

.loading {
  text-align: center;
  padding: 50px;
  font-size: 18px;
  color: #888;
}

.alert {
  padding: 15px 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.alert-error {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid #ef4444;
  color: #ef4444;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  margin-top: 20px;
}

.empty-state h3 {
  font-size: 24px;
  margin-bottom: 10px;
  color: #fff;
}

.empty-state p {
  color: #888;
  font-size: 16px;
}

.stats-summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.stat-label {
  font-size: 14px;
  color: #888;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #00ff88;
}

.students-table {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  overflow: hidden;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background: rgba(0, 255, 136, 0.1);
}

th {
  padding: 15px;
  text-align: left;
  font-weight: 600;
  color: #00ff88;
  font-size: 14px;
  text-transform: uppercase;
}

td {
  padding: 15px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  color: #e0e0e0;
}

tbody tr:hover {
  background: rgba(255, 255, 255, 0.03);
}

.progress-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.progress-bar {
  flex: 1;
  height: 8px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #00ff88, #00cc6a);
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 14px;
  color: #888;
  min-width: 45px;
  text-align: right;
}

.badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.badge-success {
  background: rgba(34, 197, 94, 0.2);
  color: #22c55e;
}

.badge-primary {
  background: rgba(59, 130, 246, 0.2);
  color: #3b82f6;
}

strong {
  color: #fff;
}
</style>
