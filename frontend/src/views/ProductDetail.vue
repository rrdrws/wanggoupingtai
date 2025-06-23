<template>
  <div class="product-detail-view view-container-global">
    <div v-if="loading" class="loading">商品详情加载中...</div>
    <div v-if="error" class="error-message">{{ error }}</div> <!-- Error message will be translated when set -->
    <div v-if="product" class="product-layout">
      <div class="product-image-gallery">
        <img :src="product.imageUrl || defaultImage" :alt="product.name" class="main-product-image">
        <!-- Placeholder for additional thumbnail images if any -->
      </div>
      <div class="product-info-details">
        <h1>{{ product.name }}</h1>
        <p class="product-category">分类: {{ product.category || '暂无分类' }}</p>
        <p class="product-price-detail">¥{{ product.price ? product.price.toFixed(2) : '暂无价格' }}</p> <!-- Changed currency -->

        <div class="product-description-full">
          <h3>商品描述</h3>
          <p>{{ product.description || '暂无描述。' }}</p>
        </div>

        <div class="product-stock">
          <p>库存: <span :class="{'in-stock': product.stockQuantity > 0, 'out-of-stock': product.stockQuantity === 0}">
            {{ product.stockQuantity > 0 ? `${product.stockQuantity} 件可用` : '已售罄' }}
          </span></p>
        </div>

        <div class="actions">
          <div class="quantity-selector" v-if="product.stockQuantity > 0">
            <label for="quantity">数量:</label>
            <input type="number" id="quantity" v-model.number="quantity" min="1" :max="product.stockQuantity">
          </div>
          <button
            @click="addToCartHandler"
            class="btn btn-primary btn-add-to-cart"
            :disabled="product.stockQuantity === 0 || quantity <= 0"
          >
            加入购物车
          </button>
        </div>
      </div>
    </div>
    <div v-if="!loading && !product && !error" class="not-found">
      <h2>未找到商品</h2>
      <p>抱歉，您查找的商品不存在。</p>
      <router-link to="/products" class="btn btn-secondary">返回商品列表</router-link>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import ApiService from '@/services/ApiService';
import { useCartStore } from '@/stores/cart';

export default {
  name: 'ProductDetailView', // Renamed for consistency
  props: ['id'], // From router params
  setup(props) {
    const route = useRoute();
    const cartStore = useCartStore();

    const product = ref(null);
    const loading = ref(true);
    const error = ref(null);
    const quantity = ref(1);
    const defaultImage = 'https://via.placeholder.com/400x400?text=No+Image';

    const fetchProductDetails = async (productId) => {
      loading.value = true;
      error.value = null;
      product.value = null; // Reset product before fetching new one
      try {
        const response = await ApiService.getProductById(productId);
        product.value = response.data;
        quantity.value = product.value && product.value.stockQuantity > 0 ? 1 : 0; // Reset quantity
      } catch (err) {
        console.error(`Error fetching product details for ID ${productId}:`, err);
        error.value = '加载商品详情失败。该商品可能不存在或网络连接有问题。';
      } finally {
        loading.value = false;
      }
    };

    onMounted(() => {
      fetchProductDetails(props.id);
    });

    // Watch for changes in route param 'id' if user navigates from one product detail to another
    watch(() => props.id, (newId) => {
      if (newId) {
        fetchProductDetails(newId);
      }
    });
     watch(() => route.params.id, (newId) => {
      if (newId && newId !== props.id) { // Ensure it's actually a different product
        fetchProductDetails(newId);
      }
    });


    const addToCartHandler = () => {
      if (product.value && quantity.value > 0 && quantity.value <= product.value.stockQuantity) {
        cartStore.addItem(product.value, quantity.value);
        alert(`${quantity.value} 件 ${product.value.name} 已加入购物车！`);
      } else if (product.value && quantity.value > product.value.stockQuantity) {
        alert(`无法添加 ${quantity.value} 件商品。库存仅剩 ${product.value.stockQuantity} 件。`);
      }
    };

    return {
      product,
      loading,
      error,
      quantity,
      defaultImage,
      addToCartHandler
    };
  }
}
</script>

<style scoped>
.product-detail-view {
  /* Uses .view-container-global for max-width and padding */
}

.loading, .error-message, .not-found {
  text-align: center;
  padding: 30px;
  font-size: 1.1em;
}
.error-message { color: #d9534f; }
.not-found h2 { margin-bottom: 15px; }

.product-layout {
  display: flex;
  gap: 30px; /* Space between image gallery and details */
  background-color: #fff;
  padding: 25px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
}

.product-image-gallery {
  flex: 1 1 40%; /* Takes about 40% of width */
  max-width: 400px; /* Max width for image area */
}

.main-product-image {
  width: 100%;
  max-height: 400px;
  object-fit: contain; /* Contain to see full image, 'cover' if you prefer fill */
  border-radius: 8px;
  border: 1px solid #eee;
}

.product-info-details {
  flex: 1 1 60%; /* Takes about 60% of width */
  display: flex;
  flex-direction: column;
}

.product-info-details h1 {
  font-size: 2em;
  margin-top: 0;
  margin-bottom: 10px;
  color: #333;
}

.product-category {
  font-size: 0.9em;
  color: #666;
  margin-bottom: 15px;
}

.product-price-detail {
  font-size: 1.8em;
  font-weight: bold;
  color: #d9534f; /* Taobao-like red for price */
  margin-bottom: 20px;
}

.product-description-full h3 {
  font-size: 1.2em;
  margin-bottom: 8px;
  color: #444;
}
.product-description-full p {
  line-height: 1.7;
  color: #555;
  margin-bottom: 20px;
}

.product-stock {
  margin-bottom: 20px;
  font-size: 1em;
}
.in-stock { color: #28a745; font-weight: bold; } /* Green for in stock */
.out-of-stock { color: #dc3545; font-weight: bold; } /* Red for out of stock */

.actions {
  margin-top: auto; /* Push actions to the bottom if content is short */
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
}

.quantity-selector {
  display: flex;
  align-items: center;
  gap: 8px;
}
.quantity-selector label {
  font-size: 0.95em;
  color: #555;
}
.quantity-selector input[type="number"] {
  width: 70px;
  padding: 8px;
  text-align: center;
  /* Global input styles apply */
}

.btn-add-to-cart {
  padding: 12px 25px;
  font-size: 1.1em;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .product-layout {
    flex-direction: column;
  }
  .product-image-gallery, .product-info-details {
    flex-basis: auto; /* Reset flex-basis */
    max-width: 100%;
  }
  .main-product-image {
    max-height: 300px; /* Adjust image height for mobile */
  }
  .product-info-details h1 { font-size: 1.6em; }
  .product-price-detail { font-size: 1.5em; }
}
</style>
