<template>
  <header class="site-header">
    <div class="container">
      <div class="logo-area">
        <router-link to="/" class="logo">
          <!-- Placeholder for Logo -->
          <img src="@/assets/logo.png" alt="Site Logo" class="logo-img">
          <span>MyShop</span>
        </router-link>
      </div>

      <div class="search-area">
        <input type="search" v.model="searchQuery" placeholder="Search products..." @keyup.enter="performSearch">
        <button @click="performSearch" class="search-button">Search</button>
      </div>

      <nav class="navigation-area">
        <router-link to="/">Home</router-link>
        <router-link to="/products">All Products</router-link>
        <!-- <router-link to="/categories">Categories</router-link> Placeholder -->
        <router-link to="/publish-product">Publish Product</router-link> <!-- Link for new page -->
        <router-link to="/cart" class="cart-link">
          Cart
          <span v-if="cartStore.itemCount > 0" class="cart-count">({{ cartStore.itemCount }})</span>
        </router-link>

        <div v-if="isLoggedIn" class="user-actions">
          <span>Welcome, {{ username }}!</span>
          <button @click="logout" class="logout-button">Logout</button>
          <!-- <router-link to="/profile">My Profile</router-link> -->
        </div>
        <router-link v-else to="/login">Login/Register</router-link>
      </nav>
    </div>
  </header>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '@/stores/cart';

export default {
  name: 'TheHeader',
  setup() {
    const router = useRouter();
    const cartStore = useCartStore();
    const searchQuery = ref('');

    // Simplified login state check (replace with proper auth store later)
    const isLoggedIn = ref(false);
    const username = ref('');

    const checkLoginStatus = () => {
      const user = localStorage.getItem('loggedInUser');
      if (user) {
        isLoggedIn.value = true;
        username.value = user;
      } else {
        isLoggedIn.value = false;
        username.value = '';
      }
    };

    onMounted(() => {
      checkLoginStatus();
      // Listen for login/logout events if you emit them globally, or re-check periodically/on route change
      // For simplicity, we might need to call checkLoginStatus more often or use a proper auth store
      window.addEventListener('storage', checkLoginStatus); // Basic way to react to localStorage changes
    });
     // Watch for route changes to update login status, as localStorage changes might not be picked up reactively by other tabs/components immediately
    router.afterEach(() => {
      checkLoginStatus();
    });


    const performSearch = () => {
      if (searchQuery.value.trim()) {
        router.push({ name: 'ProductList', query: { name: searchQuery.value.trim() } });
        searchQuery.value = ''; // Clear search bar after search
      }
    };

    const logout = () => {
      localStorage.removeItem('loggedInUser');
      localStorage.removeItem('userToken');
      localStorage.removeItem('userId');
      isLoggedIn.value = false;
      username.value = '';
      cartStore.loadCart(); // Recalculate cart based on potentially different user or cleared state
      router.push('/'); // Redirect to home or login page
      // In a real app, also notify backend if session needs invalidation server-side
    };

    return {
      searchQuery,
      performSearch,
      cartStore,
      isLoggedIn,
      username,
      logout
    };
  }
}
</script>

<style scoped>
.site-header {
  background-color: #fff;
  padding: 15px 0;
  border-bottom: 1px solid #e0e0e0;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 15px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap; /* Allow wrapping for smaller screens */
}

.logo-area {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
  font-size: 1.5em;
  font-weight: bold;
}

.logo-img {
  height: 40px; /* Adjust as needed */
  margin-right: 10px;
}

.search-area {
  display: flex;
  align-items: center;
  flex-grow: 1; /* Allow search area to take available space */
  margin: 10px 20px; /* Add some margin for spacing */
  min-width: 200px; /* Minimum width */
  max-width: 500px; /* Maximum width */
}

.search-area input[type="search"] {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px 0 0 4px;
  font-size: 1em;
  flex-grow: 1;
  min-width: 150px;
}

.search-button {
  padding: 10px 15px;
  background-color: #f0c14b; /* Taobao-like yellow/orange */
  border: 1px solid #a88734;
  color: #111;
  border-radius: 0 4px 4px 0;
  cursor: pointer;
  font-size: 1em;
}
.search-button:hover {
  background-color: #e4b343;
}

.navigation-area {
  display: flex;
  align-items: center;
  gap: 15px; /* Space between nav links */
}

.navigation-area a, .user-actions span {
  text-decoration: none;
  color: #333;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.navigation-area a:hover {
  background-color: #f0f0f0;
  color: #007bff;
}

.navigation-area a.router-link-exact-active {
  color: #007bff;
  font-weight: bold;
}

.cart-count {
  font-weight: bold;
  color: #d9534f; /* Reddish color for count */
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logout-button {
  background: none;
  border: none;
  color: #007bff;
  cursor: pointer;
  padding: 5px;
  font-size: 0.9em;
}
.logout-button:hover {
  text-decoration: underline;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .container {
    flex-direction: column;
  }
  .search-area {
    order: 1; /* Move search below logo on small screens */
    width: 100%; /* Full width search */
    margin: 15px 0;
    max-width: none;
  }
  .navigation-area {
    order: 2;
    width: 100%;
    justify-content: center; /* Center nav links */
    margin-top: 10px;
    flex-wrap: wrap;
  }
  .logo-area {
     margin-bottom: 10px;
  }
}
</style>
