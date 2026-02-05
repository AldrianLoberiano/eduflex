import { createRouter, createWebHistory } from 'vue-router'
import { authService } from '../services'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Courses.vue')
  },
  {
    path: '/courses',
    name: 'Courses',
    component: () => import('../views/Courses.vue')
  },
  {
    path: '/courses/:id',
    name: 'CourseDetail',
    component: () => import('../views/admin/CourseDetail.vue')
  },
  {
    path: '/admin/courses/:id',
    name: 'AdminCourseDetail',
    component: () => import('../views/admin/CourseDetail.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/instructor',
    redirect: '/instructor/dashboard'
  },
  {
    path: '/instructor/dashboard',
    name: 'InstructorDashboard',
    component: () => import('../views/instructor/Dashboard.vue'),
    meta: { requiresAuth: true, role: 'INSTRUCTOR' }
  },
  {
    path: '/instructor/courses',
    name: 'InstructorCourses',
    component: () => import('../views/instructor/MyCourses.vue'),
    meta: { requiresAuth: true, role: 'INSTRUCTOR' }
  },
  {
    path: '/instructor/courses/new',
    name: 'CreateCourse',
    component: () => import('../views/instructor/CreateCourse.vue'),
    meta: { requiresAuth: true, role: 'INSTRUCTOR' }
  },
  {
    path: '/instructor/courses/:id/edit',
    name: 'EditCourse',
    component: () => import('../views/instructor/EditCourse.vue'),
    meta: { requiresAuth: true, role: 'INSTRUCTOR' }
  },
  {
    path: '/instructor/courses/:id/students',
    name: 'CourseStudents',
    component: () => import('../views/instructor/CourseStudents.vue'),
    meta: { requiresAuth: true, role: 'INSTRUCTOR' }
  },
  {
    path: '/instructor/courses/:id/lessons',
    name: 'CourseLessons',
    component: () => import('../views/instructor/EditCourse.vue'),
    meta: { requiresAuth: true, role: 'INSTRUCTOR' }
  },
  {
    path: '/student/my-learning',
    name: 'MyLearning',
    component: () => import('../views/student/MyLearning.vue'),
    meta: { requiresAuth: true, role: 'STUDENT' }
  },
  {
    path: '/student',
    redirect: '/student/my-learning'
  },
  {
    path: '/my-learning',
    redirect: '/student/my-learning'
  },
  {
    path: '/admin',
    redirect: '/admin/dashboard'
  },
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: () => import('../views/admin/Dashboard.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/profile',
    name: 'AdminProfile',
    component: () => import('../views/admin/Profile.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // User Management
  {
    path: '/admin/users',
    name: 'UserManagement',
    component: () => import('../views/admin/UserManagement.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/users/approvals',
    name: 'UserApprovals',
    component: () => import('../views/admin/UserApprovals.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // Course Management
  {
    path: '/admin/courses',
    name: 'AdminCourses',
    component: () => import('../views/admin/CourseManagement.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/courses/categories',
    name: 'CourseCategories',
    component: () => import('../views/admin/CourseCategories.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // Module Management
  {
    path: '/admin/modules',
    name: 'ModuleManagement',
    component: () => import('../views/admin/ModuleManagement.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // Content Management
  {
    path: '/admin/content',
    name: 'ContentManagement',
    component: () => import('../views/admin/ContentManagement.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // Enrollment Management
  {
    path: '/admin/enrollments',
    name: 'EnrollmentManagement',
    component: () => import('../views/admin/EnrollmentManagement.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // Assessment & Grading
  {
    path: '/admin/assessments',
    name: 'AssessmentManagement',
    component: () => import('../views/admin/AssessmentManagement.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/grading',
    name: 'GradingManagement',
    component: () => import('../views/admin/GradingManagement.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // Attendance
  {
    path: '/admin/attendance',
    name: 'AttendanceManagement',
    component: () => import('../views/admin/AttendanceManagement.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // Communications
  {
    path: '/admin/communications',
    name: 'Communications',
    component: () => import('../views/admin/Communications.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/announcements',
    name: 'Announcements',
    component: () => import('../views/admin/Announcements.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // Reports & Analytics
  {
    path: '/admin/reports',
    name: 'Reports',
    component: () => import('../views/admin/Reports.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // Payments
  {
    path: '/admin/payments',
    name: 'Payments',
    component: () => import('../views/admin/Payments.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/admin/subscriptions',
    name: 'Subscriptions',
    component: () => import('../views/admin/Subscriptions.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // Content Moderation
  {
    path: '/admin/moderation',
    name: 'ContentModeration',
    component: () => import('../views/admin/ContentModeration.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  // System Settings
  {
    path: '/admin/settings',
    name: 'SystemSettings',
    component: () => import('../views/admin/SystemSettings.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' }
  },
  {
    path: '/courses/:courseId/quizzes',
    name: 'CourseQuizzes',
    component: () => import('../views/CourseQuizzes.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    redirect: '/'
  },
  {
    path: '/register',
    redirect: '/'
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

function getHomeRouteByRole(role) {
  if (role === 'ADMIN') return '/admin'
  if (role === 'INSTRUCTOR') return '/instructor'
  return '/student/my-learning'
}

// Navigation guard
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const isAuthenticated = authService.isAuthenticated()
  const user = authService.getCurrentUser()

  if (user?.role === 'ADMIN' && to.name === 'CourseDetail') {
    next(`/admin/courses/${to.params.id}`)
    return
  }

  if (requiresAuth && !isAuthenticated) {
    next('/')
  } else if (to.meta.role && user?.role !== to.meta.role) {
    if (user?.role === 'ADMIN') {
      next()
    } else if (user?.role) {
      next(getHomeRouteByRole(user.role))
    } else {
      next('/')
    }
  } else {
    next()
  }
})

export default router
