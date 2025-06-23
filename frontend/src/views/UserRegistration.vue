<template>
  <div class="user-registration-view view-container-global">
    <div class="registration-panel">
      <h1>创建您的账户</h1>
      <form @submit.prevent="handleRegister" class="registration-form">
        <div class="form-group">
          <label for="username">用户名</label>
          <input type="text" id="username" v.model="form.username" required placeholder="请选择一个用户名" />
        </div>
        <div class="form-group">
          <label for="email">邮箱地址</label>
          <input type="email" id="email" v.model="form.email" required placeholder="请输入您的邮箱" />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input type="password" id="password" v.model="form.password" required placeholder="请创建一个密码" />
        </div>
        <div class="form-group">
          <label for="confirmPassword">确认密码</label>
          <input type="password" id="confirmPassword" v.model="form.confirmPassword" required placeholder="请确认您的密码" />
        </div>
        <button type="submit" :disabled="loading" class="btn btn-primary btn-register">
          {{ loading ? '注册中...' : '创建账户' }}
        </button>
      </form>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p> <!-- Error messages translated in script -->
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p> <!-- Success messages translated in script -->
      <div class="additional-links">
        <router-link to="/login">已有账户？在此登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import ApiService from '@/services/ApiService';

export default {
  name: 'UserRegistrationView', // Renamed component name for consistency
  data() {
    return {
      form: {
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        // Optional: Add firstName, lastName, etc.
        // firstName: '',
        // lastName: ''
      },
      loading: false,
      errorMessage: '',
      successMessage: ''
    };
  },
  methods: {
    async handleRegister() {
      this.errorMessage = '';
      this.successMessage = '';
      if (this.form.password !== this.form.confirmPassword) {
        this.errorMessage = '两次输入的密码不匹配！'; // Passwords do not match!
        return;
      }

      this.loading = true;
      try {
        // Prepare user data, excluding confirmPassword
        const userData = {
          username: this.form.username,
          email: this.form.email,
          password: this.form.password,
          // Include other fields if they are part of your User entity and registration process
          // firstName: this.form.firstName,
          // lastName: this.form.lastName,
        };

        const response = await ApiService.registerUser(userData);
        this.successMessage = `用户 ${response.data.username} 注册成功！请登录。`; // Registration successful for ${response.data.username}! Please login.
        // Clear form and redirect to login after a short delay
        this.form = { username: '', email: '', password: '', confirmPassword: '' };
        setTimeout(() => {
          this.$router.push('/login');
        }, 2000);

      } catch (err) {
        console.error('Error registering user:', err);
        if (err.response && err.response.data) {
          // Assuming backend sends a message or specific error codes
          if (err.response.status === 409) { // Conflict
             this.errorMessage = '用户名或邮箱已存在。'; // Username or email already exists.
          } else if (typeof err.response.data === 'string') {
            this.errorMessage = err.response.data; // Use backend message
          } else {
            this.errorMessage = '注册失败，请检查您的输入并重试。'; // Registration failed. Please check your input and try again.
          }
        } else {
          this.errorMessage = '注册失败，发生意外错误。'; // Registration failed. An unexpected error occurred.
        }
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>

<style scoped>
.user-registration-view {
  /* Uses .view-container-global for max-width and horizontal padding */
  display: flex;
  justify-content: center;
  align-items: flex-start; /* Align panel to top */
  padding-top: 40px; /* Add some space from header */
  padding-bottom: 40px;
}

.registration-panel {
  background-color: #fff;
  padding: 30px 40px; /* Increased padding */
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 480px; /* Slightly wider for registration form */
  text-align: center; /* Center heading and links */
}

.registration-panel h1 {
  margin-bottom: 25px;
  font-size: 1.8em; /* Larger heading */
  color: #333;
}

.registration-form .form-group {
  margin-bottom: 20px; /* Increased spacing */
  text-align: left; /* Align labels and inputs to left */
}

.registration-form label {
  display: block;
  margin-bottom: 8px; /* Increased space below label */
  font-weight: 600; /* Slightly bolder label */
  color: #555;
  font-size: 0.95em;
}

.registration-form input {
  width: 100%; /* Inputs take full width of form-group */
  padding: 12px; /* Increased padding in inputs */
  /* Global input styles from App.vue will apply for border, radius, focus */
}

.btn-register { /* Specific class for register button */
  width: 100%;
  padding: 12px; /* Larger button padding */
  font-size: 1.1em; /* Larger button text */
  margin-top: 10px; /* Space above button */
  /* Uses global .btn and .btn-primary styles */
}

.error-message {
  color: #d9534f; /* Bootstrap danger red */
  margin-top: 15px;
  font-size: 0.9em;
}

.success-message {
  color: #28a745; /* Bootstrap success green */
  margin-top: 15px;
  font-size: 0.9em;
}

.additional-links {
  margin-top: 25px;
  font-size: 0.9em;
}

.additional-links a {
  color: #007bff;
  text-decoration: none;
  /* display: block; */ /* Can be inline or block depending on preference */
}

.additional-links a:hover {
  text-decoration: underline;
}
</style>
