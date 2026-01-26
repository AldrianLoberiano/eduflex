<template>
  <div class="courses-page">
    <!-- Welcome Section -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">Welcome to EduFlex</h1>
        <p class="welcome-subtitle">Empowering learners and educators to achieve excellence through quality education</p>
      </div>
    </div>

    <!-- Mission & Vision Section -->
    <div class="mission-vision-section">
      <div class="mission-vision-grid">
        <div class="mission-card">
          <div class="card-icon">🎯</div>
          <h3>Our Mission</h3>
          <p>To provide accessible, high-quality educational resources that empower individuals to learn, grow, and succeed in their personal and professional journeys.</p>
        </div>
        <div class="vision-card">
          <div class="card-icon">🌟</div>
          <h3>Our Vision</h3>
          <p>To be the leading platform that transforms lives through education, creating a global community of lifelong learners and innovative educators.</p>
        </div>
      </div>
    </div>

    <!-- Courses Section -->
    <div ref="coursesSection" class="courses-section">
      <!-- Instructor's Courses Section -->
      <div v-if="instructorCourses.length > 0" class="instructor-courses-section">
        <div class="section-header">
          <h2>Your Courses</h2>
          <p class="text-muted">Courses you've created</p>
        </div>
        <div class="grid grid-3">
          <CourseCard
            v-for="course in instructorCourses"
            :key="course.id"
            :course="course"
          />
        </div>
      </div>

      <!-- All Courses Section -->
      <div class="section-header" :class="{ 'mt-40': instructorCourses.length > 0 }">
        <h2>{{ instructorCourses.length > 0 ? 'All Courses' : 'Explore Courses' }}</h2>
        <p class="text-muted">Discover and enroll in courses to enhance your skills</p>
      </div>

      <div v-if="loading" class="loading">Loading courses...</div>

      <div v-else-if="error" class="alert alert-error">
        {{ error }}
      </div>

      <div v-else-if="courses.length === 0 && featuredCourses.length" class="grid grid-3">
        <CourseCard
          v-for="course in featuredCourses"
          :key="course.id"
          :course="course"
        />
      </div>

      <div v-else-if="courses.length === 0" class="empty-state">
        <h3>No courses available yet</h3>
        <p>Check back soon or explore our featured learning paths.</p>
      </div>

      <div v-else class="grid grid-3">
        <CourseCard
          v-for="course in otherCourses"
          :key="course.id"
          :course="course"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import CourseCard from '../components/courses/CourseCard.vue'
import { courseService } from '../services'
import { useAuthStore } from '../stores/auth'

const emit = defineEmits(['open-register'])
const authStore = useAuthStore()

const currentYear = computed(() => new Date().getFullYear())

const courses = ref([])
const featuredCourses = ref([
  {
    id: 'featured-1',
    title: 'Full-Stack Web Development',
    description: 'Master modern web development with hands-on projects and real-world workflows.',
    category: 'Development',
    level: 'Beginner',
    lessonCount: 18,
    enrollmentCount: 1240,
    duration: 12,
    instructorName: 'EduFlex Team',
    isFeatured: true
  },
  {
    id: 'featured-2',
    title: 'Product Design Sprint',
    description: 'Learn the complete product design process from discovery to high-fidelity prototypes.',
    category: 'Design',
    level: 'Intermediate',
    lessonCount: 14,
    enrollmentCount: 860,
    duration: 10,
    instructorName: 'EduFlex Team',
    isFeatured: true
  },
  {
    id: 'featured-3',
    title: 'Data Analytics for Business',
    description: 'Turn data into actionable insights with dashboards, metrics, and storytelling.',
    category: 'Data',
    level: 'Beginner',
    lessonCount: 16,
    enrollmentCount: 980,
    duration: 11,
    instructorName: 'EduFlex Team',
    isFeatured: true
  }
])

const loading = ref(true)
const error = ref('')
const coursesSection = ref(null)

// Separate instructor's courses from other courses
const instructorCourses = computed(() => {
  if (!authStore.isInstructor || !authStore.user) return []
  const userEmail = authStore.user.email
  return courses.value.filter(course => course.instructorEmail === userEmail)
})

const otherCourses = computed(() => {
  if (!authStore.isInstructor || !authStore.user) return courses.value
  const userEmail = authStore.user.email
  return courses.value.filter(course => course.instructorEmail !== userEmail)
})

onMounted(loadCourses)

async function loadCourses() {
  loading.value = true
  error.value = ''
  try {
    const response = await courseService.getAllCourses()
    courses.value = response.data || []
  } catch (err) {
    error.value = 'Failed to load courses'
  } finally {
    loading.value = false
  }
}

function scrollToCourses() {
  coursesSection.value?.scrollIntoView({ behavior: 'smooth' })
}
</script>

<style scoped>
.courses-page {
  margin: 0;
  padding: 0;
}

.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 100px 20px;
  text-align: center;
  margin: -20px -20px 0 -20px;
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
}

.hero-title {
  font-size: 48px;
  font-weight: 700;
  margin-bottom: 20px;
  animation: fadeInUp 0.8s ease;
}

.hero-subtitle {
  font-size: 20px;
  margin-bottom: 40px;
  opacity: 0.9;
  animation: fadeInUp 0.8s ease 0.2s both;
}

.hero-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
  animation: fadeInUp 0.8s ease 0.4s both;
}

.btn-lg {
  padding: 15px 40px;
  font-size: 18px;
  font-weight: 600;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.courses-page {
  min-height: calc(100vh - 200px);
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
}

.welcome-section {
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.1) 0%, rgba(0, 153, 255, 0.1) 100%);
  padding: 80px 20px;
  text-align: center;
  border-bottom: 2px solid rgba(0, 212, 255, 0.3);
}

.welcome-content {
  max-width: 800px;
  margin: 0 auto;
}

.welcome-title {
  font-size: 48px;
  font-weight: 700;
  background: linear-gradient(135deg, #00d4ff 0%, #00ff88 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 20px;
}

.welcome-subtitle {
  font-size: 20px;
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.6;
}

.mission-vision-section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 60px 20px;
}

.mission-vision-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(450px, 1fr));
  gap: 30px;
}

.mission-card,
.vision-card {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.05) 0%, rgba(255, 255, 255, 0.02) 100%);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 16px;
  padding: 40px;
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.mission-card:hover,
.vision-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 212, 255, 0.2);
}

.card-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.mission-card h3,
.vision-card h3 {
  font-size: 28px;
  font-weight: 600;
  color: #00d4ff;
  margin-bottom: 15px;
}

.mission-card p,
.vision-card p {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.7);
  line-height: 1.8;
}

.courses-section {
  max-width: 1200px;
  margin: 60px auto;
  padding: 0 20px;
}

.instructor-courses-section {
  padding: 30px;
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.05) 0%, rgba(0, 153, 255, 0.05) 100%);
  border-radius: 16px;
  border: 1px solid rgba(0, 212, 255, 0.2);
  margin-bottom: 40px;
}

.mt-40 {
  margin-top: 40px;
}

.section-header {
  text-align: center;
  margin-bottom: 50px;
}

.section-header h2 {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 10px;
  color: #333;
}
</style>
