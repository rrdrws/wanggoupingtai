<template>
  <div class="user-login-view view-container-global">
    <div class="login-panel">
      <h1>Login to Your Account</h1>
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="username">Username or Email</label>
          <input type="text" id="username" v.model="credentials.username" required placeholder="Enter your username or email" />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" id="password" v.model="credentials.password" required placeholder="Enter your password" />
        </div>
        <button type="submit" :disabled="loading" class="btn btn-primary btn-login">
          {{ loading ? 'Logging in...' : 'Login' }}
        </button>
      </form>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
      <div class="additional-links">
        <router-link to="/register">Don't have an account? Register here</router-link>
        <!-- <router-link to="/forgot-password">Forgot password?</router-link> -->
      </div>
    </div>
  </div>
</template>

<script>
import ApiService from '@/services/ApiService';

export default {
  name: 'UserLoginView', // Renamed component name for consistency
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
.user-login-view {
  /* Uses .view-container-global for max-width and horizontal padding */
  display: flex;
  justify-content: center;
  align-items: flex-start; /* Align panel to top */
  padding-top: 40px; /* Add some space from header */
  padding-bottom: 40px;
}

.login-panel {
  background-color: #fff;
  padding: 30px 40px; /* Increased padding */
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 450px; /* Slightly wider panel */
  text-align: center; /* Center heading and links */
}

.login-panel h1 {
  margin-bottom: 25px;
  font-size: 1.8em; /* Larger heading */
  color: #333;
}

.login-form .form-group {
  margin-bottom: 20px; /* Increased spacing */
  text-align: left; /* Align labels and inputs to left */
}

.login-form label {
  display: block;
  margin-bottom: 8px; /* Increased space below label */
  font-weight: 600; /* Slightly bolder label */
  color: #555;
  font-size: 0.95em;
}

.login-form input {
  width: 100%; /* Inputs take full width of form-group */
  padding: 12px; /* Increased padding in inputs */
  /* Global input styles from App.vue will apply for border, radius, focus */
}

.btn-login { /* Specific class for login button for easier targeting */
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
  display: block; /* Each link on new line */
  margin-top: 8px;
}

.additional-links a:hover {
  text-decoration: underline;
}
</style>
