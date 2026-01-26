<template>
  <div class="course-quizzes-page">
    <div class="page-header">
      <button @click="goBack" class="btn-back">
        <i class="fas fa-arrow-left"></i> Back to Course
      </button>
      <h1>{{ courseName }} - Quizzes</h1>
    </div>

    <QuizList 
      v-if="!showForm && !showTaker"
      ref="quizList"
      :courseId="courseId"
      @create-quiz="showForm = true"
      @edit-quiz="editQuiz"
      @delete-quiz="deleteQuiz"
      @take-quiz="takeQuiz"
      @view-attempts="viewAttempts"
    />

    <QuizForm 
      v-if="showForm"
      :courseId="courseId"
      :quizId="selectedQuizId"
      @close="closeForm"
      @saved="onQuizSaved"
    />

    <QuizTaker
      v-if="showTaker"
      :quizId="selectedQuizId"
      @close="closeTaker"
      @submitted="onQuizSubmitted"
    />
  </div>
</template>

<script>
import QuizList from '../components/QuizList.vue';
import QuizForm from '../components/QuizForm.vue';
import QuizTaker from '../components/QuizTaker.vue';
import axios from 'axios';

export default {
  name: 'CourseQuizzesPage',
  components: {
    QuizList,
    QuizForm,
    QuizTaker
  },
  data() {
    return {
      courseId: 0,
      courseName: '',
      showForm: false,
      showTaker: false,
      selectedQuizId: null
    };
  },
  mounted() {
    const id = parseInt(this.$route.params.courseId);
    if (id && !isNaN(id)) {
      this.courseId = id;
      this.loadCourseInfo();
    } else {
      console.error('Invalid course ID');
      this.$router.push('/');
    }
  },
  methods: {
    async loadCourseInfo() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(
          `http://localhost:8080/api/courses/${this.courseId}`,
          {
            headers: { Authorization: `Bearer ${token}` }
          }
        );
        this.courseName = response.data.data.title;
      } catch (error) {
        console.error('Error loading course:', error);
        this.courseName = 'Course Quizzes';
      }
    },
    goBack() {
      this.$router.back();
    },
    editQuiz(quizId) {
      this.selectedQuizId = quizId;
      this.showForm = true;
    },
    async deleteQuiz(quizId) {
      if (!confirm('Are you sure you want to delete this quiz? This action cannot be undone.')) {
        return;
      }

      try {
        const token = localStorage.getItem('token');
        await axios.delete(
          `http://localhost:8080/api/quizzes/${quizId}`,
          {
            headers: { Authorization: `Bearer ${token}` }
          }
        );
        alert('Quiz deleted successfully');
        this.$refs.quizList.refresh();
      } catch (error) {
        console.error('Error deleting quiz:', error);
        alert(error.response?.data?.message || 'Failed to delete quiz');
      }
    },
    takeQuiz(quizId) {
      this.selectedQuizId = quizId;
      this.showTaker = true;
    },
    viewAttempts(quizId) {
      alert('View quiz attempts feature - Coming soon!');
      // TODO: Implement quiz attempts view
    },
    closeForm() {
      this.showForm = false;
      this.selectedQuizId = null;
    },
    closeTaker() {
      this.showTaker = false;
      this.selectedQuizId = null;
    },
    onQuizSaved() {
      this.$refs.quizList.refresh();
    },
    onQuizSubmitted(result) {
      this.$refs.quizList.refresh();
    }
  }
};
</script>

<style scoped>
.course-quizzes-page {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 20px;
}

.page-header {
  max-width: 1400px;
  margin: 0 auto 30px;
  display: flex;
  align-items: center;
  gap: 20px;
}

.btn-back {
  padding: 10px 16px;
  background: white;
  border: 2px solid #ecf0f1;
  border-radius: 8px;
  color: #2c3e50;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s;
}

.btn-back:hover {
  background: #ecf0f1;
  border-color: #bdc3c7;
}

.page-header h1 {
  color: #2c3e50;
  font-size: 32px;
  margin: 0;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .page-header h1 {
    font-size: 24px;
  }
}
</style>
