<template>
  <div class="user-login view-container">
    <h1>Login</h1>
    <form @submit.prevent="handleLogin" class="login-form">
      <div class="form-group">
        <label for="username">Username:</label>
        <input type="text" id="username" v.model="credentials.username" required />
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" id="password" v.model="credentials.password" required />
      </div>
      <button type="submit" :disabled="loading" class="btn btn-primary">
        {{ loading ? 'Logging in...' : 'Login' }}
      </button>
    </form>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
  </div>
</template>

<script>
import ApiService from '@/services/ApiService';

export default {
  name: 'UserLogin',
  data() {
    return {
      credentials: {
        username: '',
        password: ''
      },
      loading: false,
      errorMessage: '',
      successMessage: ''
    };
  },
  methods: {
    async handleLogin() {
      this.errorMessage = '';
      this.successMessage = '';
      this.loading = true;

      try {
        const response = await ApiService.loginUser(this.credentials);
        // The backend currently returns a simple string "Login successful for user: username"
        // In a real app, it would return a user object and a JWT token.
        console.log('Login API Response:', response.data);

        // Simulate storing a token and user info
        // Backend now returns { id, username, message (or token) }
        const { id, username, message } = response.data;

        localStorage.setItem('userToken', message); // Store message as token (placeholder)
        localStorage.setItem('loggedInUser', username);
        if (id) {
          localStorage.setItem('userId', id.toString());
        } else {
          // Should not happen if login is successful and backend sends ID
          localStorage.removeItem('userId');
          console.warn("User ID not received from backend on login.");
        }

        this.successMessage = `Login successful! Welcome ${username}.`;

        // TODO: Update global state (e.g., using Pinia/Vuex) to reflect login status
        // Example: this.$store.dispatch('auth/login', { user: { id, username }, token: message });
        // this.$store.dispatch('auth/login', { user: response.data.user, token: response.data.token });


        // Redirect to home or a protected page after a short delay
        setTimeout(() => {
          this.$router.push('/');
        }, 1500);

      } catch (err) {
        console.error('Error logging in:', err);
        if (err.response) {
          if (err.response.status === 401) {
            this.errorMessage = 'Invalid username or password.';
          } else if (err.response.data && typeof err.response.data === 'string') {
            this.errorMessage = err.response.data;
          } else {
            this.errorMessage = 'Login failed. Please try again.';
          }
        } else {
          this.errorMessage = 'Login failed. An unexpected error occurred or server is unavailable.';
        }
      } finally {
        this.loading = false;
      }
    }
  }
}
</script>

<style scoped>
.view-container {
  padding: 20px;
  max-width: 400px;
  margin: 40px auto;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}
.login-form .form-group {
  margin-bottom: 15px;
}
.login-form label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}
.login-form input {
  width: calc(100% - 20px);
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.btn-primary {
  background-color: #007bff;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  width: 100%;
  font-size: 1em;
}
.btn-primary:disabled {
  background-color: #aaa;
}
.btn-primary:hover:not(:disabled) {
  background-color: #0056b3;
}
.error-message {
  color: red;
  margin-top: 10px;
  text-align: center;
}
.success-message {
  color: green;
  margin-top: 10px;
  text-align: center;
}
</style>
