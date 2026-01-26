<template>
  <div class="course-card card">
    <img
      v-if="course.thumbnailUrl"
      :src="course.thumbnailUrl"
      :alt="course.title"
      class="course-thumbnail"
    />
    <div v-else class="course-thumbnail-placeholder">
      <span>📚</span>
    </div>

    <div class="course-content">
      <div class="course-header">
        <span class="badge badge-primary">{{ course.category }}</span>
        <span v-if="course.level" class="badge badge-secondary">{{ course.level }}</span>
      </div>

      <h3>{{ course.title }}</h3>
      <p class="course-description">{{ truncateDescription(course.description) }}</p>

      <div class="course-meta">
        <span class="meta-item">
          <span class="icon">👤</span>
          {{ course.instructorName || 'Instructor' }}
        </span>
        <span class="meta-item">
          <span class="icon">📖</span>
          {{ course.lessonCount || 0 }} lessons
        </span>
        <span v-if="course.duration" class="meta-item">
          <span class="icon">⏱️</span>
          {{ course.duration }}h
        </span>
      </div>

      <div class="course-footer">
        <span class="enrollment-count">
          {{ course.enrollmentCount || 0 }} students enrolled
        </span>
        <router-link v-if="canViewCourse" :to="`/courses/${course.id}`" class="btn btn-primary">
          View Course
        </router-link>
        <button v-else class="btn btn-secondary" disabled>
          Coming Soon
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, computed } from 'vue'

const props = defineProps({
  course: {
    type: Object,
    required: true
  }
})

const canViewCourse = computed(() => !props.course?.isFeatured)

function truncateDescription(text) {
  if (!text) return ''
  return text.length > 120 ? text.substring(0, 120) + '...' : text
}
</script>

<style scoped>
.course-card {
  display: flex;
  flex-direction: column;
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
  height: 100%;
}

.course-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.course-thumbnail,
.course-thumbnail-placeholder {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 8px 8px 0 0;
  margin: -20px -20px 0 -20px;
  width: calc(100% + 40px);
}

.course-thumbnail-placeholder {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48px;
}

.course-content {
  padding-top: 15px;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.course-header {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
}

.course-card h3 {
  margin-bottom: 10px;
  color: #111827;
  font-size: 18px;
}

.course-description {
  color: #6b7280;
  font-size: 14px;
  margin-bottom: 15px;
  flex: 1;
}

.course-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 15px;
  padding-top: 15px;
  border-top: 1px solid #e5e7eb;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 13px;
  color: #6b7280;
}

.icon {
  font-size: 14px;
}

.course-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 15px;
  border-top: 1px solid #e5e7eb;
}

.enrollment-count {
  font-size: 13px;
  color: #6b7280;
}
</style>
