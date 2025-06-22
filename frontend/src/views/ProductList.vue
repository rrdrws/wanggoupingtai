<template>
  <div class="product-list view-container">
    <h1>Products</h1>

    <div class="filters">
      <input type="text" v-model="searchName" placeholder="Search by name..." @keyup.enter="applyFilters" />
      <input type="text" v-model="searchCategory" placeholder="Filter by category..." @keyup.enter="applyFilters" />
      <button @click="applyFilters" class="btn btn-secondary">Search/Filter</button>
      <button @click="clearFilters" class="btn btn-outline">Clear</button>
    </div>

    <div v.if="loading" class="loading">Loading products...</div>
    <div v.if="error" class="error-message">{{ error }}</div>
    <div v.if="!loading && !error" class="products-grid">
      <div v-for="product in products" :key="product.id" class="product-card">
        <img :src="product.imageUrl || defaultImage" :alt="product.name" class="product-image">
        <h2>{{ product.name }}</h2>
        <p class="product-price">${{ product.price }}</p>
        <p class="product-description">{{ product.description }}</p>
        <router-link :to="{ name: 'ProductDetail', params: { id: product.id } }" class="btn">
          View Details
        </router-link>
        <button @click="addToCart(product)" class="btn btn-primary">Add to Cart</button>
      </div>
    </div>
    <p v.if="!loading && !error && products.length === 0" class="no-products">No products found matching your criteria.</p>
  </div>
</template>

<script>
import ApiService from '@/services/ApiService';
import { useCartStore } from '@/stores/cart';

export default {
  name: 'ProductList',
  setup() {
    const cartStore = useCartStore();
    return { cartStore };
  },
  data() {
    return {
      products: [],
      loading: true,
      error: null,
      defaultImage: 'https://via.placeholder.com/150?text=No+Image',
      searchName: '',
      searchCategory: ''
    };
  },
  created() {
    this.fetchProducts();
  },
  methods: {
    async fetchProducts(params = {}) {
      this.loading = true;
      this.error = null;
      try {
        const response = await ApiService.getProducts(params);
        this.products = response.data;
      } catch (err) {
        console.error('Error fetching products:', err);
        this.error = 'Failed to load products. Please try again later.';
        if (err.response) {
          this.error += ` (Status: ${err.response.status})`;
        }
      } finally {
        this.loading = false;
      }
    },
    applyFilters() {
      const params = {};
      if (this.searchName) {
        params.name = this.searchName;
      }
      if (this.searchCategory) {
        params.category = this.searchCategory;
      }
      this.fetchProducts(params);
    },
    clearFilters() {
      this.searchName = '';
      this.searchCategory = '';
      this.fetchProducts();
    },
    addToCart(product) {
      this.cartStore.addItem(product);
      alert(`${product.name} added to cart!`);
      // Optionally, provide more sophisticated user feedback (e.g., a toast notification)
    }
  }
}
</script>

<style scoped>
.view-container {
  padding: 20px;
}
.filters {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap; /* Allow filters to wrap on smaller screens */
}
.filters input[type="text"] {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  min-width: 150px; /* Minimum width for inputs */
}
.btn-secondary {
  background-color: #6c757d;
  color: white;
  border-color: #6c757d;
}
.btn-secondary:hover {
  background-color: #5a6268;
}
.btn-outline {
  background-color: transparent;
  color: #007bff;
  border: 1px solid #007bff;
}
.btn-outline:hover {
  background-color: #007bff;
  color: white;
}
.loading, .error-message {
  text-align: center;
  padding: 20px;
}
.error-message {
  color: red;
}
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}
.product-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
.product-image {
  max-width: 100%;
  height: 150px;
  object-fit: cover;
  margin-bottom: 10px;
  border-radius: 4px;
}
.product-price {
  font-weight: bold;
  color: #333;
  margin: 10px 0;
}
.product-description {
  font-size: 0.9em;
  color: #666;
  min-height: 60px; /* Adjust as needed */
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3; /* Limit to 3 lines */
  -webkit-box-orient: vertical;
}
.btn {
  display: inline-block;
  padding: 8px 15px;
  margin-top: 10px;
  text-decoration: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9em;
  border: 1px solid transparent;
}
.btn-primary {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
  margin-left: 5px;
}
.btn-primary:hover {
  background-color: #0056b3;
}
.router-link-active { /* Style for router-link as button */
  background-color: #6c757d;
  color: white;
}
.router-link-active:hover {
  background-color: #5a6268;
}
</style>
