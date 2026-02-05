<template>
  <div class="course-categories">
    <div class="page-header">
      <h1>Course Categories</h1>
      <div class="header-actions">
        <button class="btn btn-primary">
          <i class="fas fa-plus"></i> Add Category
        </button>
      </div>
    </div>

    <div class="categories-grid">
      <div class="category-card">
        <div class="category-icon">
          <i class="fas fa-code"></i>
        </div>
        <div class="category-info">
          <h3>Programming</h3>
          <p>0 courses</p>
        </div>
        <div class="category-actions">
          <button class="btn-icon"><i class="fas fa-edit"></i></button>
          <button class="btn-icon"><i class="fas fa-trash"></i></button>
        </div>
      </div>

      <div class="category-card">
        <div class="category-icon">
          <i class="fas fa-palette"></i>
        </div>
        <div class="category-info">
          <h3>Design</h3>
          <p>0 courses</p>
        </div>
        <div class="category-actions">
          <button class="btn-icon"><i class="fas fa-edit"></i></button>
          <button class="btn-icon"><i class="fas fa-trash"></i></button>
        </div>
      </div>

      <div class="category-card">
        <div class="category-icon">
          <i class="fas fa-briefcase"></i>
        </div>
        <div class="category-info">
          <h3>Business</h3>
          <p>0 courses</p>
        </div>
        <div class="category-actions">
          <button class="btn-icon"><i class="fas fa-edit"></i></button>
          <button class="btn-icon"><i class="fas fa-trash"></i></button>
        </div>
      </div>

      <div class="category-card">
        <div class="category-icon">
          <i class="fas fa-chart-line"></i>
        </div>
        <div class="category-info">
          <h3>Data Science</h3>
          <p>0 courses</p>
        </div>
        <div class="category-actions">
          <button class="btn-icon"><i class="fas fa-edit"></i></button>
          <button class="btn-icon"><i class="fas fa-trash"></i></button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Placeholder for category management functionality
</script>

<style scoped>
.course-categories {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-primary {
  background: #4CAF50;
  color: white;
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.category-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
  gap: 15px;
}

.category-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
}

.category-info {
  flex: 1;
}

.category-info h3 {
  margin: 0 0 5px 0;
  font-size: 18px;
}

.category-info p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.category-actions {
  display: flex;
  gap: 10px;
}

.btn-icon {
  width: 36px;
  height: 36px;
  border: none;
  background: #f5f5f5;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}

.btn-icon:hover {
  background: #e0e0e0;
}
</style>
