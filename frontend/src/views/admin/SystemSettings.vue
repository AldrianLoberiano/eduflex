<template>
  <div class="system-settings">
    <div class="page-header">
      <h1>System Settings</h1>
      <div class="header-actions">
        <button class="btn btn-primary">
          <i class="fas fa-save"></i> Save Changes
        </button>
      </div>
    </div>

    <div class="settings-grid">
      <div class="settings-card">
        <div class="card-header">
          <i class="fas fa-cog"></i>
          <h3>General Settings</h3>
        </div>
        <div class="card-body">
          <div class="form-group">
            <label>Platform Name</label>
            <input type="text" value="EduFlex" class="form-control" />
          </div>
          <div class="form-group">
            <label>Support Email</label>
            <input type="email" value="support@eduflex.com" class="form-control" />
          </div>
          <div class="form-group">
            <label>Time Zone</label>
            <select class="form-control">
              <option>UTC</option>
              <option>EST</option>
              <option>PST</option>
            </select>
          </div>
        </div>
      </div>

      <div class="settings-card">
        <div class="card-header">
          <i class="fas fa-envelope"></i>
          <h3>Email Configuration</h3>
        </div>
        <div class="card-body">
          <div class="form-group">
            <label>SMTP Server</label>
            <input type="text" placeholder="smtp.example.com" class="form-control" />
          </div>
          <div class="form-group">
            <label>SMTP Port</label>
            <input type="number" placeholder="587" class="form-control" />
          </div>
          <div class="form-group">
            <label>From Email</label>
            <input type="email" placeholder="noreply@eduflex.com" class="form-control" />
          </div>
        </div>
      </div>

      <div class="settings-card">
        <div class="card-header">
          <i class="fas fa-lock"></i>
          <h3>Security Settings</h3>
        </div>
        <div class="card-body">
          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" />
              Enable Two-Factor Authentication
            </label>
          </div>
          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" />
              Require Strong Passwords
            </label>
          </div>
          <div class="form-group">
            <label>Session Timeout (minutes)</label>
            <input type="number" value="30" class="form-control" />
          </div>
        </div>
      </div>

      <div class="settings-card">
        <div class="card-header">
          <i class="fas fa-credit-card"></i>
          <h3>Payment Gateway</h3>
        </div>
        <div class="card-body">
          <div class="form-group">
            <label>Payment Provider</label>
            <select class="form-control">
              <option>Stripe</option>
              <option>PayPal</option>
              <option>Square</option>
            </select>
          </div>
          <div class="form-group">
            <label>API Key</label>
            <input type="password" placeholder="Enter API key" class="form-control" />
          </div>
          <div class="form-group">
            <label>Currency</label>
            <select class="form-control">
              <option>USD</option>
              <option>EUR</option>
              <option>GBP</option>
            </select>
          </div>
        </div>
      </div>

      <div class="settings-card">
        <div class="card-header">
          <i class="fas fa-database"></i>
          <h3>Storage Settings</h3>
        </div>
        <div class="card-body">
          <div class="form-group">
            <label>Max File Upload Size (MB)</label>
            <input type="number" value="100" class="form-control" />
          </div>
          <div class="form-group">
            <label>Storage Provider</label>
            <select class="form-control">
              <option>Local</option>
              <option>AWS S3</option>
              <option>Azure Blob</option>
            </select>
          </div>
          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" />
              Enable CDN
            </label>
          </div>
        </div>
      </div>

      <div class="settings-card">
        <div class="card-header">
          <i class="fas fa-bell"></i>
          <h3>Notification Settings</h3>
        </div>
        <div class="card-body">
          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" checked />
              Email Notifications
            </label>
          </div>
          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" checked />
              Push Notifications
            </label>
          </div>
          <div class="form-group">
            <label class="checkbox-label">
              <input type="checkbox" />
              SMS Notifications
            </label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Placeholder for system settings functionality
</script>

<style scoped>
.system-settings {
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

.settings-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.settings-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  overflow: hidden;
}

.card-header {
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  gap: 10px;
}

.card-header i {
  font-size: 24px;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
}

.card-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.form-control {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-control:focus {
  outline: none;
  border-color: #4CAF50;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.checkbox-label input[type="checkbox"] {
  width: auto;
  cursor: pointer;
}
</style>
