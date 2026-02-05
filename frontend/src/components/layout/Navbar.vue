<template>
  <nav class="navbar">
    <div class="nav-container">
      <div class="nav-left">
        <router-link to="/" class="nav-brand">
          <svg width="32" height="32" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
            <defs>
              <linearGradient id="capGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" style="stop-color:#5B6FE3;stop-opacity:1" />
                <stop offset="100%" style="stop-color:#FB7D5B;stop-opacity:1" />
              </linearGradient>
            </defs>
            <path d="M16 2L2 10v3h28v-3L16 2z" fill="url(#capGradient)"/>
            <rect x="14" y="13" width="4" height="17" fill="#5B6FE3"/>
            <path d="M8 16h16v2H8z" fill="#FCC43E"/>
          </svg>
        </router-link>
      </div>

      <div class="nav-center">
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
        <div class="success-icon">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
            <circle cx="12" cy="12" r="10" stroke="#27ae60" stroke-width="2" fill="none"/>
            <path d="M8 12l3 3 5-6" stroke="#27ae60" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
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
const emit = defineEmits(['open-login', 'open-register', 'toggle-sidebar'])

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
  background: #ffffff;
  border-bottom: 1px solid #e5e7eb;
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
}

.nav-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 2rem;
  display: grid;
  grid-template-columns: 1fr 2fr 1fr;
  align-items: center;
  gap: 20px;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.hamburger-btn {
  background: none;
  border: none;
  padding: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  transition: background 0.3s ease;
}

.hamburger-btn:hover {
  background: #f3f4f6;
}

.nav-brand {
  font-size: 24px;
  font-weight: 700;
  color: #1f2937;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: color 0.3s ease;
}

.nav-brand:hover {
  color: #5B6FE3;
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
  color: #6b7280;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link:hover {
  color: #5B6FE3;
}

.nav-link.router-link-active {
  color: #5B6FE3;
}

.nav-link.router-link-active::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  right: 0;
  height: 2px;
  background: #5B6FE3;
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
  background: #5B6FE3;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  color: white;
  font-size: 14px;
  box-shadow: 0 2px 8px rgba(91, 111, 227, 0.3);
  cursor: pointer;
  transition: all 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(91, 111, 227, 0.4);
}

.user-name {
  font-weight: 500;
  color: #1f2937;
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
  background: #ffffff;
  border-radius: 16px;
  padding: 30px;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  border: 1px solid #e5e7eb;
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
  color: #1f2937;
  font-size: 24px;
  margin: 0 0 20px 0;
}

.modal-body {
  margin-bottom: 25px;
}

.modal-body p {
  color: #6b7280;
  font-size: 16px;
  line-height: 1.6;
  margin: 0;
}

.modal-body strong {
  color: #5B6FE3;
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
  background: #ef4444;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-danger:hover {
  background: #dc2626;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.3);
}

.success-modal {
  text-align: center;
}

.success-icon {
  width: 80px;
  height: 80px;
  background: #10b981;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48px;
  color: white;
  margin: 0 auto 20px;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
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
  color: #1f2937;
  font-size: 24px;
  margin: 0 0 10px 0;
}

.success-modal p {
  color: #6b7280;
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
  color: #6b7280;
  font-size: 32px;
  cursor: pointer;
  line-height: 1;
  padding: 0;
  width: 30px;
  height: 30px;
  transition: color 0.3s ease;
}

.close-btn:hover {
  color: #5B6FE3;
}

.profile-avatar-large {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: #5B6FE3;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  color: white;
  font-size: 36px;
  box-shadow: 0 4px 12px rgba(91, 111, 227, 0.3);
  margin: 0 auto 30px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  color: #374151;
  font-weight: 500;
  margin-bottom: 8px;
  font-size: 14px;
}

.form-control {
  width: 100%;
  padding: 12px;
  background: #f9fafb;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  color: #1f2937;
  font-size: 14px;
  transition: all 0.3s ease;
}

.form-control:focus {
  outline: none;
  border-color: #5B6FE3;
  box-shadow: 0 0 0 3px rgba(91, 111, 227, 0.1);
}

.form-control:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.form-group small {
  display: block;
  margin-top: 5px;
  font-size: 12px;
  color: #6b7280;
}

/* Mobile Responsive */
@media (max-width: 768px) {
  .nav-container {
    padding: 0 15px;
  }
  
  .user-name {
    display: none;
  }
  
  .btn {
    padding: 8px 12px;
    font-size: 14px;
  }
}
</style>
