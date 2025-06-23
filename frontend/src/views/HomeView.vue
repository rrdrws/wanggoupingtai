<template>
  <div class="home-view view-container-global">

    <!-- Optional: Hero Section / Main Banner -->
    <section class="hero-section">
      <h1>Welcome to MyShop</h1>
      <p>Your one-stop shop for everything you need!</p>
      <!-- The main search is in TheHeader, but you can add another prominent one here if desired -->
      <!-- <div class="home-search-bar">
        <input type="search" placeholder="I'm shopping for...">
        <button class="btn btn-primary">Search</button>
      </div> -->
    </section>

    <div class="home-layout">
      <aside class="sidebar">
        <CategoryNavigation />
      </aside>

      <main class="main-content-home">
        <section class="featured-products">
          <h2>Featured Products</h2>
          <div v-if="loading" class="loading">Loading featured products...</div>
          <div v-if="error" class="error-message">{{ error }}</div>
          <div v-if="!loading && !error" class="products-grid">
            <FeaturedProductCard
              v-for="product in featuredProducts"
              :key="product.id"
              :product="product"
            />
          </div>
          <p v-if="!loading && !error && featuredProducts.length === 0" class="no-products">
            No featured products available at the moment.
          </p>
        </section>

        <!-- Placeholder for other sections like promotions, new arrivals etc. -->
        <!-- <section class="promotions">
          <h2>Special Offers</h2>
          <p>Promotion banners or content here...</p>
        </section> -->
      </main>
    </div>

  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import ApiService from '@/services/ApiService';
import CategoryNavigation from '@/components/home/CategoryNavigation.vue';
import FeaturedProductCard from '@/components/home/FeaturedProductCard.vue';
// HelloWorld is no longer used directly here unless specifically needed.
// import HelloWorld from '@/components/HelloWorld.vue'

export default {
  name: 'HomeView',
  components: {
    CategoryNavigation,
    FeaturedProductCard,
    // HelloWorld
  },
  setup() {
    const featuredProducts = ref([]);
    const loading = ref(true);
    const error = ref(null);

    const fetchFeaturedProducts = async () => {
      loading.value = true;
      error.value = null;
      try {
        // Fetch a small number of products to feature, e.g., first 4-8 products
        // Taobao might have complex logic for "featured"; here we just take some from the general list.
        // The backend /api/products doesn't have pagination/limit yet, so it will fetch all.
        // We'll slice it on the frontend for now.
        const response = await ApiService.getProducts();
        featuredProducts.value = response.data.slice(0, 8); // Display up to 8 featured products
      } catch (err) {
        console.error('Error fetching featured products:', err);
        error.value = 'Failed to load featured products.';
      } finally {
        loading.value = false;
      }
    };

    onMounted(() => {
      fetchFeaturedProducts();
    });

    return {
      featuredProducts,
      loading,
      error
    };
  }
}
</script>

<style scoped>
.home-view {
  /* .view-container-global provides max-width and padding */
}

.hero-section {
  background-color: #007bff; /* Example: Primary color */
  color: white;
  padding: 40px 20px;
  text-align: center;
  margin-bottom: 30px;
  border-radius: 8px;
}
.hero-section h1 {
  margin-top: 0;
  font-size: 2.5em;
}
.hero-section p {
  font-size: 1.2em;
  margin-bottom: 0;
}
/* Optional prominent search bar for home
.home-search-bar {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}
.home-search-bar input {
  flex-grow: 1;
  border-radius: 4px 0 0 4px;
  border-right: none;
}
.home-search-bar button {
  border-radius: 0 4px 4px 0;
}
*/

.home-layout {
  display: flex;
  gap: 20px; /* Space between sidebar and main content */
}

.sidebar {
  flex: 0 0 250px; /* Sidebar width, doesn't grow or shrink */
  /* Taobao often has categories on the left */
}

.main-content-home {
  flex-grow: 1; /* Main content takes remaining space */
}

.featured-products h2 {
  font-size: 1.8em;
  margin-bottom: 20px;
  color: #333;
  border-bottom: 2px solid #007bff;
  padding-bottom: 10px;
  display: inline-block;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); /* Responsive grid */
  gap: 20px;
}

.loading, .error-message, .no-products {
  text-align: center;
  padding: 20px;
  font-size: 1.1em;
}
.error-message {
  color: red;
}
.no-products {
  color: #666;
}

/* Responsive adjustments for layout */
@media (max-width: 992px) {
  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }
}

@media (max-width: 768px) {
  .home-layout {
    flex-direction: column; /* Stack sidebar and main content */
  }
  .sidebar {
    flex: 0 0 auto; /* Allow sidebar to take its content height */
    width: 100%;
    margin-bottom: 20px;
  }
  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  }
}

@media (max-width: 576px) {
  .hero-section h1 {
    font-size: 2em;
  }
  .hero-section p {
    font-size: 1em;
  }
  .featured-products h2 {
    font-size: 1.5em;
  }
  .products-grid {
    /* Switch to 2 columns for very small screens if minmax doesn't give desired effect */
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }
}
</style>
