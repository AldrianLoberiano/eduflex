<template>
  <nav class="navbar">
    <div class="nav-container">
      <div class="nav-left">
        <router-link to="/" class="nav-brand">
          EduFlex
        </router-link>
      </div>

      <div class="nav-center">
        <router-link to="/" class="nav-link">Home</router-link>
        <router-link to="/courses" class="nav-link">Courses</router-link>
        
        <template v-if="authStore.isAuthenticated">
          <router-link v-if="authStore.isStudent" to="/my-learning" class="nav-link">
            My Learning
          </router-link>
          <router-link v-if="authStore.isInstructor" to="/instructor/dashboard" class="nav-link">
            Dashboard
          </router-link>
          <router-link v-if="authStore.isInstructor" to="/instructor/courses" class="nav-link">
            My Courses
          </router-link>
          <router-link v-if="authStore.isAdmin" to="/admin/dashboard" class="nav-link">
            Dashboard
          </router-link>
        </template>
      </div>

      <div class="nav-right">
        <template v-if="authStore.isAuthenticated">
          <div class="nav-user">
            <div class="user-avatar" @click="showProfileModal = true" title="Edit Profile">
              {{ getInitials(authStore.user?.firstName, authStore.user?.lastName) }}
            </div>
            <span class="user-name">Hi, {{ authStore.user?.firstName }}</span>
            <button @click="showLogoutModal = true" class="btn btn-secondary">Logout</button>
          </div>
        </template>

        <template v-else>
          <button @click="emit('open-login')" class="btn btn-secondary">Login</button>
          <button @click="emit('open-register')" class="btn btn-primary">Sign Up</button>
        </template>
      </div>
    </div>

    <!-- Logout Confirmation Modal -->
    <div v-if="showLogoutModal" class="modal-overlay" @click="showLogoutModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Confirm Logout</h3>
        </div>
        <div class="modal-body">
          <p>Are you sure you want to logout, <strong>{{ authStore.user?.firstName }}</strong>?</p>
        </div>
        <div class="modal-actions">
          <button @click="showLogoutModal = false" class="btn btn-secondary">Cancel</button>
          <button @click="confirmLogout" class="btn btn-danger">Logout</button>
        </div>
      </div>
    </div>

    <!-- Success Message Modal -->
    <div v-if="showSuccessModal" class="modal-overlay" @click="showSuccessModal = false">
      <div class="modal-content success-modal" @click.stop>
        <div class="success-icon">✓</div>
        <h3>Logged Out Successfully</h3>
        <p>See you soon!</p>
        <button @click="showSuccessModal = false" class="btn btn-primary">Close</button>
      </div>
    </div>

    <!-- Profile Edit Modal -->
    <div v-if="showProfileModal" class="modal-overlay" @click="showProfileModal = false">
      <div class="modal-content profile-modal" @click.stop>
        <div class="modal-header">
          <h3>Edit Profile</h3>
          <button @click="showProfileModal = false" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <div class="profile-avatar-large">
            {{ getInitials(authStore.user?.firstName, authStore.user?.lastName) }}
          </div>
          
          <div class="form-group">
            <label>First Name</label>
            <input v-model="profileForm.firstName" type="text" class="form-control" placeholder="First Name">
          </div>
          
          <div class="form-group">
            <label>Last Name</label>
            <input v-model="profileForm.lastName" type="text" class="form-control" placeholder="Last Name">
          </div>
          
          <div class="form-group">
            <label>Email</label>
            <input v-model="profileForm.email" type="email" class="form-control" placeholder="Email" disabled>
            <small class="text-muted">Email cannot be changed</small>
          </div>
          
          <div class="form-group">
            <label>Role</label>
            <input :value="authStore.user?.role" type="text" class="form-control" disabled>
          </div>
        </div>
        <div class="modal-actions">
          <button @click="showProfileModal = false" class="btn btn-secondary">Cancel</button>
          <button @click="saveProfile" class="btn btn-primary">Save Changes</button>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const emit = defineEmits(['open-login', 'open-register'])

const showLogoutModal = ref(false)
const showSuccessModal = ref(false)
const showProfileModal = ref(false)
const profileForm = ref({
  firstName: '',
  lastName: '',
  email: ''
})

// Watch for profile modal opening to populate form
watch(showProfileModal, (newVal) => {
  if (newVal && authStore.user) {
    profileForm.value = {
      firstName: authStore.user.firstName || '',
      lastName: authStore.user.lastName || '',
      email: authStore.user.email || ''
    }
  }
})

function getInitials(firstName, lastName) {
  const first = firstName?.charAt(0)?.toUpperCase() || ''
  const last = lastName?.charAt(0)?.toUpperCase() || ''
  return first + last
}

function confirmLogout() {
  showLogoutModal.value = false
  authStore.logout()
  router.push('/')
  
  // Show success message
  setTimeout(() => {
    showSuccessModal.value = true
  }, 200)
}

