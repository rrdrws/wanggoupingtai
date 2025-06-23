<template>
  <div id="app-layout">
    <TheHeader />
    <main class="main-content">
      <router-view/>
    </main>
    <TheFooter />
  </div>
</template>

<script>
import TheHeader from '@/components/TheHeader.vue';
import TheFooter from '@/components/TheFooter.vue';
import { useCartStore } from '@/stores/cart'; // Keep cart store logic if still needed globally
import { onMounted } from 'vue';


export default {
  name: 'App',
  components: {
    TheHeader,
    TheFooter
  },
  setup() {
    const cartStore = useCartStore();

    // Load cart from localStorage when app initializes
    // This is important for cart persistence across sessions
    onMounted(() => {
      cartStore.loadCart();
    });

    // No need to return cartStore here unless App template directly uses it,
    // TheHeader now handles cart display.
    return {};
  }
}
</script>

<style>
/* Global Styles */
body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  line-height: 1.6;
  background-color: #f4f4f4; /* A slightly off-white background for the body */
  color: #333;
}

#app-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh; /* Ensure footer is at the bottom */
}

.main-content {
  flex-grow: 1; /* Allows main content to expand and push footer down */
  padding-top: 20px; /* Add some padding below the fixed/sticky header */
  padding-bottom: 20px;
  /* Container for router-view content can be added here or in individual views */
}

/* General container class for views if needed */
.view-container-global {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 15px; /* Consistent padding for views */
}


/* Basic button styling (can be expanded or moved to a global CSS file) */
.btn {
  display: inline-block;
  padding: 10px 15px;
  margin: 5px;
  border: 1px solid transparent;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
  font-size: 1em;
  text-align: center;
  transition: background-color 0.2s ease, color 0.2s ease, border-color 0.2s ease;
}

.btn-primary {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}
.btn-primary:hover {
  background-color: #0056b3;
  border-color: #0056b3;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
  border-color: #6c757d;
}
.btn-secondary:hover {
  background-color: #545b62;
  border-color: #545b62;
}

.btn-danger {
  background-color: #dc3545;
  color: white;
  border-color: #dc3545;
}
.btn-danger:hover {
  background-color: #b02a37;
  border-color: #b02a37;
}

.btn-success {
    background-color: #28a745;
    color: white;
    border-color: #28a745;
}
.btn-success:hover {
    background-color: #1e7e34;
    border-color: #1e7e34;
}

.btn-warning {
    background-color: #ffc107;
    color: #212529;
    border-color: #ffc107;
}
.btn-warning:hover {
    background-color: #d39e00;
    border-color: #d39e00;
}


/* Remove default text-align center from old #app or #app-container */
/* #app-container, #app { text-align: left; } Replaced by #app-layout */

/* Headings */
h1, h2, h3, h4, h5, h6 {
  color: #333;
  margin-top: 0.5em;
  margin-bottom: 0.5em;
}

a {
  color: #007bff;
  text-decoration: none;
}
a:hover {
  text-decoration: underline;
}

/* Form elements basic styling */
input[type="text"],
input[type="email"],
input[type="password"],
input[type="search"],
input[type="number"],
textarea,
select {
  padding: 10px;
  margin-bottom: 10px; /* Spacing for forms */
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1em;
  box-sizing: border-box; /* Important for width calculations */
}
input[type="text"]:focus,
input[type="email"]:focus,
input[type="password"]:focus,
input[type="search"]:focus,
input[type="number"]:focus,
textarea:focus,
select:focus {
  border-color: #007bff;
  box-shadow: 0 0 0 0.2rem rgba(0,123,255,.25);
  outline: none;
}

/* Specific styling for view content containers if needed, replacing old individual paddings */
/* .view-container in individual components can still be used for specific layout needs */
/* For example, ProductList.vue has .view-container */
/* div[class*="view-"], div[class*="-list"], ... can be removed if .view-container-global is used or views manage their own padding within main-content */

</style>
