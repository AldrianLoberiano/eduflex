<template>
  <div class="quiz-taker-overlay" @click.self="confirmExit">
    <div class="quiz-taker-modal">
      <div v-if="!quizStarted" class="quiz-intro">
        <h2>{{ quiz.title }}</h2>
        <p class="quiz-description">{{ quiz.description }}</p>
        
        <div class="quiz-info">
          <div class="info-item">
            <i class="fas fa-question-circle"></i>
            <span>{{ quiz.totalQuestions }} Questions</span>
          </div>
          <div class="info-item">
            <i class="fas fa-clock"></i>
            <span>{{ quiz.timeLimit }} Minutes</span>
          </div>
          <div class="info-item">
            <i class="fas fa-award"></i>
            <span>Passing Score: {{ quiz.passingScore }}%</span>
          </div>
        </div>

        <div class="instructions">
          <h3>Instructions:</h3>
          <ul>
            <li>You have {{ quiz.timeLimit }} minutes to complete this quiz</li>
            <li>All questions must be answered before submission</li>
            <li>You need {{ quiz.passingScore }}% to pass</li>
            <li>Once started, the timer cannot be paused</li>
          </ul>
        </div>

        <div class="intro-actions">
          <button @click="$emit('close')" class="btn-secondary">Cancel</button>
          <button @click="startQuiz" class="btn-primary">
            <i class="fas fa-play"></i> Start Quiz
          </button>
        </div>
      </div>

      <div v-else class="quiz-content">
        <div class="quiz-header">
          <div class="quiz-progress">
            <span class="progress-text">Question {{ currentQuestionIndex + 1 }} of {{ questions.length }}</span>
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: progressPercentage + '%' }"></div>
            </div>
          </div>
          
          <div class="quiz-timer" :class="{ 'timer-warning': timeRemaining < 300 }">
            <i class="fas fa-clock"></i>
            <span>{{ formattedTime }}</span>
          </div>
        </div>

        <div class="question-container">
          <div class="question-text">
            <span class="question-label">Question {{ currentQuestionIndex + 1 }}</span>
            <h3>{{ currentQuestion.question }}</h3>
            <span class="question-points">{{ currentQuestion.points }} points</span>
          </div>

          <div class="answer-section">
            <!-- Multiple Choice -->
            <div v-if="currentQuestion.type === 'MULTIPLE_CHOICE'" class="options-list">
              <label 
                v-for="option in currentQuestion.options" 
                :key="option.id"
                class="option-label"
              >
                <input 
                  type="radio" 
                  :name="'question-' + currentQuestion.id"
                  :value="option.id"
                  v-model="answers[currentQuestion.id]"
                >
                <span class="option-text">{{ option.optionText }}</span>
              </label>
            </div>

            <!-- True/False -->
            <div v-else-if="currentQuestion.type === 'TRUE_FALSE'" class="options-list">
              <label 
                v-for="option in currentQuestion.options" 
                :key="option.id"
                class="option-label"
              >
                <input 
                  type="radio" 
                  :name="'question-' + currentQuestion.id"
                  :value="option.id"
                  v-model="answers[currentQuestion.id]"
                >
                <span class="option-text">{{ option.optionText }}</span>
              </label>
            </div>

            <!-- Essay -->
            <div v-else-if="currentQuestion.type === 'ESSAY'" class="essay-answer">
              <textarea 
                v-model="answers[currentQuestion.id]"
                rows="8"
                placeholder="Type your answer here..."
              ></textarea>
              <span class="essay-hint">
                <i class="fas fa-info-circle"></i>
                This will be manually graded by your instructor
              </span>
            </div>
          </div>
        </div>

        <div class="quiz-navigation">
          <button 
            @click="previousQuestion" 
            :disabled="currentQuestionIndex === 0"
            class="btn-nav"
          >
            <i class="fas fa-arrow-left"></i> Previous
          </button>

          <div class="question-dots">
            <span 
              v-for="(q, index) in questions" 
              :key="q.id"
              @click="goToQuestion(index)"
              class="question-dot"
              :class="{ 
                'active': index === currentQuestionIndex,
                'answered': isQuestionAnswered(q.id)
              }"
            >
              {{ index + 1 }}
            </span>
          </div>

          <button 
            v-if="currentQuestionIndex < questions.length - 1"
            @click="nextQuestion"
            class="btn-nav"
          >
            Next <i class="fas fa-arrow-right"></i>
          </button>

          <button 
            v-else
            @click="submitQuiz"
            :disabled="submitting"
            class="btn-submit"
          >
            <i v-if="submitting" class="fas fa-spinner fa-spin"></i>
            <span>{{ submitting ? 'Submitting...' : 'Submit Quiz' }}</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'QuizTaker',
  props: {
    quizId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      quiz: {},
      questions: [],
      quizStarted: false,
      currentQuestionIndex: 0,
      answers: {},
      timeRemaining: 0,
      timer: null,
      submitting: false
    };
  },
  computed: {
    currentQuestion() {
      return this.questions[this.currentQuestionIndex] || {};
    },
    progressPercentage() {
      return ((this.currentQuestionIndex + 1) / this.questions.length) * 100;
    },
    formattedTime() {
      const minutes = Math.floor(this.timeRemaining / 60);
      const seconds = this.timeRemaining % 60;
      return `${minutes}:${seconds.toString().padStart(2, '0')}`;
    }
  },
  mounted() {
    this.loadQuiz();
  },
  beforeUnmount() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  methods: {
    async loadQuiz() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(
          `http://localhost:8080/api/quizzes/${this.quizId}`,
          {
            headers: { Authorization: `Bearer ${token}` }
          }
        );
        this.quiz = response.data.data;
        this.questions = this.quiz.questions || [];
        this.timeRemaining = this.quiz.timeLimit * 60;
      } catch (error) {
        console.error('Error loading quiz:', error);
        alert('Failed to load quiz');
        this.$emit('close');
      }
    },
    startQuiz() {
      this.quizStarted = true;
      this.startTimer();
    },
    startTimer() {
      this.timer = setInterval(() => {
        this.timeRemaining--;
        if (this.timeRemaining <= 0) {
          clearInterval(this.timer);
          alert('Time is up! Submitting your quiz...');
          this.submitQuiz();
        }
      }, 1000);
    },
    nextQuestion() {
      if (this.currentQuestionIndex < this.questions.length - 1) {
        this.currentQuestionIndex++;
      }
    },
    previousQuestion() {
      if (this.currentQuestionIndex > 0) {
        this.currentQuestionIndex--;
      }
    },
    goToQuestion(index) {
      this.currentQuestionIndex = index;
    },
    isQuestionAnswered(questionId) {
      return this.answers[questionId] !== undefined && this.answers[questionId] !== null && this.answers[questionId] !== '';
    },
    async submitQuiz() {
      if (!this.validateAnswers()) {
        return;
      }

      if (!confirm('Are you sure you want to submit your quiz? You cannot change your answers after submission.')) {
        return;
      }

      this.submitting = true;
      clearInterval(this.timer);

      try {
        const token = localStorage.getItem('token');
        const answers = Object.entries(this.answers).map(([questionId, answer]) => {
          const question = this.questions.find(q => q.id === parseInt(questionId));
          
          if (question.type === 'ESSAY') {
            return {
              questionId: parseInt(questionId),
              textAnswer: answer
            };
          } else {
            return {
              questionId: parseInt(questionId),
              selectedOptionId: answer
            };
          }
        });

        const response = await axios.post(
          'http://localhost:8080/api/quizzes/submit',
          {
            quizId: this.quizId,
            answers: answers
          },
          {
            headers: { Authorization: `Bearer ${token}` }
          }
        );

        alert(`Quiz submitted successfully!\nYour Score: ${response.data.data.score}%\nStatus: ${response.data.data.passed ? 'PASSED ✓' : 'FAILED ✗'}`);
        this.$emit('submitted', response.data.data);
        this.$emit('close');
      } catch (error) {
        console.error('Error submitting quiz:', error);
        alert(error.response?.data?.message || 'Failed to submit quiz');
      } finally {
        this.submitting = false;
      }
    },
    validateAnswers() {
      const unanswered = this.questions.filter(q => !this.isQuestionAnswered(q.id));
      
      if (unanswered.length > 0) {
        const confirm = window.confirm(`You have ${unanswered.length} unanswered question(s). Do you want to submit anyway?`);
        return confirm;
      }
      
      return true;
    },
    confirmExit() {
      if (this.quizStarted) {
        if (confirm('Are you sure you want to exit? Your progress will be lost.')) {
          clearInterval(this.timer);
          this.$emit('close');
        }
      } else {
        this.$emit('close');
      }
    }
  }
};
</script>