function saveProfile() {
  // Here you would typically make an API call to update the profile
  // For now, we'll just update the local store
  if (authStore.user) {
    authStore.user.firstName = profileForm.value.firstName
    authStore.user.lastName = profileForm.value.lastName
    
    // Save to localStorage to persist
    const currentUser = JSON.parse(localStorage.getItem('user') || '{}')
    currentUser.firstName = profileForm.value.firstName
    currentUser.lastName = profileForm.value.lastName
    localStorage.setItem('user', JSON.stringify(currentUser))
    
    showProfileModal.value = false
    
    // Show success notification (you could add a toast notification here)
    alert('Profile updated successfully!')
  }
}
</script>

<style scoped>
.navbar {
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 100%);
  box-shadow: 0 4px 20px rgba(0, 212, 255, 0.1);
  border-bottom: 1px solid rgba(0, 212, 255, 0.2);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 900;
  backdrop-filter: blur(10px);
}

.nav-container {
  max-width: 1600px;
  margin: 0 auto;
  padding: 15px 20px;
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  gap: 20px;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 15px;
}


.nav-brand {
  font-size: 24px;
  font-weight: 700;
  background: linear-gradient(135deg, #00d4ff 0%, #0099ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 8px;
  filter: drop-shadow(0 0 10px rgba(0, 212, 255, 0.3));
}

.brand-icon {
  font-size: 28px;
}

.nav-center {
  display: flex;
  align-items: center;
  gap: 20px;
  justify-content: center;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 15px;
  justify-content: flex-end;
}


.nav-link {
  color: #e0e0e0;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link:hover {
  color: #00d4ff;
  text-shadow: 0 0 10px rgba(0, 212, 255, 0.5);
}

.nav-link.router-link-active {
  color: #00d4ff;
}

.nav-link.router-link-active::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, #00d4ff, transparent);
  box-shadow: 0 0 10px rgba(0, 212, 255, 0.5);
}

.nav-user {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: 5px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #00d4ff 0%, #0099ff 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  color: white;
  font-size: 14px;
  box-shadow: 0 2px 10px rgba(0, 212, 255, 0.3);
  cursor: pointer;
  transition: all 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 20px rgba(0, 212, 255, 0.5);
}

.user-name {
  font-weight: 500;
  color: #00d4ff;
  text-shadow: 0 0 10px rgba(0, 212, 255, 0.3);
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  backdrop-filter: blur(5px);
  padding: 20px;
}

.modal-content {
  background: linear-gradient(135deg, #1a1a1a 0%, #2a2a2a 100%);
  border-radius: 16px;
  padding: 30px;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 20px 60px rgba(0, 212, 255, 0.3);
  border: 1px solid rgba(0, 212, 255, 0.3);
  animation: modalSlideIn 0.3s ease-out;
  margin: auto;
  position: relative;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header h3 {
  color: #fff;
  font-size: 24px;
  margin: 0 0 20px 0;
}

.modal-body {
  margin-bottom: 25px;
}

.modal-body p {
  color: #e0e0e0;
  font-size: 16px;
  line-height: 1.6;
  margin: 0;
}

.modal-body strong {
  color: #00d4ff;
}

.modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.modal-actions .btn {
  flex: 1;
}

.btn-danger {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-danger:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(239, 68, 68, 0.4);
}

.success-modal {
  text-align: center;
}

.success-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48px;
  color: white;
  margin: 0 auto 20px;
  box-shadow: 0 10px 30px rgba(16, 185, 129, 0.4);
  animation: successPulse 0.6s ease-out;
}

@keyframes successPulse {
  0% {
    transform: scale(0);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.success-modal h3 {
  color: #fff;
  font-size: 24px;
  margin: 0 0 10px 0;
}

.success-modal p {
  color: #888;
  font-size: 16px;
  margin: 0 0 25px 0;
}

.success-modal .btn {
  width: 100%;
}

/* Profile Modal */
.profile-modal {
  max-width: 500px;
}

.profile-modal .modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.close-btn {
  background: none;
  border: none;
  color: #888;
  font-size: 32px;
  cursor: pointer;
  line-height: 1;
  padding: 0;
  width: 30px;
  height: 30px;
  transition: color 0.3s ease;
}

.close-btn:hover {
  color: #00d4ff;
}

.profile-avatar-large {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #00d4ff 0%, #0099ff 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  color: white;
  font-size: 36px;
  box-shadow: 0 4px 20px rgba(0, 212, 255, 0.4);
  margin: 0 auto 30px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  color: #e0e0e0;
  font-weight: 500;
  margin-bottom: 8px;
  font-size: 14px;
}

.form-control {
  width: 100%;
  padding: 12px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 8px;
  color: #fff;
  font-size: 14px;
  transition: all 0.3s ease;
}

.form-control:focus {
  outline: none;
  border-color: #00d4ff;
  box-shadow: 0 0 0 3px rgba(0, 212, 255, 0.1);
}

.form-control:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.form-group small {
  display: block;
  margin-top: 5px;
  font-size: 12px;
}
</style>
