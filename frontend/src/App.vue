<template>
  <div id="app-container"> <!-- Changed id to avoid conflict if #app is on body/html by Vue -->
    <nav>
      <router-link to="/">Home</router-link> |
      <router-link to="/products">Products</router-link> |
      <router-link to="/cart">Cart ({{ cartStore.itemCount }})</router-link> |
      <router-link to="/login">Login</router-link> |
      <router-link to="/register">Register</router-link> |
      <router-link to="/about">About</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<script>
import { useCartStore } from '@/stores/cart';
import { onMounted } from 'vue';

export default {
  name: 'App',
  setup() {
    const cartStore = useCartStore();

    // Load cart from localStorage when app initializes
    onMounted(() => {
      cartStore.loadCart();
    });

    return {
      cartStore
    };
  }
}
</script>

<style>
#app-container { /* Matches the template id */
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
  background-color: #f8f9fa; /* Light background for nav */
  border-bottom: 1px solid #dee2e6; /* Subtle border */
}

nav a {
  font-weight: bold;
  color: #2c3e50;
  text-decoration: none; /* Remove underline from links */
  padding: 0 10px; /* Add some spacing around links */
}

nav a.router-link-exact-active {
  color: #42b983; /* Vue green for active link */
}

/* Basic styling for view content */
div[class*="view-"], div[class*="-list"], div[class*="-detail"], div[class*="-login"], div[class*="-registration"], div[class*="-cart"], .home, .about {
  padding: 20px;
}
</style>