<style scoped>
.quiz-taker-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
}

.quiz-taker-modal {
  background: white;
  border-radius: 16px;
  max-width: 900px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.5);
}

.quiz-intro {
  padding: 40px;
  text-align: center;
}

.quiz-intro h2 {
  color: #2c3e50;
  font-size: 32px;
  margin-bottom: 16px;
}

.quiz-description {
  color: #7f8c8d;
  font-size: 16px;
  margin-bottom: 32px;
}

.quiz-info {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-bottom: 32px;
  flex-wrap: wrap;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #2c3e50;
  font-size: 16px;
  font-weight: 600;
}

.info-item i {
  color: #3498db;
  font-size: 20px;
}

.instructions {
  background: #f8f9fa;
  padding: 24px;
  border-radius: 12px;
  text-align: left;
  margin-bottom: 32px;
}

.instructions h3 {
  color: #2c3e50;
  margin-bottom: 12px;
}

.instructions ul {
  color: #7f8c8d;
  line-height: 1.8;
}

.intro-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.quiz-content {
  display: flex;
  flex-direction: column;
  height: 85vh;
}

.quiz-header {
  padding: 24px;
  border-bottom: 2px solid #ecf0f1;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

.quiz-progress {
  flex: 1;
  min-width: 200px;
}

.progress-text {
  display: block;
  color: #7f8c8d;
  font-size: 14px;
  margin-bottom: 8px;
}

.progress-bar {
  height: 8px;
  background: #ecf0f1;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: #3498db;
  transition: width 0.3s;
}

.quiz-timer {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 24px;
  font-weight: 700;
  color: #2c3e50;
}

.timer-warning {
  color: #e74c3c;
  animation: pulse 1s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.6; }
}

