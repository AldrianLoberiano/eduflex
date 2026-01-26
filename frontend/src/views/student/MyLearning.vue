<template>
  <div class="my-learning-page">
    <h1>My Learning</h1>
    <p class="text-muted mb-0">Track your progress and continue learning</p>

    <!-- Stats Overview -->
    <div class="stats-grid grid grid-4">
      <div class="stat-card card">
        <div class="stat-icon" style="background: #3498db;">📚</div>
        <div class="stat-info">
          <h3>Enrolled Courses</h3>
          <p class="stat-value">{{ enrollments.length }}</p>
          <span class="stat-label">Active courses</span>
        </div>
      </div>
      <div class="stat-card card">
        <div class="stat-icon" style="background: #27ae60;">✅</div>
        <div class="stat-info">
          <h3>Completed</h3>
          <p class="stat-value">{{ completedCount }}</p>
          <span class="stat-label">Finished courses</span>
        </div>
      </div>
      <div class="stat-card card">
        <div class="stat-icon" style="background: #e74c3c;">📝</div>
        <div class="stat-info">
          <h3>Quizzes Taken</h3>
          <p class="stat-value">{{ totalQuizzes }}</p>
          <span class="stat-label">Total attempts</span>
        </div>
      </div>
      <div class="stat-card card">
        <div class="stat-icon" style="background: #f39c12;">⏱️</div>
        <div class="stat-info">
          <h3>Learning Time</h3>
          <p class="stat-value">{{ totalHours }}h</p>
          <span class="stat-label">Time invested</span>
        </div>
      </div>
    </div>

    <div v-if="loading" class="loading">Loading your courses...</div>

    <div v-else-if="error" class="alert alert-error">
      {{ error }}
    </div>

    <div v-else-if="enrollments.length === 0" class="empty-state">
      <h3>You haven't enrolled in any courses yet</h3>
      <p>Start your learning journey today!</p>
      <router-link to="/courses" class="btn btn-primary">Browse Courses</router-link>
    </div>

    <div v-else>
      <h2 class="section-title">My Courses</h2>
      <div class="enrollments-grid">
        <div
          v-for="enrollment in enrollments"
          :key="enrollment.id"
          class="enrollment-card card"
        >
          <div class="enrollment-header">
            <h3>{{ enrollment.courseTitle }}</h3>
            <span class="badge" :class="enrollment.progressPercentage === 100 ? 'badge-success' : 'badge-primary'">
              {{ enrollment.progressPercentage }}%
            </span>
          </div>

          <p class="course-category">📂 {{ enrollment.courseCategory }}</p>

          <div class="progress-section">
            <div class="progress-header">
              <span>Progress</span>
              <span class="progress-text">{{ enrollment.progressPercentage }}% Complete</span>
            </div>
            <div class="progress-bar">
              <div
                class="progress-fill"
                :style="{ width: enrollment.progressPercentage + '%' }"
              ></div>
            </div>
          </div>

          <div class="course-stats">
            <div class="stat-item">
              <span class="stat-icon">📖</span>
              <span>Lessons completed</span>
            </div>
            <div class="stat-item">
              <span class="stat-icon">📝</span>
              <span>Quizzes available</span>
            </div>
            <div class="stat-item">
              <span class="stat-icon">📅</span>
              <span>Enrolled: {{ formatDate(enrollment.enrolledAt) }}</span>
            </div>
            <div v-if="enrollment.completedAt" class="stat-item">
              <span class="stat-icon">✅</span>
              <span>Completed: {{ formatDate(enrollment.completedAt) }}</span>
            </div>
          </div>

          <div class="card-actions">
            <router-link
              :to="`/courses/${enrollment.courseId}`"
              class="btn btn-primary"
            >
              {{ enrollment.progressPercentage === 100 ? 'Review Course' : 'Continue Learning' }}
            </router-link>
            <router-link
              :to="`/courses/${enrollment.courseId}/quizzes`"
              class="btn btn-secondary"
            >
              View Quizzes
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { enrollmentService } from '../../services'

const enrollments = ref([])
const loading = ref(true)
const error = ref('')

const completedCount = computed(() => {
  return enrollments.value.filter(e => e.progressPercentage === 100).length
})

const totalQuizzes = computed(() => {
  // Mock data - replace with actual quiz count when API is available
  return enrollments.value.length * 2
})

const totalHours = computed(() => {
  // Mock calculation - could be based on actual time tracking
  return enrollments.value.length * 8
})

onMounted(async () => {
  try {
    const response = await enrollmentService.getMyEnrollments()
    enrollments.value = response.data || []
  } catch (err) {
    error.value = 'Failed to load your enrollments'
  } finally {
    loading.value = false
  }
})

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}
</script>

<style scoped>
.my-learning-page {
  padding: 0 30px 30px;
  max-width: 1400px;
  margin: 0 auto;
}

.my-learning-page h1 {
  margin-bottom: 0;
  margin-top: 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 10px;
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

.section-title {
  margin-bottom: 20px;
  color: #333;
}

.enrollments-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.enrollment-card {
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.enrollment-header {
  display: flex;
  justify-content: space-between;
  align-items: start;
  gap: 15px;
  margin-bottom: 10px;
}

.enrollment-header h3 {
  margin: 0;
  flex: 1;
  font-size: 18px;
  color: #333;
}

.course-category {
  color: #666;
  font-size: 14px;
  margin-bottom: 15px;
}

.progress-section {
  margin: 15px 0;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
  color: #666;
}

.progress-text {
  font-weight: 600;
  color: #333;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background-color: #e5e7eb;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #4facfe 0%, #00f2fe 100%);
  transition: width 0.3s;
}

.course-stats {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin: 15px 0;
  padding: 15px 0;
  border-top: 1px solid #e9ecef;
  border-bottom: 1px solid #e9ecef;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #666;
}

.stat-item .stat-icon {
  font-size: 16px;
  width: auto;
  height: auto;
}

.card-actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.card-actions .btn {
  flex: 1;
  text-align: center;
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
  
  .enrollments-grid {
    grid-template-columns: 1fr;
  }
  
  .card-actions {
    flex-direction: column;
  }
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 8px;
}

.empty-state h3 {
  margin-bottom: 10px;
  color: #333;
}

.empty-state p {
  color: #666;
  margin-bottom: 20px;
}

.text-success {
  color: #059669;
  font-weight: 500;
}
</style>
