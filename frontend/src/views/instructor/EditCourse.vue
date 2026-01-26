<template>
  <div class="create-course-page">
    <div class="page-header">
      <h1>Edit Course</h1>
      <router-link to="/instructor/dashboard" class="btn btn-secondary">← Back to Dashboard</router-link>
    </div>

    <div v-if="loading" class="loading">Loading course...</div>

    <div v-else-if="error" class="alert alert-error">
      {{ error }}
    </div>

    <div v-else-if="success" class="alert alert-success">
      {{ success }}
    </div>

    <div v-else class="course-builder">
      <!-- Step Indicator -->
      <div class="steps-indicator">
        <div class="step" :class="{ active: currentStep === 1, completed: currentStep > 1 }">
          <div class="step-number">1</div>
          <div class="step-label">Course Details</div>
        </div>
        <div class="step-divider"></div>
        <div class="step" :class="{ active: currentStep === 2, completed: currentStep > 2 }">
          <div class="step-number">2</div>
          <div class="step-label">Manage Lessons</div>
        </div>
        <div class="step-divider"></div>
        <div class="step" :class="{ active: currentStep === 3 }">
          <div class="step-number">3</div>
          <div class="step-label">Review & Update</div>
        </div>
      </div>

      <!-- Step 1: Course Details -->
      <div v-if="currentStep === 1" class="step-content card">
        <h2>Course Information</h2>
        <form @submit.prevent="nextStep">
          <div class="form-group">
            <label class="form-label">Course Title *</label>
            <input
              v-model="formData.title"
              type="text"
              class="form-control"
              placeholder="e.g., Introduction to Web Development"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">Description *</label>
            <textarea
              v-model="formData.description"
              class="form-control"
              rows="5"
              placeholder="Describe what students will learn in this course..."
              required
            ></textarea>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label class="form-label">Category *</label>
              <select v-model="formData.category" class="form-select" required>
                <option value="">Select a category</option>
                <option value="Programming">Programming</option>
                <option value="Design">Design</option>
                <option value="Business">Business</option>
                <option value="Marketing">Marketing</option>
                <option value="Data Science">Data Science</option>
                <option value="Web Development">Web Development</option>
                <option value="Mobile Development">Mobile Development</option>
                <option value="Other">Other</option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Level *</label>
              <select v-model="formData.level" class="form-select" required>
                <option value="">Select level</option>
                <option value="Beginner">Beginner</option>
                <option value="Intermediate">Intermediate</option>
                <option value="Advanced">Advanced</option>
              </select>
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label class="form-label">Duration (hours)</label>
              <input
                v-model.number="formData.duration"
                type="number"
                class="form-control"
                placeholder="Estimated course duration"
                min="1"
                max="500"
              />
            </div>

            <div class="form-group">
              <label class="form-label">Thumbnail URL</label>
              <input
                v-model="formData.thumbnailUrl"
                type="url"
                class="form-control"
                placeholder="https://example.com/image.jpg"
              />
            </div>
          </div>

          <div class="form-actions">
            <router-link to="/instructor/dashboard" class="btn btn-secondary">Cancel</router-link>
            <button type="submit" class="btn btn-primary">Next: Manage Lessons →</button>
          </div>
        </form>
      </div>

      <!-- Step 2: Manage Lessons -->
      <div v-if="currentStep === 2" class="step-content card">
        <div class="lessons-header">
          <h2>Course Lessons</h2>
          <button type="button" @click="addLesson" class="btn btn-primary">+ Add Lesson</button>
        </div>

        <div v-if="lessons.length === 0" class="empty-lessons">
          <p>No lessons added yet. Click "Add Lesson" to get started!</p>
        </div>

        <div v-else class="lessons-list">
          <div v-for="(lesson, index) in lessons" :key="index" class="lesson-card">
            <div class="lesson-header">
              <div class="lesson-number">Lesson {{ index + 1 }}</div>
              <button type="button" @click="removeLesson(index)" class="btn-icon">🗑️</button>
            </div>

            <div class="form-group">
              <label class="form-label">Lesson Title *</label>
              <input
                v-model="lesson.title"
                type="text"
                class="form-control"
                placeholder="Enter lesson title"
                required
              />
            </div>

            <div class="form-group">
              <label class="form-label">Lesson Type *</label>
              <select v-model="lesson.type" class="form-select" required>
                <option value="">Select type</option>
                <option value="VIDEO">Video</option>
                <option value="TEXT">Text/Reading</option>
                <option value="QUIZ">Quiz</option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">Content *</label>
              <textarea
                v-model="lesson.content"
                class="form-control"
                rows="4"
                :placeholder="getContentPlaceholder(lesson.type)"
                required
              ></textarea>
              <small class="form-hint">{{ getContentHint(lesson.type) }}</small>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label class="form-label">Duration (minutes)</label>
                <input
                  v-model.number="lesson.duration"
                  type="number"
                  class="form-control"
                  placeholder="Lesson duration"
                  min="1"
                />
              </div>

              <div class="form-group">
                <label class="form-label">Order</label>
                <input
                  v-model.number="lesson.orderIndex"
                  type="number"
                  class="form-control"
                  min="1"
                />
              </div>
            </div>

            <div class="form-group">
              <label class="form-check">
                <input v-model="lesson.isFree" type="checkbox" />
                <span>Free preview lesson</span>
              </label>
            </div>
          </div>
        </div>

        <p v-if="lessons.length > 0 && !lessonsValid" class="validation-hint">
          Please complete all lesson fields before continuing.
        </p>

        <div class="form-actions">
          <button type="button" @click="prevStep" class="btn btn-secondary">← Back</button>
          <button type="button" @click="nextStep" class="btn btn-primary" :disabled="!lessonsValid">
            Next: Review & Update →
          </button>
        </div>
      </div>

      <!-- Step 3: Review & Update -->
      <div v-if="currentStep === 3" class="step-content card">
        <h2>Review Your Course</h2>

        <div class="review-section">
          <h3>Course Details</h3>
          <div class="review-item">
            <span class="review-label">Title:</span>
            <span class="review-value">{{ formData.title }}</span>
          </div>
          <div class="review-item">
            <span class="review-label">Category:</span>
            <span class="review-value">{{ formData.category }}</span>
          </div>
          <div class="review-item">
            <span class="review-label">Level:</span>
            <span class="review-value">{{ formData.level }}</span>
          </div>
          <div class="review-item">
            <span class="review-label">Duration:</span>
            <span class="review-value">{{ formData.duration || 'Not specified' }} hours</span>
          </div>
          <div class="review-item">
            <span class="review-label">Description:</span>
            <p class="review-value">{{ formData.description }}</p>
          </div>
        </div>

        <div class="review-section">
          <h3>Lessons ({{ lessons.length }})</h3>
          <div class="review-lessons">
            <div v-for="(lesson, index) in lessons" :key="index" class="review-lesson">
              <span class="lesson-number">{{ index + 1 }}</span>
              <div class="lesson-info">
                <strong>{{ lesson.title }}</strong>
                <span class="badge badge-primary">{{ formatLessonType(lesson.type) }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="form-group">
          <label class="form-check publish-check">
            <input v-model="formData.isPublished" type="checkbox" />
            <span>
              <strong>Publish course</strong>
              <small>Make this course visible to students</small>
            </span>
          </label>
        </div>

        <div class="form-actions">
          <button type="button" @click="prevStep" class="btn btn-secondary">← Back</button>
          <button type="button" @click="handleSubmit" class="btn btn-success" :disabled="saving">
            {{ saving ? 'Updating...' : '✓ Update Course' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { courseService, lessonService } from '../../services'

const router = useRouter()
const route = useRoute()
const currentStep = ref(1)

const lessonTypeLabels = {
  VIDEO: 'Video',
  TEXT: 'Text/Reading',
  QUIZ: 'Quiz'
}

const formData = ref({
  title: '',
  description: '',
  category: '',
  level: '',
  duration: null,
  thumbnailUrl: '',
  isPublished: false
})

const lessons = ref([])
const deletedLessonIds = ref([])
const error = ref('')
const success = ref('')
const loading = ref(true)
const saving = ref(false)

const lessonsValid = computed(() => {
  if (lessons.value.length === 0) {
    return false
  }
  return lessons.value.every(isLessonComplete)
})

onMounted(async () => {
  try {
    const response = await courseService.getCourseById(route.params.id)
    formData.value = { ...response.data }

    const lessonsResponse = await lessonService.getLessonsByCourse(route.params.id)
    lessons.value = (lessonsResponse.data || []).map(mapLessonResponseToForm)
  } catch (err) {
    error.value = 'Failed to load course'
  } finally {
    loading.value = false
  }
})

function nextStep() {
  if (currentStep.value === 2 && !lessonsValid.value) {
    error.value = 'Add at least one lesson and complete all lesson fields before continuing.'
    return
  }

  error.value = ''

  if (currentStep.value < 3) {
    currentStep.value++
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

function prevStep() {
  if (currentStep.value > 1) {
    currentStep.value--
    error.value = ''
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

function createLessonTemplate() {
  return {
    id: null,
    title: '',
    type: '',
    content: '',
    duration: null,
    orderIndex: lessons.value.length + 1,
    isPublished: true,
    isFree: false
  }
}

function addLesson() {
  lessons.value.push(createLessonTemplate())
}

function removeLesson(index) {
  const [removed] = lessons.value.splice(index, 1)

  if (removed?.id) {
    deletedLessonIds.value.push(removed.id)
  }

  lessons.value.forEach((lesson, idx) => {
    lesson.orderIndex = idx + 1
  })
}

function mapLessonResponseToForm(lesson) {
  return {
    id: lesson.id,
    title: lesson.title,
    type: lesson.type,
    content: lesson.type === 'VIDEO' ? lesson.contentUrl || '' : lesson.description || '',
    duration: lesson.duration,
    orderIndex: lesson.orderIndex,
    isPublished: lesson.isPublished ?? true,
    isFree: false
  }
}

function isLessonComplete(lesson) {
  return !!lesson.title?.trim() && !!lesson.type && !!lesson.content?.trim()
}

function buildLessonPayload(lesson, index) {
  const contentValue = lesson.content?.trim() || ''
  return {
    title: lesson.title?.trim(),
    description: lesson.type === 'VIDEO' ? '' : contentValue,
    type: lesson.type,
    contentUrl: lesson.type === 'VIDEO' ? contentValue : null,
    duration: lesson.duration || null,
    orderIndex: lesson.orderIndex || index + 1,
    isPublished: lesson.isPublished ?? true
  }
}

function formatLessonType(type) {
  return lessonTypeLabels[type] || type || 'Lesson'
}

function getContentPlaceholder(type) {
  switch (type) {
    case 'VIDEO':
      return 'Enter video URL (YouTube, Vimeo, etc.)'
    case 'TEXT':
      return 'Enter lesson content, markdown supported...'
    case 'QUIZ':
      return 'Quiz content will be created separately'
    default:
      return 'Enter lesson content'
  }
}

function getContentHint(type) {
  switch (type) {
    case 'VIDEO':
      return 'Paste the video URL from YouTube, Vimeo, or other platforms'
    case 'TEXT':
      return 'Write your lesson content here. You can use markdown formatting.'
    case 'QUIZ':
      return 'Quizzes can be managed separately in the Quiz section'
    default:
      return ''
  }
}

async function handleSubmit() {
  error.value = ''
  success.value = ''

  if (!lessonsValid.value) {
    error.value = 'Add at least one lesson and complete all lesson fields before publishing.'
    currentStep.value = 2
    window.scrollTo({ top: 0, behavior: 'smooth' })
    return
  }

  saving.value = true

  try {
    await courseService.updateCourse(route.params.id, formData.value)

    for (let index = 0; index < lessons.value.length; index++) {
      const lesson = lessons.value[index]
      const payload = buildLessonPayload(lesson, index)

      if (lesson.id) {
        await lessonService.updateLesson(lesson.id, payload)
      } else {
        await lessonService.createLesson(route.params.id, payload)
      }
    }

    if (deletedLessonIds.value.length > 0) {
      for (const lessonId of deletedLessonIds.value) {
        await lessonService.deleteLesson(lessonId)
      }
      deletedLessonIds.value = []
    }

    success.value = 'Course updated successfully!'
    setTimeout(() => {
      router.push('/instructor/dashboard')
    }, 2000)
  } catch (err) {
    error.value = err.response?.data?.message || 'Failed to update course'
    window.scrollTo({ top: 0, behavior: 'smooth' })
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
/* Reuse styles from CreateCourse.vue */
.create-course-page {
  padding: 30px;
  max-width: 900px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h1 {
  margin: 0;
}

.steps-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.step-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 2px solid #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  color: #9ca3af;
  background: white;
  transition: all 0.3s;
}

.step.active .step-number {
  border-color: #00ff88;
  background: #00ff88;
  color: white;
}

.step.completed .step-number {
  border-color: #10b981;
  background: #10b981;
  color: white;
}

.step-label {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

.step.active .step-label {
  color: #00ff88;
  font-weight: 600;
}

.step-divider {
  width: 80px;
  height: 2px;
  background: #e5e7eb;
  margin: 0 10px;
}

.step-content {
  padding: 40px;
  margin-bottom: 20px;
}

.step-content h2 {
  margin: 0 0 30px 0;
  color: #111827;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.form-check {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.form-check input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.form-actions {
  display: flex;
  gap: 15px;
  justify-content: flex-end;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e5e7eb;
}

textarea.form-control {
  resize: vertical;
  min-height: 100px;
}

.form-hint {
  color: #6b7280;
  font-size: 13px;
  margin-top: 5px;
  display: block;
}

.validation-hint {
  color: #dc2626;
  font-size: 14px;
  margin: 10px 0 0;
}

.lessons-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.lessons-header h2 {
  margin: 0;
}

.empty-lessons {
  text-align: center;
  padding: 60px 20px;
  color: #6b7280;
  background: #f9fafb;
  border-radius: 8px;
}

.lessons-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.lesson-card {
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 20px;
}

.lesson-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.lesson-number {
  font-weight: 600;
  color: #00ff88;
  font-size: 16px;
}

.btn-icon {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  padding: 5px;
  opacity: 0.6;
  transition: opacity 0.2s;
}

.btn-icon:hover {
  opacity: 1;
}

.review-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e5e7eb;
}

.review-section:last-of-type {
  border-bottom: none;
}

.review-section h3 {
  margin: 0 0 15px 0;
  color: #374151;
}

.review-item {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.review-label {
  font-weight: 600;
  color: #6b7280;
  min-width: 120px;
}

.review-value {
  color: #111827;
}

.review-lessons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.review-lesson {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 12px;
  background: #f9fafb;
  border-radius: 6px;
}

.review-lesson .lesson-number {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: #00ff88;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
}

.review-lesson .lesson-info {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.publish-check span {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.publish-check small {
  color: #6b7280;
  font-weight: normal;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }

  .steps-indicator {
    padding: 15px;
  }

  .step-divider {
    width: 40px;
  }

  .step-label {
    font-size: 12px;
  }

  .step-content {
    padding: 20px;
  }
}
</style>

