<template>
  <div class="product-list-view view-container-global"> <!-- Updated class & use global container -->
    <h1>Products</h1>

    <div class="filters-panel"> <!-- Updated class -->
      <input type="text" v-model="searchName" placeholder="Search by name..." @keyup.enter="applyFilters" />
      <input type="text" v-model="searchCategory" placeholder="Filter by category..." @keyup.enter="applyFilters" />
      <button @click="applyFilters" class="btn btn-secondary">Search/Filter</button>
      <button @click="clearFilters" class="btn btn-outline">Clear</button>
    </div>

    <div v.if="loading" class="loading">Loading products...</div>
    <div v.if="error" class="error-message">{{ error }}</div>
    <div v.if="!loading && !error" class="products-grid">
      <div v-for="product in products" :key="product.id" class="product-card">
        <router-link :to="{ name: 'ProductDetail', params: { id: product.id } }" class="product-image-link">
          <img :src="product.imageUrl || defaultImage" :alt="product.name" class="product-image">
        </router-link>
        <div class="product-info">
          <h2 class="product-name">{{ product.name }}</h2>
          <p class="product-description">{{ product.description }}</p>
          <p class="product-price">${{ product.price ? product.price.toFixed(2) : '0.00' }}</p>
        </div>
        <div class="product-actions">
          <router-link :to="{ name: 'ProductDetail', params: { id: product.id } }" class="btn btn-secondary">
            View Details
          </router-link>
          <button @click="addToCart(product)" class="btn btn-primary">Add to Cart</button>
        </div>
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
.product-list-view { /* Changed class name for clarity */
  /* Uses .view-container-global from App.vue for max-width and padding */
}

.filters-panel { /* Renamed for clarity */
  background-color: #fff;
  padding: 15px;
  margin-bottom: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.05);
  display: flex;
  gap: 15px;
  align-items: center;
  flex-wrap: wrap;
}

.filters-panel input[type="text"] {
  padding: 10px; /* Slightly larger padding */
  border: 1px solid #ccc;
  border-radius: 4px;
  min-width: 200px; /* Increased min-width */
  font-size: 0.95em;
}

/* Using global .btn styles, specific overrides if needed */
.filters-panel .btn {
  padding: 10px 18px; /* Consistent padding with inputs */
}

.loading, .error-message, .no-products { /* .no-products was added in template */
  text-align: center;
  padding: 25px;
  font-size: 1.1em;
}
.error-message {
  color: #d9534f; /* Bootstrap danger color */
}
.no-products {
  color: #666;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(230px, 1fr)); /* Slightly adjusted minmax */
  gap: 25px; /* Increased gap */
}

.product-card {
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden; /* Ensures child elements like image don't overflow radius */
  box-shadow: 0 2px 8px rgba(0,0,0,0.07);
  transition: transform 0.2s ease-out, box-shadow 0.2s ease-out;
  display: flex;
  flex-direction: column; /* Important for button placement */
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.12);
}

.product-image-link { /* Wrapper for image to make it clickable */
  display: block;
}

.product-image {
  width: 100%;
  height: 220px; /* Increased height for better visuals */
  object-fit: cover; /* 'cover' is usually better than 'contain' for cards */
  /* border-bottom: 1px solid #f0f0f0; Remove if not desired */
}

.product-info {
  padding: 15px;
  text-align: left; /* Align text to left for typical card layout */
  flex-grow: 1; /* Allows info to take space, pushing actions down */
}

.product-name {
  font-size: 1.1em; /* Slightly larger name */
  font-weight: 600; /* Bolder name */
  color: #333;
  margin: 0 0 8px 0;
  /* Truncate long names - good for consistency */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-description {
  font-size: 0.85em; /* Smaller description */
  color: #555;
  margin-bottom: 12px;
  min-height: 50px; /* Approx 3 lines */
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3; /* Limit to 3 lines */
  -webkit-box-orient: vertical;
}

.product-price {
  font-size: 1.25em; /* Larger price */
  font-weight: bold;
  color: #d9534f; /* Prominent price color, e.g., Taobao red */
  margin-bottom: 15px;
}

.product-actions {
  padding: 0 15px 15px 15px; /* Padding for buttons */
  display: flex;
  gap: 10px; /* Space between buttons if multiple */
  margin-top: auto; /* Pushes actions to the bottom of the card */
}

.product-actions .btn {
  flex-grow: 1; /* Make buttons take equal width if needed */
  padding: 10px; /* Ensure consistent button padding */
  font-size: 0.9em;
}
/* No need for .router-link-active styling here as it's handled by global .btn style */

@media (max-width: 576px) {
  .filters-panel {
    flex-direction: column;
    align-items: stretch; /* Make input and buttons full width */
  }
  .filters-panel input[type="text"], .filters-panel .btn {
    min-width: unset;
    width: 100%;
  }
  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr)); /* Smaller cards on mobile */
    gap: 15px;
  }
  .product-image {
    height: 180px;
  }
  .product-name {
    font-size: 1em;
  }
  .product-price {
    font-size: 1.15em;
  }
}
</style>
