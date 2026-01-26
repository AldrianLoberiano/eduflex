import api from './api'

export const authService = {
  async register(data) {
    const response = await api.post('/auth/register', data)
    return response.data
  },

  async login(credentials) {
    const response = await api.post('/auth/login', credentials)
    const authData = response.data?.data
    if (authData?.token) {
      const user = {
        id: authData.id,
        firstName: authData.firstName,
        lastName: authData.lastName,
        email: authData.email,
        role: authData.role
      }
      localStorage.setItem('token', authData.token)
      localStorage.setItem('user', JSON.stringify(user))
      return { token: authData.token, user }
    }
    return { token: null, user: null }
  },

  logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  },

  getCurrentUser() {
    const userStr = localStorage.getItem('user')
    if (!userStr || userStr === 'undefined' || userStr === 'null') {
      return null
    }
    try {
      return JSON.parse(userStr)
    } catch (e) {
      console.error('Error parsing user data:', e)
      localStorage.removeItem('user')
      return null
    }
  },

  isAuthenticated() {
    return !!localStorage.getItem('token')
  }
}

export const courseService = {
  async getAllCourses() {
    const response = await api.get('/courses')
    return response.data
  },

  async getCourseById(id) {
    const response = await api.get(`/courses/${id}`)
    return response.data
  },

  async createCourse(data) {
    const response = await api.post('/instructor/courses', data)
    return response.data
  },

  async updateCourse(id, data) {
    const response = await api.put(`/instructor/courses/${id}`, data)
    return response.data
  },

  async deleteCourse(id) {
    const response = await api.delete(`/instructor/courses/${id}`)
    return response.data
  },

  async getInstructorCourses() {
    const response = await api.get('/instructor/courses')
    return response.data
  }
}

export const enrollmentService = {
  async enrollInCourse(courseId) {
    const response = await api.post(`/student/enroll/${courseId}`)
    return response.data
  },

  async getMyEnrollments() {
    const response = await api.get('/student/enrollments')
    return response.data
  },

  async getEnrollmentProgress(enrollmentId) {
    const response = await api.get(`/student/enrollments/${enrollmentId}/progress`)
    return response.data
  },

  async getCourseEnrollments(courseId) {
    const response = await api.get(`/instructor/courses/${courseId}/enrollments`)
    return response.data
  }
}

export const lessonService = {
  async getLessonsByCourse(courseId) {
    const response = await api.get(`/instructor/courses/${courseId}/lessons`)
    return response.data
  },

  async createLesson(courseId, data) {
    const response = await api.post(`/instructor/courses/${courseId}/lessons`, data)
    return response.data
  },

  async updateLesson(lessonId, data) {
    const response = await api.put(`/instructor/lessons/${lessonId}`, data)
    return response.data
  },

  async addLesson(courseId, data) {
    const response = await api.post(`/instructor/courses/${courseId}/lessons`, data)
    return response.data
  },

  async deleteLesson(lessonId) {
    const response = await api.delete(`/instructor/lessons/${lessonId}`)
    return response.data
  },

  async markLessonComplete(lessonId) {
    const response = await api.post(`/student/lessons/${lessonId}/complete`)
    return response.data
  }
}
