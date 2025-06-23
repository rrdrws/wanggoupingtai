<template>
  <div class="product-publish-view view-container-global">
    <div class="publish-panel">
      <h1>发布新商品</h1>
      <form @submit.prevent="handlePublishProduct" class="publish-form">
        <div class="form-group">
          <label for="name">商品名称</label>
          <input type="text" id="name" v.model="product.name" required placeholder="请输入商品名称" />
        </div>
        <div class="form-group">
          <label for="description">商品描述</label>
          <textarea id="description" v.model="product.description" rows="4" required placeholder="请输入商品描述"></textarea>
        </div>
        <div class="form-row">
          <div class="form-group half-width">
            <label for="price">价格 (¥)</label> <!-- Changed currency symbol -->
            <input type="number" id="price" v.model.number="product.price" step="0.01" min="0" required placeholder="例如：19.99" />
          </div>
          <div class="form-group half-width">
            <label for="stockQuantity">库存数量</label>
            <input type="number" id="stockQuantity" v.model.number="product.stockQuantity" min="0" required placeholder="例如：100" />
          </div>
        </div>
        <div class="form-group">
          <label for="category">商品分类</label>
          <input type="text" id="category" v.model="product.category" required placeholder="例如：电子产品、图书" />
        </div>
        <div class="form-group">
          <label for="imageUrl">图片链接</label>
          <input type="url" id="imageUrl" v.model="product.imageUrl" placeholder="https://example.com/image.jpg" />
        </div>

        <button type="submit" :disabled="loading" class="btn btn-primary btn-publish">
          {{ loading ? '发布中...' : '发布商品' }}
        </button>
      </form>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p> <!-- Error messages translated in script -->
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p> <!-- Success messages translated in script -->
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
          errorMessage.value = "价格必须是非负数。"; // Price must be a non-negative number.
          loading.value = false;
          return;
        }
        if (product.value.stockQuantity === null || product.value.stockQuantity < 0) {
          errorMessage.value = "库存数量必须是非负数。"; // Stock quantity must be a non-negative number.
          loading.value = false;
          return;
        }

        const response = await ApiService.createProduct(product.value);
        successMessage.value = `商品 "${response.data.name}" 发布成功！商品ID: ${response.data.id}`; // Product "${response.data.name}" published successfully! Product ID: ${response.data.id}
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
          errorMessage.value = `发布商品失败: ${err.response.data.message || '服务器错误'}`; // Failed to publish product: ${err.response.data.message || 'Server error'}
        } else {
          errorMessage.value = '发布商品失败，发生意外错误。'; // Failed to publish product. An unexpected error occurred.
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
