<template>
  <div class="quiz-form-overlay" @click.self="$emit('close')">
    <div class="quiz-form-modal">
      <div class="modal-header">
        <h2>{{ quizId ? 'Edit Quiz' : 'Create Quiz' }}</h2>
        <button @click="$emit('close')" class="close-btn">
          <i class="fas fa-times"></i>
        </button>
      </div>

      <div class="modal-body">
        <form @submit.prevent="saveQuiz">
          <div class="form-section">
            <h3>Basic Information</h3>
            
            <div class="form-group">
              <label>Title *</label>
              <input v-model="quiz.title" type="text" required placeholder="Quiz title">
            </div>

            <div class="form-group">
              <label>Description</label>
              <textarea v-model="quiz.description" rows="3" placeholder="Brief description of the quiz"></textarea>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>Time Limit (minutes) *</label>
                <input v-model.number="quiz.timeLimit" type="number" min="1" required>
              </div>

              <div class="form-group">
                <label>Passing Score (%) *</label>
                <input v-model.number="quiz.passingScore" type="number" min="0" max="100" required>
              </div>

              <div class="form-group">
                <label>Order Index</label>
                <input v-model.number="quiz.orderIndex" type="number" min="0">
              </div>
            </div>

            <div class="form-group">
              <label class="checkbox-label">
                <input v-model="quiz.published" type="checkbox">
                <span>Publish Quiz (students can take it)</span>
              </label>
            </div>
          </div>

          <div class="form-section">
            <div class="section-header">
              <h3>Questions</h3>
              <button type="button" @click="addQuestion" class="btn-add">
                <i class="fas fa-plus"></i> Add Question
              </button>
            </div>

            <div v-if="quiz.questions.length === 0" class="empty-questions">
              <i class="fas fa-clipboard-question"></i>
              <p>No questions added yet. Click "Add Question" to start.</p>
            </div>

            <div v-for="(question, qIndex) in quiz.questions" :key="qIndex" class="question-card">
              <div class="question-header">
                <span class="question-number">Question {{ qIndex + 1 }}</span>
                <button type="button" @click="removeQuestion(qIndex)" class="btn-remove">
                  <i class="fas fa-trash"></i>
                </button>
              </div>

              <div class="form-group">
                <label>Question Text *</label>
                <textarea v-model="question.question" rows="2" required placeholder="Enter your question"></textarea>
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label>Question Type *</label>
                  <select v-model="question.type" @change="onQuestionTypeChange(qIndex)" required>
                    <option value="MULTIPLE_CHOICE">Multiple Choice</option>
                    <option value="TRUE_FALSE">True/False</option>
                    <option value="ESSAY">Essay</option>
                  </select>
                </div>

                <div class="form-group">
                  <label>Points *</label>
                  <input v-model.number="question.points" type="number" min="1" required>
                </div>
              </div>

              <!-- Options for Multiple Choice and True/False -->
              <div v-if="question.type === 'MULTIPLE_CHOICE' || question.type === 'TRUE_FALSE'" class="options-section">
                <label>Answer Options</label>
                
                <div v-for="(option, oIndex) in question.options" :key="oIndex" class="option-item">
                  <input v-model="option.optionText" type="text" required placeholder="Option text">
                  <label class="checkbox-label">
                    <input v-model="option.isCorrect" type="checkbox">
                    <span>Correct</span>
                  </label>
                  <button v-if="question.type === 'MULTIPLE_CHOICE'" type="button" @click="removeOption(qIndex, oIndex)" class="btn-remove-small">
                    <i class="fas fa-times"></i>
                  </button>
                </div>

                <button v-if="question.type === 'MULTIPLE_CHOICE'" type="button" @click="addOption(qIndex)" class="btn-add-small">
                  <i class="fas fa-plus"></i> Add Option
                </button>
              </div>

              <div v-if="question.type === 'ESSAY'" class="essay-note">
                <i class="fas fa-info-circle"></i>
                <span>Essay questions require manual grading by instructors</span>
              </div>
            </div>
          </div>

          <div class="modal-footer">
            <button type="button" @click="$emit('close')" class="btn-secondary">Cancel</button>
            <button type="submit" :disabled="saving" class="btn-primary">
              <i v-if="saving" class="fas fa-spinner fa-spin"></i>
              <span>{{ saving ? 'Saving...' : 'Save Quiz' }}</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'QuizForm',
  props: {
    courseId: {
      type: Number,
      required: true
    },
    quizId: {
      type: Number,
      default: null
    },
    lessonId: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      quiz: {
        title: '',
        description: '',
        timeLimit: 30,
        passingScore: 70,
        orderIndex: 0,
        published: false,
        questions: []
      },
      saving: false
    };
  },
  mounted() {
    if (this.quizId) {
      this.loadQuiz();
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
      } catch (error) {
        console.error('Error loading quiz:', error);
        alert('Failed to load quiz');
      }
    },
    addQuestion() {
      this.quiz.questions.push({
        question: '',
        type: 'MULTIPLE_CHOICE',
        points: 10,
        orderIndex: this.quiz.questions.length,
        options: [
          { optionText: '', isCorrect: false, orderIndex: 0 },
          { optionText: '', isCorrect: false, orderIndex: 1 }
        ]
      });
    },
    removeQuestion(index) {
      if (confirm('Remove this question?')) {
        this.quiz.questions.splice(index, 1);
        this.quiz.questions.forEach((q, i) => q.orderIndex = i);
      }
    },
    onQuestionTypeChange(qIndex) {
      const question = this.quiz.questions[qIndex];
      if (question.type === 'TRUE_FALSE') {
        question.options = [
          { optionText: 'True', isCorrect: false, orderIndex: 0 },
          { optionText: 'False', isCorrect: false, orderIndex: 1 }
        ];
      } else if (question.type === 'ESSAY') {
        question.options = [];
      } else if (question.options.length === 0) {
        question.options = [
          { optionText: '', isCorrect: false, orderIndex: 0 },
          { optionText: '', isCorrect: false, orderIndex: 1 }
        ];
      }
    },
    addOption(qIndex) {
      const question = this.quiz.questions[qIndex];
      question.options.push({
        optionText: '',
        isCorrect: false,
        orderIndex: question.options.length
      });
    },
    removeOption(qIndex, oIndex) {
      const question = this.quiz.questions[qIndex];
      if (question.options.length > 2) {
        question.options.splice(oIndex, 1);
        question.options.forEach((opt, i) => opt.orderIndex = i);
      } else {
        alert('At least 2 options are required');
      }
    },
    async saveQuiz() {
      if (!this.validateQuiz()) {
        return;
      }

      this.saving = true;
      try {
        const token = localStorage.getItem('token');
        const payload = {
          ...this.quiz,
          courseId: this.courseId,
          lessonId: this.lessonId
        };

        if (this.quizId) {
          await axios.put(
            `http://localhost:8080/api/quizzes/${this.quizId}`,
            payload,
            {
              headers: { Authorization: `Bearer ${token}` }
            }
          );
          alert('Quiz updated successfully!');
        } else {
          await axios.post(
            'http://localhost:8080/api/quizzes',
            payload,
            {
              headers: { Authorization: `Bearer ${token}` }
            }
          );
          alert('Quiz created successfully!');
        }

        this.$emit('saved');
        this.$emit('close');
      } catch (error) {
        console.error('Error saving quiz:', error);
        alert(error.response?.data?.message || 'Failed to save quiz');
      } finally {
        this.saving = false;
      }
    },
    validateQuiz() {
      if (this.quiz.questions.length === 0) {
        alert('Please add at least one question');
        return false;
      }

      for (let i = 0; i < this.quiz.questions.length; i++) {
        const q = this.quiz.questions[i];
        if (!q.question.trim()) {
          alert(`Question ${i + 1} text is required`);
          return false;
        }

        if (q.type === 'MULTIPLE_CHOICE' || q.type === 'TRUE_FALSE') {
          if (!q.options.some(opt => opt.isCorrect)) {
            alert(`Question ${i + 1} must have at least one correct answer`);
            return false;
          }
          if (q.options.some(opt => !opt.optionText.trim())) {
            alert(`Question ${i + 1} has empty options`);
            return false;
          }
        }
      }

      return true;
    }
  }
};
</script>

