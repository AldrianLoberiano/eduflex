<template>
  <div class="quiz-list">
    <div class="quiz-header">
      <h2>{{ lessonId ? 'Lesson Quizzes' : 'Course Quizzes' }}</h2>
      <button v-if="isInstructor" @click="$emit('create-quiz')" class="btn-primary">
        <i class="fas fa-plus"></i> Create Quiz
      </button>
    </div>

    <div v-if="loading" class="loading">
      <i class="fas fa-spinner fa-spin"></i> Loading quizzes...
    </div>

    <div v-else-if="quizzes.length === 0" class="empty-state">
      <i class="fas fa-clipboard-question"></i>
      <p>No quizzes available yet</p>
    </div>

    <div v-else class="quizzes-grid">
      <div v-for="quiz in quizzes" :key="quiz.id" class="quiz-card">
        <div class="quiz-card-header">
          <h3>{{ quiz.title }}</h3>
          <span v-if="!quiz.published" class="badge-draft">Draft</span>
          <span v-else class="badge-published">Published</span>
        </div>
        
        <p class="quiz-description">{{ quiz.description || 'No description' }}</p>
        
        <div class="quiz-meta">
          <div class="meta-item">
            <i class="fas fa-question-circle"></i>
            <span>{{ quiz.totalQuestions }} questions</span>
          </div>
          <div class="meta-item">
            <i class="fas fa-clock"></i>
            <span>{{ quiz.timeLimit }} min</span>
          </div>
          <div class="meta-item">
            <i class="fas fa-award"></i>
            <span>Pass: {{ quiz.passingScore }}%</span>
          </div>
        </div>

        <div v-if="quiz.lastAttempt" class="last-attempt">
          <span :class="{'passed': quiz.lastAttempt.passed, 'failed': !quiz.lastAttempt.passed}">
            Last Score: {{ quiz.lastAttempt.score }}% - {{ quiz.lastAttempt.passed ? 'Passed' : 'Failed' }}
          </span>
        </div>

        <div class="quiz-actions">
          <button v-if="isStudent && quiz.published" @click="$emit('take-quiz', quiz.id)" class="btn-primary">
            <i class="fas fa-play"></i> Take Quiz
          </button>
          
          <button v-if="isInstructor" @click="$emit('edit-quiz', quiz.id)" class="btn-secondary">
            <i class="fas fa-edit"></i> Edit
          </button>
          
          <button v-if="isInstructor" @click="$emit('delete-quiz', quiz.id)" class="btn-danger">
            <i class="fas fa-trash"></i>
          </button>
          
          <button @click="$emit('view-attempts', quiz.id)" class="btn-secondary">
            <i class="fas fa-chart-line"></i> Results
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'QuizList',
  props: {
    courseId: {
      type: Number,
      required: true
    },
    lessonId: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      quizzes: [],
      loading: false,
      userRole: null
    };
  },
  computed: {
    isInstructor() {
      return this.userRole === 'INSTRUCTOR' || this.userRole === 'ADMIN';
    },
    isStudent() {
      return this.userRole === 'STUDENT';
    }
  },
  mounted() {
    this.loadUserRole();
    this.loadQuizzes();
  },
  methods: {
    loadUserRole() {
      try {
        const userStr = localStorage.getItem('user');
        if (userStr) {
          const user = JSON.parse(userStr);
          this.userRole = user.role;
        }
      } catch (error) {
        console.error('Error loading user role:', error);
      }
    },
    async loadQuizzes() {
      this.loading = true;
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(
          `http://localhost:8080/api/quizzes/course/${this.courseId}`,
          {
            headers: { Authorization: `Bearer ${token}` }
          }
        );
        this.quizzes = response.data.data;
      } catch (error) {
        console.error('Error loading quizzes:', error);
        alert('Failed to load quizzes');
      } finally {
        this.loading = false;
      }
    },
    refresh() {
      this.loadQuizzes();
    }
  }
};
</script>

<style scoped>
.quiz-list {
  padding: 20px;
}

.quiz-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.quiz-header h2 {
  color: #2c3e50;
  font-size: 28px;
  margin: 0;
}

.loading, .empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #7f8c8d;
}

.empty-state i {
  font-size: 64px;
  margin-bottom: 20px;
  opacity: 0.3;
}

.quizzes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 24px;
}

.quiz-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.quiz-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.quiz-card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
  gap: 12px;
}

.quiz-card-header h3 {
  color: #2c3e50;
  font-size: 20px;
  margin: 0;
  flex: 1;
}

.badge-draft, .badge-published {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  white-space: nowrap;
}

.badge-draft {
  background: #f39c12;
  color: white;
}

.badge-published {
  background: #27ae60;
  color: white;
}

.quiz-description {
  color: #7f8c8d;
  font-size: 14px;
  margin-bottom: 16px;
  line-height: 1.5;
}

.quiz-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #7f8c8d;
  font-size: 14px;
}

.meta-item i {
  color: #3498db;
}

.last-attempt {
  padding: 10px;
  border-radius: 8px;
  margin-bottom: 16px;
  text-align: center;
  font-weight: 600;
  font-size: 14px;
}

.last-attempt .passed {
  color: #27ae60;
}

.last-attempt .failed {
  color: #e74c3c;
}

.quiz-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.btn-primary, .btn-secondary, .btn-danger {
  padding: 10px 16px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 6px;
}

.btn-primary {
  background: #3498db;
  color: white;
  flex: 1;
}

.btn-primary:hover {
  background: #2980b9;
}

.btn-secondary {
  background: #ecf0f1;
  color: #2c3e50;
}

.btn-secondary:hover {
  background: #bdc3c7;
}

.btn-danger {
  background: #e74c3c;
  color: white;
}

.btn-danger:hover {
  background: #c0392b;
}
</style>
