<template>
  <div class="course-detail-page">
    <div v-if="loading" class="loading">Loading course...</div>

    <div v-else-if="error" class="alert alert-error">
      {{ error }}
    </div>

    <div v-else-if="course" class="course-detail">
      <div class="course-hero">
        <div class="course-info">
          <div class="breadcrumb">
            <router-link to="/courses">Courses</router-link>
            <span> / </span>
            <span>{{ course.title }}</span>
          </div>

          <h1>{{ course.title }}</h1>
          <p class="course-intro">{{ course.description }}</p>

          <div class="course-badges">
            <span class="badge badge-primary">{{ course.category }}</span>
            <span v-if="course.level" class="badge badge-warning">{{ course.level }}</span>
            <span v-if="course.isPublished" class="badge badge-success">Published</span>
          </div>

          <div class="course-stats">
            <div class="stat">
              <span class="stat-icon">👤</span>
              <div>
                <div class="stat-value">{{ course.instructorName }}</div>
                <div class="stat-label">Instructor</div>
              </div>
            </div>
            <div class="stat">
              <span class="stat-icon">👥</span>
              <div>
                <div class="stat-value">{{ course.enrollmentCount || 0 }}</div>
                <div class="stat-label">Students</div>
              </div>
            </div>
            <div class="stat">
              <span class="stat-icon">📖</span>
              <div>
                <div class="stat-value">{{ course.lessonCount || 0 }}</div>
                <div class="stat-label">Lessons</div>
              </div>
            </div>
            <div v-if="course.duration" class="stat">
              <span class="stat-icon">⏱️</span>
              <div>
                <div class="stat-value">{{ course.duration }}h</div>
                <div class="stat-label">Duration</div>
              </div>
            </div>
          </div>

          <div class="action-buttons">
            <button
              v-if="authStore.isStudent && !isEnrolled"
              @click="enrollInCourse"
              class="btn btn-primary btn-large"
              :disabled="enrolling"
            >
              {{ enrolling ? 'Enrolling...' : 'Enroll Now' }}
            </button>

            <button
              @click="goToQuizzes"
              class="btn btn-secondary btn-large"
            >
              <i class="fas fa-clipboard-question"></i> View Quizzes
            </button>
          </div>

          <div v-if="isEnrolled" class="alert alert-success">
            ✓ You are enrolled in this course
          </div>
        </div>

        <div v-if="course.thumbnailUrl" class="course-thumbnail">
          <img :src="course.thumbnailUrl" :alt="course.title" />
        </div>
      </div>

      <div class="course-content-section">
        <h2>Course Content</h2>
        <div v-if="course.lessons && course.lessons.length > 0" class="lessons-list">
          <div
            v-for="(lesson, index) in course.lessons"
            :key="lesson.id"
            class="lesson-item card"
          >
            <div class="lesson-number">{{ index + 1 }}</div>
            <div class="lesson-info">
              <h4>{{ lesson.title }}</h4>
              <p v-if="lesson.description">{{ lesson.description }}</p>
              <div class="lesson-meta">
                <span class="badge badge-primary">{{ lesson.type }}</span>
                <span v-if="lesson.duration" class="text-muted">
                  {{ Math.floor(lesson.duration / 60) }} min
                </span>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="empty-state">
          <p>No lessons available yet</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { courseService, enrollmentService } from '../../services'
import { useAuthStore } from '../../stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const course = ref(null)
const loading = ref(true)
const error = ref('')
const enrolling = ref(false)
const isEnrolled = ref(false)

onMounted(async () => {
  const courseId = route.params.id
  if (typeof courseId === 'string' && courseId.startsWith('featured-')) {
    error.value = 'This featured course is not available yet.'
    loading.value = false
    return
  }

  try {
    const response = await courseService.getCourseById(courseId)
    course.value = response.data
    
    // Check if already enrolled
    if (authStore.isStudent) {
      try {
        const enrollments = await enrollmentService.getMyEnrollments()
        isEnrolled.value = enrollments.data.some(e => e.courseId === course.value.id)
      } catch (err) {
        // Ignore errors checking enrollment
      }
    }
  } catch (err) {
    error.value = 'Failed to load course details'
  } finally {
    loading.value = false
  }
})

async function enrollInCourse() {
  if (!authStore.isAuthenticated) {
    router.push('/')
    return
  }

  enrolling.value = true
  try {
    await enrollmentService.enrollInCourse(course.value.id)
    isEnrolled.value = true
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to enroll in course'
  } finally {
    enrolling.value = false
  }
}

function goToQuizzes() {
  router.push(`/courses/${course.value.id}/quizzes`)
}
</script>

<style scoped>
.course-hero {
  background: white;
  border-radius: 8px;
  padding: 40px;
  margin-bottom: 30px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  align-items: start;
}

.breadcrumb {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 15px;
}

.breadcrumb a {
  color: #00ff88;
}

.course-intro {
  font-size: 16px;
  color: #6b7280;
  margin-bottom: 20px;
  line-height: 1.6;
}

.course-badges {
  display: flex;
  gap: 10px;
  margin-bottom: 25px;
}

.course-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 20px;
  margin-bottom: 25px;
}

.stat {
  display: flex;
  gap: 10px;
  align-items: center;
}

.stat-icon {
  font-size: 32px;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #111827;
}

.stat-label {
  font-size: 13px;
  color: #6b7280;
}

.action-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.btn-large {
  padding: 12px 32px;
  font-size: 16px;
}

.btn-secondary {
  background: #6b7280;
  color: white;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-secondary:hover {
  background: #4b5563;
}

.course-thumbnail img {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.course-content-section {
  background: white;
  border-radius: 8px;
  padding: 30px;
}

.lessons-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 20px;
}

.lesson-item {
  display: flex;
  gap: 20px;
  align-items: start;
  transition: transform 0.2s;
}

.lesson-item:hover {
  transform: translateX(5px);
}

.lesson-number {
  width: 40px;
  height: 40px;
  background: #00ff88;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  flex-shrink: 0;
}

.lesson-info {
  flex: 1;
}

.lesson-info h4 {
  margin-bottom: 5px;
  color: #111827;
}

.lesson-info p {
  color: #6b7280;
  font-size: 14px;
  margin-bottom: 10px;
}

.lesson-meta {
  display: flex;
  gap: 10px;
  align-items: center;
}

@media (max-width: 768px) {
  .course-hero {
    grid-template-columns: 1fr;
  }
}
</style>