<style scoped>
.quiz-form-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
  overflow-y: auto;
}

.quiz-form-modal {
  background: white;
  border-radius: 16px;
  max-width: 900px;
  width: 100%;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
}

.modal-header {
  padding: 24px;
  border-bottom: 1px solid #ecf0f1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h2 {
  margin: 0;
  color: #2c3e50;
  font-size: 24px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #7f8c8d;
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background 0.2s;
}

.close-btn:hover {
  background: #ecf0f1;
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
  flex: 1;
}

.form-section {
  margin-bottom: 32px;
}

.form-section h3 {
  color: #2c3e50;
  font-size: 18px;
  margin-bottom: 16px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  color: #2c3e50;
  font-weight: 600;
  font-size: 14px;
}

.form-group input[type="text"],
.form-group input[type="number"],
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #dce4ec;
  border-radius: 8px;
  font-size: 14px;
  transition: border-color 0.2s;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: #3498db;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 16px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-weight: normal;
}

.checkbox-label input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.empty-questions {
  text-align: center;
  padding: 40px 20px;
  color: #7f8c8d;
  background: #f8f9fa;
  border-radius: 8px;
}

.empty-questions i {
  font-size: 48px;
  margin-bottom: 12px;
  opacity: 0.3;
}

.question-card {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 16px;
  border: 2px solid #ecf0f1;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.question-number {
  font-weight: 700;
  color: #3498db;
  font-size: 16px;
}

.options-section {
  margin-top: 16px;
}

.options-section > label {
  display: block;
  margin-bottom: 8px;
  color: #2c3e50;
  font-weight: 600;
  font-size: 14px;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.option-item input[type="text"] {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #dce4ec;
  border-radius: 6px;
  font-size: 14px;
}

.essay-note {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px;
  background: #fff3cd;
  border-radius: 8px;
  color: #856404;
  font-size: 14px;
  margin-top: 12px;
}

.btn-add, .btn-add-small {
  background: #27ae60;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: background 0.2s;
}

.btn-add:hover, .btn-add-small:hover {
  background: #229954;
}

.btn-add-small {
  padding: 8px 12px;
  font-size: 13px;
  margin-top: 8px;
}

.btn-remove, .btn-remove-small {
  background: #e74c3c;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-remove:hover, .btn-remove-small:hover {
  background: #c0392b;
}

.btn-remove-small {
  padding: 4px 8px;
  font-size: 12px;
}

.modal-footer {
  padding: 20px 24px;
  border-top: 1px solid #ecf0f1;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.btn-primary, .btn-secondary {
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

.btn-primary {
  background: #3498db;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: #2980b9;
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-secondary {
  background: #ecf0f1;
  color: #2c3e50;
}

.btn-secondary:hover {
  background: #bdc3c7;
}
</style>
