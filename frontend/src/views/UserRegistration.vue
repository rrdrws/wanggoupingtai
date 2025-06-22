<template>
  <div class="user-registration view-container">
    <h1>Register</h1>
    <form @submit.prevent="handleRegister" class="registration-form">
      <div class="form-group">
        <label for="username">Username:</label>
        <input type="text" id="username" v.model="form.username" required />
      </div>
      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" v.model="form.email" required />
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" id="password" v.model="form.password" required />
      </div>
      <div class="form-group">
        <label for="confirmPassword">Confirm Password:</label>
        <input type="password" id="confirmPassword" v.model="form.confirmPassword" required />
      </div>
      <!-- Add more fields like firstName, lastName if desired -->
      <button type="submit" :disabled="loading" class="btn btn-primary">
        {{ loading ? 'Registering...' : 'Register' }}
      </button>
    </form>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
  </div>
</template>

<script>
import ApiService from '@/services/ApiService';

export default {
  name: 'UserRegistration',
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
        this.errorMessage = 'Passwords do not match!';
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
        this.successMessage = `Registration successful for ${response.data.username}! Please login.`;
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
             this.errorMessage = 'Username or email already exists.';
          } else if (typeof err.response.data === 'string') {
            this.errorMessage = err.response.data;
          } else {
            this.errorMessage = 'Registration failed. Please check your input and try again.';
          }
        } else {
          this.errorMessage = 'Registration failed. An unexpected error occurred.';
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
.registration-form .form-group {
  margin-bottom: 15px;
}
.registration-form label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}
.registration-form input {
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
