<template>
  <div id="app">
    <Sidebar 
      v-if="currentRoute !== '/'"
      @open-login="showLogin = true" 
      @open-register="showRegister = true"
    />
    <div class="app-main" :class="{ 'no-sidebar': currentRoute === '/' }">
      <Navbar 
        @open-login="showLogin = true" 
        @open-register="showRegister = true"
      />
      <main class="main-content">
        <router-view @open-register="showRegister = true" />
      </main>
    </div>
    
    <!-- Modals -->
    <LoginModal 
      :isOpen="showLogin" 
      @close="showLogin = false"
      @switch-to-register="showLogin = false; showRegister = true"
    />
    <RegisterModal 
      :isOpen="showRegister" 
      @close="showRegister = false"
      @switch-to-login="showRegister = false; showLogin = true"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import Sidebar from './components/layout/Sidebar.vue'
import Navbar from './components/layout/Navbar.vue'
import LoginModal from './components/LoginModal.vue'
import RegisterModal from './components/RegisterModal.vue'

const showLogin = ref(false)
const showRegister = ref(false)
const route = useRoute()
const currentRoute = computed(() => route.path)
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  min-height: 100vh;
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 50%, #0a0a0a 100%);
  font-size: 20px;
  color: #e0e0e0;
  font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.app-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  width: 100%;
  margin-left: 260px;
  transition: margin-left 0.3s ease;
}

.app-main.no-sidebar {
  margin-left: 0;
}

.main-content {
  flex: 1;
  padding: 80px 70px;
  max-width: 1800px;
  margin: 60px auto 0;
  width: 100%;
  box-sizing: border-box;
}

@media (max-width: 767px) {
  .app-main {
    margin-left: 0;
  }
  
  .main-content {
    padding: 20px 15px;
    margin-top: 80px;
  }
}

/* Card styling */
:deep(.card) {
  background: linear-gradient(135deg, #1a1a1a 0%, #2a2a2a 100%);
  border-radius: 16px;
  border: 1px solid rgba(0, 212, 255, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.5);
  transition: all 0.3s ease;
}

:deep(.card:hover) {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 212, 255, 0.25);
  border-color: rgba(0, 212, 255, 0.5);
}

/* Button styling */
:deep(button) {
  border-radius: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
}

:deep(button:hover) {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(99, 102, 241, 0.3);
}

/* Input styling */
:deep(input), :deep(select), :deep(textarea) {
  background: #1a1a1a;
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 10px;
  color: #e0e0e0;
  padding: 12px 16px;
  transition: all 0.3s ease;
}

:deep(input:focus), :deep(select:focus), :deep(textarea:focus) {
  outline: none;
  border-color: #00d4ff;
  box-shadow: 0 0 0 3px rgba(0, 212, 255, 0.2);
  background: #0a0a0a;
}

/* Responsive padding */
@media (min-width: 768px) and (max-width: 1023px) {
  .main-content {
    padding: 60px 40px;
  }
}

@media (min-width: 1024px) {
  .main-content {
    padding: 90px 70px;
  }
}

@media (max-width: 767px) {
  #app {
    font-size: 17px;
  }
  
  .main-content {
    padding: 44px 28px;
  }
}
</style>