.question-container {
  flex: 1;
  overflow-y: auto;
  padding: 32px;
}

.question-text {
  margin-bottom: 32px;
}

.question-label {
  color: #3498db;
  font-weight: 600;
  font-size: 14px;
  text-transform: uppercase;
}

.question-text h3 {
  color: #2c3e50;
  font-size: 22px;
  margin: 12px 0;
  line-height: 1.5;
}

.question-points {
  color: #7f8c8d;
  font-size: 14px;
}

.options-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.option-label {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  border: 2px solid #ecf0f1;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.option-label:hover {
  border-color: #3498db;
  background: #f8f9fa;
}

.option-label input[type="radio"] {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.option-text {
  flex: 1;
  color: #2c3e50;
  font-size: 16px;
}

.essay-answer textarea {
  width: 100%;
  padding: 16px;
  border: 2px solid #ecf0f1;
  border-radius: 12px;
  font-size: 16px;
  font-family: inherit;
  resize: vertical;
}

.essay-answer textarea:focus {
  outline: none;
  border-color: #3498db;
}

.essay-hint {
  display: block;
  margin-top: 8px;
  color: #7f8c8d;
  font-size: 14px;
}

.quiz-navigation {
  padding: 24px;
  border-top: 2px solid #ecf0f1;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.question-dots {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: center;
}

.question-dot {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #ecf0f1;
  color: #7f8c8d;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.question-dot:hover {
  background: #bdc3c7;
}

.question-dot.active {
  background: #3498db;
  color: white;
}

.question-dot.answered {
  background: #27ae60;
  color: white;
}

.btn-nav, .btn-submit, .btn-primary, .btn-secondary {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-nav {
  background: #ecf0f1;
  color: #2c3e50;
}

.btn-nav:hover:not(:disabled) {
  background: #bdc3c7;
}

.btn-nav:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.btn-submit {
  background: #27ae60;
  color: white;
}

.btn-submit:hover:not(:disabled) {
  background: #229954;
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-primary {
  background: #3498db;
  color: white;
  padding: 16px 32px;
  font-size: 16px;
}

.btn-primary:hover {
  background: #2980b9;
}

.btn-secondary {
  background: #ecf0f1;
  color: #2c3e50;
  padding: 16px 32px;
  font-size: 16px;
}

.btn-secondary:hover {
  background: #bdc3c7;
}
</style>
