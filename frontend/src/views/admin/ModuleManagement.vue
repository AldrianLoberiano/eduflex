<template>
  <div class="module-management">
    <div class="page-header">
      <h1>Module Management</h1>
      <button class="btn-primary" @click="showAddModal = true">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" style="margin-right: 8px;">
          <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
        </svg>
        Create Module
      </button>
    </div>

    <!-- Statistics -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon" style="background: #5B6FE3;">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" stroke="white" stroke-width="2" stroke-linecap="round"/>
            <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" stroke="white" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.totalModules }}</div>
          <div class="stat-label">Total Modules</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #00cc70;">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M9 12l2 2 4-4" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <circle cx="12" cy="12" r="10" stroke="white" stroke-width="2"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.published }}</div>
          <div class="stat-label">Published</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #FCC43E;">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" stroke="white" stroke-width="2"/>
            <path d="M14 2v6h6M16 13H8M16 17H8M10 9H8" stroke="white" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.draft }}</div>
          <div class="stat-label">Draft</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #FB7D5B;">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
            <path d="M23 7l-7 5 7 5V7z" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <rect x="1" y="5" width="15" height="14" rx="2" stroke="white" stroke-width="2"/>
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.videoLessons }}</div>
          <div class="stat-label">Video Lessons</div>
        </div>
      </div>
    </div>

    <!-- Filters -->
    <div class="filters-section">
      <div class="search-box">
        <input type="text" v-model="searchQuery" placeholder="Search modules and lessons...">
      </div>
      <div class="filter-tabs">
        <button :class="{'active': selectedCourse === 'ALL'}" @click="selectedCourse = 'ALL'">All Courses</button>
        <button v-for="course in courses" :key="course.id" 
                :class="{'active': selectedCourse === course.id}" 
                @click="selectedCourse = course.id">
          {{ course.title }}
        </button>
      </div>
    </div>

    <!-- Modules List -->
    <div class="modules-container">
      <div v-for="course in filteredCourses" :key="course.id" class="course-section">
        <div class="course-header">
          <h2>{{ course.title }}</h2>
          <span class="course-badge">{{ course.lessons?.length || 0 }} Lessons</span>
        </div>

        <div class="lessons-list">
          <div v-for="(lesson, index) in course.lessons" :key="lesson.id" class="lesson-card">
            <div class="lesson-order">{{ index + 1 }}</div>
            <div class="lesson-content">
              <div class="lesson-header">
                <h3>{{ lesson.title }}</h3>
                <span class="lesson-type" :class="lesson.type.toLowerCase()">
                  <svg v-if="lesson.type === 'VIDEO'" width="16" height="16" viewBox="0 0 24 24" fill="none" style="margin-right: 4px;">
                    <path d="M23 7l-7 5 7 5V7z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <rect x="1" y="5" width="15" height="14" rx="2" stroke="currentColor" stroke-width="2"/>
                  </svg>
                  <svg v-else-if="lesson.type === 'TEXT'" width="16" height="16" viewBox="0 0 24 24" fill="none" style="margin-right: 4px;">
                    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" stroke="currentColor" stroke-width="2"/>
                    <path d="M14 2v6h6M16 13H8M16 17H8M10 9H8" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                  </svg>
                  <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" style="margin-right: 4px;">
                    <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
                    <path d="M12 16v-4M12 8h.01" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                  </svg>
                  {{ lesson.type }}
                </span>
              </div>
              <p class="lesson-description">{{ lesson.description }}</p>
              <div class="lesson-meta">
                <span v-if="lesson.duration">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" style="margin-right: 4px; vertical-align: middle;">
                    <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
                    <path d="M12 6v6l4 2" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                  </svg>
                  {{ lesson.duration }} min
                </span>
                <span :class="lesson.isPublished ? 'status-published' : 'status-draft'">
                  {{ lesson.isPublished ? '✓ Published' : 'Draft' }}
                </span>
              </div>
            </div>
            <div class="lesson-actions">
              <button class="btn-icon" title="Edit" @click="editLesson(lesson)">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" stroke="currentColor" stroke-width="2"/>
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" stroke="currentColor" stroke-width="2"/>
                </svg>
              </button>
              <button class="btn-icon" title="Move Up" @click="moveLesson(lesson, -1)" :disabled="index === 0">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                  <path d="M18 15l-6-6-6 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </button>
              <button class="btn-icon" title="Move Down" @click="moveLesson(lesson, 1)" :disabled="index === course.lessons.length - 1">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                  <path d="M6 9l6 6 6-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </button>
              <button class="btn-icon" title="Delete" @click="deleteLesson(lesson.id)">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                  <path d="M3 6h18M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M10 11v6M14 11v6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
              </button>
            </div>
          </div>

          <div v-if="!course.lessons || course.lessons.length === 0" class="empty-lessons">
            <p>No lessons yet. Create your first lesson!</p>
            <button class="btn-secondary" @click="addLesson(course.id)">+ Add Lesson</button>
          </div>
        </div>
      </div>

      <div v-if="filteredCourses.length === 0" class="empty-state">
        <svg width="64" height="64" viewBox="0 0 24 24" fill="none" style="margin: 0 auto 16px; opacity: 0.5;">
          <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
        </svg>
        <h3>No modules found</h3>
        <p>Start by creating your first course module</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const courses = ref([])
