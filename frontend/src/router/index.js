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
