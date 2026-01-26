<template>
  <div class="instructor-dashboard">
    <div class="dashboard-header">
      <div>
        <h1>Instructor Dashboard</h1>
        <p class="text-muted">Manage your courses and track student progress</p>
      </div>
      <router-link to="/instructor/courses/new" class="btn btn-primary">
        + Create New Course
      </router-link>
    </div>

    <!-- Stats Overview -->
    <div class="stats-grid grid grid-4">
      <div class="stat-card card">
        <div class="stat-icon" style="background: #3498db;">📚</div>
        <div class="stat-info">
          <h3>My Courses</h3>
          <p class="stat-value">{{ courses.length }}</p>
          <span class="stat-label">Total courses</span>
        </div>
      </div>
      <div class="stat-card card">
        <div class="stat-icon" style="background: #27ae60;">👥</div>
        <div class="stat-info">
          <h3>Total Students</h3>
          <p class="stat-value">{{ totalStudents }}</p>
          <span class="stat-label">Enrolled students</span>
        </div>
      </div>
      <div class="stat-card card">
        <div class="stat-icon" style="background: #e74c3c;">📝</div>
        <div class="stat-info">
          <h3>Quizzes</h3>
          <p class="stat-value">{{ totalQuizzes }}</p>
          <span class="stat-label">Across all courses</span>
        </div>
      </div>
      <div class="stat-card card">
        <div class="stat-icon" style="background: #f39c12;">⭐</div>
        <div class="stat-info">
          <h3>Avg Rating</h3>
          <p class="stat-value">{{ avgRating }}</p>
          <span class="stat-label">Course ratings</span>
        </div>
      </div>
    </div>

    <div v-if="loading" class="loading">Loading your courses...</div>

    <div v-else-if="error" class="alert alert-error">
      {{ error }}
    </div>

    <div v-else-if="courses.length === 0" class="empty-state">
      <h3>You haven't created any courses yet</h3>
      <p>Start sharing your knowledge with students!</p>
      <router-link to="/instructor/courses/new" class="btn btn-primary">
        Create Your First Course
      </router-link>
    </div>

    <div v-else>
      <h2 class="section-title">My Courses</h2>
      <div class="courses-table">
        <table>
          <thead>
            <tr>
              <th>Course</th>
              <th>Category</th>
              <th>Status</th>
              <th>Lessons</th>
              <th>Students</th>
              <th>Quizzes</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="course in courses" :key="course.id">
              <td>
                <div class="course-cell">
                  <strong>{{ course.title }}</strong>
                  <span class="text-muted">{{ course.level }}</span>
                </div>
              </td>
              <td>{{ course.category }}</td>
              <td>
                <span class="badge" :class="course.isPublished ? 'badge-success' : 'badge-warning'">
                  {{ course.isPublished ? 'Published' : 'Draft' }}
                </span>
              </td>
              <td>{{ course.lessonCount || 0 }}</td>
              <td>{{ course.enrollmentCount || 0 }}</td>
              <td>{{ course.quizCount || 0 }}</td>
              <td>
                <div class="action-buttons">
                  <router-link
                    :to="`/instructor/courses/${course.id}/edit`"
                    class="btn btn-secondary btn-sm"
                    title="Edit course"
                  >
                    ✏️ Edit
                  </router-link>
                  <router-link
                    :to="`/courses/${course.id}/quizzes`"
                    class="btn btn-info btn-sm"
                    title="Manage quizzes"
                  >
                    📝 Quizzes
                  </router-link>
                  <router-link
                    :to="`/instructor/courses/${course.id}/students`"
                    class="btn btn-success btn-sm"
                    title="View students"
                  >
                    👥 Students
                  </router-link>
                  <button
                    @click="confirmDelete(course)"
                    class="btn btn-danger btn-sm"
                    title="Delete course"
                  >
                    🗑️
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div v-if="deleteModal" class="modal-overlay" @click="deleteModal = null">
      <div class="modal-content" @click.stop>
        <h3>Delete Course</h3>
        <p>Are you sure you want to delete "{{ deleteModal.title }}"?</p>
        <p class="text-muted">This action cannot be undone.</p>
        <div class="modal-actions">
          <button @click="deleteModal = null" class="btn btn-secondary">Cancel</button>
          <button @click="deleteCourse" class="btn btn-danger" :disabled="deleting">
            {{ deleting ? 'Deleting...' : 'Delete' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { courseService } from '../../services'

const courses = ref([])
const loading = ref(true)
const error = ref('')
const deleteModal = ref(null)
const deleting = ref(false)

const totalStudents = computed(() => {
  return courses.value.reduce((sum, course) => sum + (course.enrollmentCount || 0), 0)
})

const totalQuizzes = computed(() => {
  return courses.value.reduce((sum, course) => sum + (course.quizCount || 0), 0)
})

const avgRating = computed(() => {
  const ratings = courses.value.filter(c => c.rating).map(c => c.rating)
  if (ratings.length === 0) return '—'
  return (ratings.reduce((sum, r) => sum + r, 0) / ratings.length).toFixed(1)
})

onMounted(loadCourses)

async function loadCourses() {
  loading.value = true
  try {
    const response = await courseService.getInstructorCourses()
    courses.value = response.data || []
  } catch (err) {
    error.value = 'Failed to load your courses'
  } finally {
    loading.value = false
  }
}

function confirmDelete(course) {
  deleteModal.value = course
}

async function deleteCourse() {
  deleting.value = true
  try {
    await courseService.deleteCourse(deleteModal.value.id)
    courses.value = courses.value.filter(c => c.id !== deleteModal.value.id)
    deleteModal.value = null
  } catch (err) {
    error.value = 'Failed to delete course'
  } finally {
    deleting.value = false
  }
}
</script>

<style scoped>
.instructor-dashboard {
  padding: 5px 30px;
  max-width: 1400px;
  margin: 0 auto;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: start;
  margin-bottom: 5px;
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
  margin-bottom: 15px;
  color: #333;
}

.courses-table {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

table {
  width: 100%;
  border-collapse: collapse;
}

th {
  background-color: #f9fafb;
  padding: 12px 16px;
  text-align: left;
  font-weight: 600;
  color: #374151;
  font-size: 14px;
}

td {
  padding: 16px;
  border-top: 1px solid #e5e7eb;
}

.course-cell {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.action-buttons {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.btn-sm {
  padding: 6px 10px;
  font-size: 12px;
}

.btn-info {
  background: #3498db;
  color: white;
}

.btn-info:hover {
  background: #2980b9;
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
  
  .action-buttons {
    flex-direction: column;
  }
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  padding: 30px;
  max-width: 500px;
  width: 90%;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 20px;
}

.modal-content {
  background: white;
  border-radius: 8px;
  padding: 30px;
  max-width: 450px;
  width: 90%;
}

.modal-content h3 {
  margin-bottom: 15px;
}

.modal-content p {
  margin-bottom: 10px;
}

.modal-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 25px;
}
</style>