const searchQuery = ref('')
const selectedCourse = ref('ALL')
const showAddModal = ref(false)
const stats = ref({
  totalModules: 0,
  published: 0,
  draft: 0,
  videoLessons: 0
})

const filteredCourses = computed(() => {
  let filtered = courses.value

  if (selectedCourse.value !== 'ALL') {
    filtered = filtered.filter(c => c.id === selectedCourse.value)
  }

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(course =>
      course.title?.toLowerCase().includes(query) ||
      course.lessons?.some(l => l.title?.toLowerCase().includes(query))
    )
  }

  return filtered
})

const loadCourses = async () => {
  try {
    const token = localStorage.getItem('token')
    const response = await axios.get('http://localhost:8080/api/courses', {
      headers: { Authorization: `Bearer ${token}` }
    })
    courses.value = response.data.data || []
    calculateStats()
  } catch (error) {
    console.error('Error loading courses:', error)
  }
}

const calculateStats = () => {
  stats.value.totalModules = courses.value.reduce((sum, c) => sum + (c.lessons?.length || 0), 0)
  stats.value.published = courses.value.reduce((sum, c) =>
    sum + (c.lessons?.filter(l => l.isPublished).length || 0), 0)
  stats.value.draft = courses.value.reduce((sum, c) =>
    sum + (c.lessons?.filter(l => !l.isPublished).length || 0), 0)
  stats.value.videoLessons = courses.value.reduce((sum, c) =>
    sum + (c.lessons?.filter(l => l.type === 'VIDEO').length || 0), 0)
}

const editLesson = (lesson) => {
  console.log('Edit lesson:', lesson)
}

const moveLesson = (lesson, direction) => {
  console.log('Move lesson:', lesson, direction)
}

const deleteLesson = (lessonId) => {
  if (!confirm('Delete this lesson?')) return
  console.log('Delete lesson:', lessonId)
}

const addLesson = (courseId) => {
  console.log('Add lesson to course:', courseId)
}

onMounted(() => {
  loadCourses()
})
</script>

<style scoped>
.module-management {
  padding: 2rem;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.page-header h1 {
  font-size: 2rem;
  color: #1a1a1a;
  font-weight: 700;
}

.btn-primary {
  background: linear-gradient(135deg, #5B6FE3 0%, #4355d4 100%);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-secondary {
  background: white;
  color: #5B6FE3;
  border: 2px solid #5B6FE3;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 1rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.8rem;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 2rem;
  font-weight: 700;
  color: #1a1a1a;
}

.stat-label {
  color: #666;
  font-size: 0.875rem;
  margin-top: 0.25rem;
}

.filters-section {
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  margin-bottom: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.search-box input {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 0.95rem;
  margin-bottom: 1rem;
}

.search-box input:focus {
  outline: none;
  border-color: #5B6FE3;
}

.filter-tabs {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.filter-tabs button {
  padding: 0.5rem 1rem;
  border: 2px solid #e0e0e0;
  background: white;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s;
}

.filter-tabs button.active {
  background: #5B6FE3;
  color: white;
  border-color: #5B6FE3;
}

.course-section {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #f0f0f0;
}

.course-header h2 {
  font-size: 1.5rem;
  color: #1a1a1a;
  font-weight: 600;
}

.course-badge {
  background: #e3f2fd;
  color: #1976d2;
  padding: 0.375rem 0.75rem;
  border-radius: 6px;
  font-size: 0.875rem;
  font-weight: 600;
}

.lessons-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.lesson-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
  border: 2px solid #e0e0e0;
  transition: all 0.2s;
}

.lesson-card:hover {
  border-color: #5B6FE3;
  box-shadow: 0 4px 12px rgba(91, 111, 227, 0.1);
}

.lesson-order {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #5B6FE3 0%, #4355d4 100%);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1.1rem;
  flex-shrink: 0;
}

.lesson-content {
  flex: 1;
}

.lesson-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.lesson-header h3 {
  font-size: 1.1rem;
  color: #1a1a1a;
  font-weight: 600;
}

.lesson-type {
  padding: 0.25rem 0.75rem;
  border-radius: 6px;
  font-size: 0.75rem;
  font-weight: 600;
}

.lesson-type.video {
  background: #ffe0e6;
  color: #c62828;
}

.lesson-type.text {
  background: #e3f2fd;
  color: #1976d2;
}

.lesson-type.quiz {
  background: #fff9c4;
  color: #f57f17;
}

.lesson-description {
  color: #666;
  font-size: 0.875rem;
  margin-bottom: 0.5rem;
}

.lesson-meta {
  display: flex;
  gap: 1rem;
  font-size: 0.875rem;
  color: #999;
}

.status-published {
  color: #2e7d32;
}

.status-draft {
  color: #f57f17;
}

.lesson-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-icon {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  padding: 0.25rem;
  transition: transform 0.2s;
}

.btn-icon:hover:not(:disabled) {
  transform: scale(1.2);
}

.btn-icon:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.empty-lessons {
  text-align: center;
  padding: 2rem;
  color: #999;
}

.empty-state {
  text-align: center;
  padding: 3rem;
  color: #999;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}
</style>
