<template>
  <div v-if="isOpen" class="modal-overlay" @click="closeModal">
    <div class="modal-container" @click.stop>
      <button class="close-btn" @click="closeModal">&times;</button>
      
      <div class="modal-header">
        <h2>Create Account</h2>
        <p>Join EduFlex and start learning today!</p>
      </div>

      <form @submit.prevent="handleRegister" class="modal-form">
        <div v-if="error" class="error-message">{{ error }}</div>
        <div v-if="success" class="success-message">{{ success }}</div>

        <div class="form-row">
          <div class="form-group">
            <label for="firstName">First Name</label>
            <input
              type="text"
              id="firstName"
              v-model="form.firstName"
              placeholder="First name"
              required
            />
          </div>

          <div class="form-group">
            <label for="lastName">Last Name</label>
            <input
              type="text"
              id="lastName"
              v-model="form.lastName"
              placeholder="Last name"
              required
            />
          </div>
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input
            type="email"
            id="email"
            v-model="form.email"
            placeholder="Enter your email"
            required
          />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input
            type="password"
            id="password"
            v-model="form.password"
            placeholder="Create a password"
            required
            minlength="6"
          />
        </div>

        <div class="form-group">
          <label for="role">I want to:</label>
          <select id="role" v-model="form.role" required>
            <option value="STUDENT">Learn (Student)</option>
            <option value="INSTRUCTOR">Teach (Instructor)</option>
          </select>
        </div>

        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? 'Creating Account...' : 'Create Account' }}
        </button>

        <div class="modal-footer">
          <p>Already have an account? 
            <a href="#" @click.prevent="switchToLogin">Login here</a>
          </p>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '../services'
import { useAuthStore } from '../stores/auth'

const props = defineProps({
  isOpen: Boolean
})

const emit = defineEmits(['close', 'switch-to-login'])

const router = useRouter()
const authStore = useAuthStore()

const form = ref({
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  role: 'STUDENT'
})
const loading = ref(false)
const error = ref('')
const success = ref('')

watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    error.value = ''
    success.value = ''
    form.value = {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
      role: 'STUDENT'
    }
  }
})

const closeModal = () => {
  emit('close')
}

const switchToLogin = () => {
  emit('switch-to-login')
}

const handleRegister = async () => {
  loading.value = true
  error.value = ''
  success.value = ''

  try {
    // Register the user
    await authService.register(form.value)
    
    success.value = 'Account created successfully! Logging you in...'
    
    // Wait 1 second then automatically login
    setTimeout(async () => {
      try {
        // Automatically login with the same credentials
        const response = await authService.login({
          email: form.value.email,
          password: form.value.password
        })
        
        authStore.setUser(response.user, response.token)
        closeModal()
        
        // Navigate based on role
        if (response.user.role === 'INSTRUCTOR') {
          await router.push('/instructor/dashboard')
        } else if (response.user.role === 'ADMIN') {
          await router.push('/admin/dashboard')
        } else {
          await router.push('/student/my-learning')
        }
      } catch (loginErr) {
        // If auto-login fails, just switch to login modal
        console.error('Auto-login failed:', loginErr)
        switchToLogin()
      }
    }, 1000)
  } catch (err) {
    error.value = err.response?.data?.message || 'Registration failed. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.modal-container {
  background: white;
  border-radius: 12px;
  padding: 40px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  position: relative;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from {
    transform: translateY(50px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.close-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  background: none;
  border: none;
  font-size: 32px;
  color: #666;
  cursor: pointer;
  transition: color 0.3s;
  line-height: 1;
  padding: 0;
  width: 32px;
  height: 32px;
}

.close-btn:hover {
  color: #333;
}

.modal-header {
  text-align: center;
  margin-bottom: 30px;
}

.modal-header h2 {
  color: #333;
  margin-bottom: 10px;
  font-size: 28px;
}

.modal-header p {
  color: #666;
  font-size: 14px;
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.error-message {
  background: #fee;
  color: #c33;
  padding: 12px;
  border-radius: 6px;
  font-size: 14px;
  text-align: center;
}

.success-message {
  background: #dff0d8;
  color: #3c763d;
  padding: 12px;
  border-radius: 6px;
  font-size: 14px;
  text-align: center;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  color: #333;
  font-weight: 600;
  font-size: 14px;
}

.form-group input,
.form-group select {
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 15px;
  transition: border-color 0.3s;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #4CAF50;
}

.form-group select {
  cursor: pointer;
}

.submit-btn {
  background: #4CAF50;
  color: white;
  padding: 14px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.3s;
  margin-top: 10px;
}

.submit-btn:hover:not(:disabled) {
  background: #45a049;
}

.submit-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.modal-footer {
  text-align: center;
  margin-top: 20px;
}

.modal-footer p {
  color: #666;
  font-size: 14px;
}

.modal-footer a {
  color: #4CAF50;
  text-decoration: none;
  font-weight: 600;
}

.modal-footer a:hover {
  text-decoration: underline;
}
</style>
