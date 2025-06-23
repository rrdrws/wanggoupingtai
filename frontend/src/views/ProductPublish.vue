<template>
  <div class="product-publish-view view-container-global">
    <div class="publish-panel">
      <h1>Publish New Product</h1>
      <form @submit.prevent="handlePublishProduct" class="publish-form">
        <div class="form-group">
          <label for="name">Product Name</label>
          <input type="text" id="name" v.model="product.name" required placeholder="Enter product name" />
        </div>
        <div class="form-group">
          <label for="description">Description</label>
          <textarea id="description" v.model="product.description" rows="4" required placeholder="Enter product description"></textarea>
        </div>
        <div class="form-row">
          <div class="form-group half-width">
            <label for="price">Price ($)</label>
            <input type="number" id="price" v.model.number="product.price" step="0.01" min="0" required placeholder="e.g., 19.99" />
          </div>
          <div class="form-group half-width">
            <label for="stockQuantity">Stock Quantity</label>
            <input type="number" id="stockQuantity" v.model.number="product.stockQuantity" min="0" required placeholder="e.g., 100" />
          </div>
        </div>
        <div class="form-group">
          <label for="category">Category</label>
          <input type="text" id="category" v.model="product.category" required placeholder="e.g., Electronics, Books" />
        </div>
        <div class="form-group">
          <label for="imageUrl">Image URL</label>
          <input type="url" id="imageUrl" v.model="product.imageUrl" placeholder="https://example.com/image.jpg" />
        </div>

        <button type="submit" :disabled="loading" class="btn btn-primary btn-publish">
          {{ loading ? 'Publishing...' : 'Publish Product' }}
        </button>
      </form>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import ApiService from '@/services/ApiService';
import { useRouter } from 'vue-router'; // Import useRouter

export default {
  name: 'ProductPublishView',
  setup() {
    const router = useRouter(); // Initialize router
    const product = ref({
      name: '',
      description: '',
      price: null,
      category: '',
      imageUrl: '',
      stockQuantity: null
    });
    const loading = ref(false);
    const errorMessage = ref('');
    const successMessage = ref('');

    const handlePublishProduct = async () => {
      loading.value = true;
      errorMessage.value = '';
      successMessage.value = '';

      try {
        // Basic validation for price and stock
        if (product.value.price === null || product.value.price < 0) {
          errorMessage.value = "Price must be a non-negative number.";
          loading.value = false;
          return;
        }
        if (product.value.stockQuantity === null || product.value.stockQuantity < 0) {
          errorMessage.value = "Stock quantity must be a non-negative number.";
          loading.value = false;
          return;
        }

        const response = await ApiService.createProduct(product.value);
        successMessage.value = `Product "${response.data.name}" published successfully! Product ID: ${response.data.id}`;
        // Clear form
        product.value = { name: '', description: '', price: null, category: '', imageUrl: '', stockQuantity: null };
        // Optionally redirect to product page or product list
        setTimeout(() => {
          if (response.data.id) {
            router.push({ name: 'ProductDetail', params: { id: response.data.id } });
          } else {
            router.push({ name: 'ProductList' });
          }
        }, 2000);
      } catch (err) {
        console.error('Error publishing product:', err);
        if (err.response && err.response.data) {
          errorMessage.value = `Failed to publish product: ${err.response.data.message || 'Server error'}`;
        } else {
          errorMessage.value = 'Failed to publish product. An unexpected error occurred.';
        }
      } finally {
        loading.value = false;
      }
    };

    return {
      product,
      loading,
      errorMessage,
      successMessage,
      handlePublishProduct
    };
  }
}
</script>

<style scoped>
.product-publish-view {
  /* Uses .view-container-global for max-width and horizontal padding */
  padding-top: 30px;
  padding-bottom: 30px;
}

.publish-panel {
  background-color: #fff;
  padding: 30px 40px;
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  max-width: 700px; /* Wider panel for more form fields */
  margin: 0 auto; /* Center the panel */
}

.publish-panel h1 {
  text-align: center;
  margin-bottom: 25px;
  font-size: 1.8em;
  color: #333;
}

.publish-form .form-group {
  margin-bottom: 20px;
}

.publish-form label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #555;
  font-size: 0.95em;
}

.publish-form input,
.publish-form textarea {
  width: 100%;
  /* Global input styles from App.vue will apply */
}

.form-row {
  display: flex;
  gap: 20px; /* Space between side-by-side fields */
}
.form-group.half-width {
  flex: 1; /* Each takes half the space */
}

.btn-publish {
  width: 100%;
  padding: 12px;
  font-size: 1.1em;
  margin-top: 10px;
}

.error-message, .success-message {
  text-align: center;
  margin-top: 15px;
  font-size: 0.9em;
  padding: 10px;
  border-radius: 4px;
}
.error-message {
  color: #d9534f;
  background-color: #f8d7da; /* Light red background for error */
}
.success-message {
  color: #28a745;
  background-color: #d4edda; /* Light green background for success */
}

@media (max-width: 576px) {
  .form-row {
    flex-direction: column;
    gap: 0; /* Remove gap if stacking */
  }
  .form-group.half-width {
    margin-bottom: 20px; /* Add margin back if stacked */
  }
  .form-group.half-width:last-child {
    margin-bottom: 0; /* No bottom margin for the last stacked item in row */
  }
   .publish-panel {
    padding: 20px;
  }
}
</style>
