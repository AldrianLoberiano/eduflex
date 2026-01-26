<template>
  <div v-if="isOpen" class="modal-overlay" @click="closeModal">
    <div class="modal-container" @click.stop>
      <button class="close-btn" @click="closeModal">&times;</button>
      
      <div class="modal-header">
        <h2>Login to EduFlex</h2>
        <p>Welcome back! Please login to your account.</p>
      </div>

      <form @submit.prevent="handleLogin" class="modal-form">
        <div v-if="error" class="error-message">{{ error }}</div>

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
            placeholder="Enter your password"
            required
          />
        </div>

        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? 'Logging in...' : 'Login' }}
        </button>

        <div class="modal-footer">
          <p>Don't have an account? 
            <a href="#" @click.prevent="switchToRegister">Register here</a>
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

const emit = defineEmits(['close', 'switch-to-register'])

const router = useRouter()
const authStore = useAuthStore()
const form = ref({
  email: '',
  password: ''
})
const loading = ref(false)
const error = ref('')

watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    error.value = ''
    form.value = { email: '', password: '' }
  }
})

const closeModal = () => {
  emit('close')
}

const switchToRegister = () => {
  emit('switch-to-register')
}

const handleLogin = async () => {
  loading.value = true
  error.value = ''

  try {
    const response = await authService.login({
      email: form.value.email,
      password: form.value.password
    })

    // authService.login already saves to localStorage
    // Now update the Pinia store
    authStore.setUser(response.user, response.token)

    closeModal()

    // Navigate based on role
    if (response.user.role === 'INSTRUCTOR') {
      await router.push('/instructor/dashboard')
    } else if (response.user.role === 'ADMIN') {
      await router.push('/admin/dashboard')
    } else if (response.user.role === 'STUDENT') {
      await router.push('/student/my-learning')
    }
  } catch (err) {
    error.value = err.response?.data?.message || 'Login failed. Please try again.'
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
  max-width: 450px;
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

.form-group input {
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 15px;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: #4CAF50;
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
