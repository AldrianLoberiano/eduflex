<template>
  <div class="my-courses">
    <div class="page-header">
      <div>
        <h1>My Courses</h1>
        <p class="text-muted">Manage and organize your courses</p>
      </div>
      <router-link to="/instructor/courses/new" class="btn btn-primary">
        + Create New Course
      </router-link>
    </div>

    <div v-if="loading" class="loading">Loading your courses...</div>

    <div v-else-if="error" class="alert alert-error">
      {{ error }}
    </div>

    <div v-else-if="courses.length === 0" class="empty-state">
      <div class="empty-icon">📚</div>
      <h3>No courses yet</h3>
      <p>Start sharing your knowledge by creating your first course!</p>
      <router-link to="/instructor/courses/new" class="btn btn-primary btn-lg">
        Create Your First Course
      </router-link>
    </div>

    <div v-else class="courses-container">
      <div class="courses-grid">
        <div v-for="course in courses" :key="course.id" class="course-card card">
          <div class="course-header">
            <div class="course-thumbnail">
              <img v-if="course.thumbnailUrl" :src="course.thumbnailUrl" :alt="course.title">
              <div v-else class="thumbnail-placeholder">
                <span>📚</span>
              </div>
            </div>
            <span class="badge" :class="course.isPublished ? 'badge-success' : 'badge-warning'">
              {{ course.isPublished ? 'Published' : 'Draft' }}
            </span>
          </div>
          
          <div class="course-content">
            <h3>{{ course.title }}</h3>
            <p class="course-description">{{ course.description }}</p>
            
            <div class="course-meta">
              <span class="meta-item">
                <span class="icon">📂</span>
                {{ course.category }}
              </span>
              <span class="meta-item">
                <span class="icon">📊</span>
                {{ course.level }}
              </span>
            </div>
            
            <div class="course-stats">
              <div class="stat-item">
                <span class="stat-label">Lessons</span>
                <span class="stat-value">{{ course.lessonCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">Students</span>
                <span class="stat-value">{{ course.enrollmentCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">Quizzes</span>
                <span class="stat-value">{{ course.quizCount || 0 }}</span>
              </div>
            </div>
          </div>
          
          <div class="course-actions">
            <router-link
              :to="`/instructor/courses/${course.id}/edit`"
              class="btn btn-secondary btn-sm"
            >
              ✏️ Edit
            </router-link>
            <router-link
              :to="`/instructor/courses/${course.id}/students`"
              class="btn btn-success btn-sm"
            >
              👥 Students
            </router-link>
            <button
              @click="deleteCourse(course.id)"
              class="btn btn-danger btn-sm"
              title="Delete course"
            >
              🗑️
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const courses = ref([])
const loading = ref(true)
const error = ref('')

onMounted(async () => {
  await loadCourses()
})

async function loadCourses() {
  try {
    loading.value = true
    const token = localStorage.getItem('token')
    const response = await axios.get('http://localhost:8080/api/instructor/courses', {
      headers: { Authorization: `Bearer ${token}` }
    })
    courses.value = response.data.data || []
  } catch (err) {
    error.value = 'Failed to load courses: ' + (err.response?.data?.message || err.message)
  } finally {
    loading.value = false
  }
}

async function deleteCourse(courseId) {
  if (!confirm('Are you sure you want to delete this course? This action cannot be undone.')) {
    return
  }

  try {
    const token = localStorage.getItem('token')
    await axios.delete(`http://localhost:8080/api/instructor/courses/${courseId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    
    // Remove from local list
    courses.value = courses.value.filter(c => c.id !== courseId)
    alert('Course deleted successfully')
  } catch (err) {
    alert('Failed to delete course: ' + (err.response?.data?.message || err.message))
  }
}
</script>

<style scoped>
.my-courses {
  padding: 0 30px 30px;
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h1 {
  margin: 0 0 5px 0;
  color: #fff;
}

.empty-state {
  text-align: center;
  padding: 80px 20px;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.empty-icon {
  font-size: 80px;
  margin-bottom: 20px;
  opacity: 0.5;
}

.empty-state h3 {
  color: #fff;
  margin: 0 0 10px 0;
  font-size: 24px;
}

.empty-state p {
  color: #888;
  margin: 0 0 30px 0;
  font-size: 16px;
}

.courses-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 25px;
}

.course-card {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.course-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 30px rgba(0, 212, 255, 0.2);
  border-color: rgba(0, 212, 255, 0.4);
}

.course-header {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.course-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumbnail-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #1a1a1a 0%, #2a2a2a 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 80px;
}

.course-header .badge {
  position: absolute;
  top: 15px;
  right: 15px;
  font-size: 12px;
  font-weight: 600;
}

.course-content {
  padding: 20px;
}

.course-content h3 {
  color: #fff;
  margin: 0 0 10px 0;
  font-size: 20px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.course-description {
  color: #888;
  font-size: 14px;
  margin: 0 0 15px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 40px;
}

.course-meta {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #888;
  font-size: 13px;
}

.meta-item .icon {
  font-size: 14px;
}

.course-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin-bottom: 15px;
}

.stat-item {
  text-align: center;
  padding: 10px;
  background: rgba(0, 212, 255, 0.05);
  border-radius: 8px;
}

.stat-label {
  display: block;
  font-size: 11px;
  color: #888;
  margin-bottom: 5px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.stat-value {
  display: block;
  font-size: 20px;
  font-weight: 700;
  color: #00d4ff;
}

.course-actions {
  display: flex;
  gap: 8px;
  padding: 15px 20px;
  background: rgba(0, 0, 0, 0.2);
  border-top: 1px solid rgba(255, 255, 255, 0.05);
}

.course-actions .btn {
  flex: 1;
  font-size: 13px;
}

.loading {
  text-align: center;
  padding: 60px 20px;
  color: #00d4ff;
  font-size: 18px;
}

@media (max-width: 768px) {
  .courses-grid {
    grid-template-columns: 1fr;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
}
</style>
