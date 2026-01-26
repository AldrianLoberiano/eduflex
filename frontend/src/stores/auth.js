import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authService } from '../services'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(authService.getCurrentUser())
  const token = ref(localStorage.getItem('token'))

  const isAuthenticated = computed(() => !!token.value)
  const isStudent = computed(() => user.value?.role === 'STUDENT')
  const isInstructor = computed(() => user.value?.role === 'INSTRUCTOR')
  const isAdmin = computed(() => user.value?.role === 'ADMIN')

  function setUser(userData, authToken) {
    user.value = userData
    token.value = authToken
  }

  function logout() {
    authService.logout()
    user.value = null
    token.value = null
  }

  return {
    user,
    token,
    isAuthenticated,
    isStudent,
    isInstructor,
    isAdmin,
    setUser,
    logout
  }
})
