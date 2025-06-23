<template>
  <div class="featured-product-card">
    <router-link :to="{ name: 'ProductDetail', params: { id: product.id } }" class="product-link">
      <img :src="product.imageUrl || defaultImage" :alt="product.name" class="product-image">
      <div class="product-info">
        <h3 class="product-name">{{ product.name }}</h3>
        <p class="product-price">¥{{ product.price.toFixed(2) }}</p>
      </div>
    </router-link>
    <button @click="addToCart" class="btn btn-primary add-to-cart-btn">加入购物车</button>
  </div>
</template>

<script>
import { useCartStore } from '@/stores/cart';

export default {
  name: 'FeaturedProductCard',
  props: {
    product: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    const cartStore = useCartStore();
    const defaultImage = 'https://via.placeholder.com/200x200?text=No+Image'; // Or a Chinese placeholder: ?text=暂无图片

    const addToCart = () => {
      cartStore.addItem(props.product);
      alert(`${props.product.name} 已加入购物车！`); // Simple feedback
    };

    return {
      defaultImage,
      addToCart
    };
  }
}
</script>

<style scoped>
.featured-product-card {
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.07);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* Pushes button to bottom if info is short */
}

.featured-product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.product-link {
  text-decoration: none;
  color: inherit;
  display: block;
}

.product-image {
  width: 100%;
  height: 200px; /* Fixed height for image consistency */
  object-fit: cover; /* Cover ensures image fills space, might crop */
}

.product-info {
  padding: 15px;
}

.product-name {
  font-size: 1.1em;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #333;
  /* Truncate long names */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-price {
  font-size: 1.2em;
  font-weight: bold;
  color: #d9534f; /* Price color */
  margin: 0;
}

.add-to-cart-btn {
  display: block;
  width: calc(100% - 30px); /* Full width minus padding */
  margin: 0 15px 15px 15px; /* Margin for spacing */
  padding: 10px;
}
</style>
