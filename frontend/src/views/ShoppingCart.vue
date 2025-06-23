<template>
  <div class="shopping-cart view-container">
    <h1>购物车</h1>
    <div v-if="cartStore.itemCount === 0" class="empty-cart">
      <p>您的购物车是空的。</p>
      <router-link to="/products" class="btn btn-primary">继续购物</router-link>
    </div>
    <div v-else>
      <div class="cart-items">
        <div v-for="item in cartStore.cartItems" :key="item.product.id" class="cart-item">
          <div class="item-info">
            <img :src="item.product.imageUrl || defaultImage" :alt="item.product.name" class="item-image">
            <div>
              <h3>{{ item.product.name }}</h3>
              <p>价格: ¥{{ item.product.price }}</p> <!-- Changed currency -->
            </div>
          </div>
          <div class="item-controls">
            <label :for="'quantity-' + item.product.id">数量:</label>
            <input
              type="number"
              :id="'quantity-' + item.product.id"
              :value="item.quantity"
              @change="updateQuantity(item.product.id, $event.target.value)"
              min="1"
              class="quantity-input"
            >
            <p>小计: ¥{{ (item.product.price * item.quantity).toFixed(2) }}</p> <!-- Changed currency -->
            <button @click="removeFromCart(item.product.id)" class="btn btn-danger">移除</button>
          </div>
        </div>
      </div>
      <div class="cart-summary">
        <h2>总计: ¥{{ cartStore.totalPrice }}</h2> <!-- Changed currency -->
        <button @click="checkout" class="btn btn-success btn-checkout">去结算</button>
        <button @click="clearCart" class="btn btn-warning">清空购物车</button>
      </div>
    </div>
  </div>
</template>

<script>
import { useCartStore } from '@/stores/cart';
import { onMounted } from 'vue';

export default {
  name: 'ShoppingCart',
  setup() {
    const cartStore = useCartStore();

    // Load cart from localStorage when component is mounted
    onMounted(() => {
      cartStore.loadCart();
    });

    const defaultImage = 'https://via.placeholder.com/100?text=No+Image';

    const removeFromCart = (productId) => {
      cartStore.removeItem(productId);
    };

    const updateQuantity = (productId, quantity) => {
      const numQuantity = parseInt(quantity, 10);
      if (!isNaN(numQuantity) && numQuantity >= 0) { // Allow 0 to remove via update
        cartStore.updateItemQuantity(productId, numQuantity);
      }
    };

    const clearCart = () => {
      if(confirm('您确定要清空整个购物车吗？')) {
        cartStore.clearCart();
      }
    };

    const checkout = async () => {
      if (cartStore.itemCount === 0) {
        alert('您的购物车是空的。请先添加商品再结算。');
        return;
      }

      // Basic check for logged-in user (placeholder - real auth state needed)
      const loggedInUser = localStorage.getItem('loggedInUser');
      const userId = localStorage.getItem('userId'); // Assume userId is stored on login

      if (!loggedInUser || !userId) {
        alert('请先登录再进行结算。');
        // Optionally redirect to login page: this.$router.push('/login');
        // For setup context, router needs to be accessed differently if not using `this`.
        // import { useRouter } from 'vue-router'; const router = useRouter(); router.push('/login');
        return;
      }

      // Simulate gathering shipping/billing - in real app, this would be a form
      const shippingAddress = prompt("请输入您的收货地址:", "例如：XX省XX市XX区XX街道XX号");
      if (!shippingAddress) {
        alert("收货地址是必填项。");
        return;
      }

      const orderData = {
        userId: parseInt(userId), // Ensure userId is a number
        totalAmount: parseFloat(cartStore.totalPrice),
        shippingAddress: shippingAddress,
        billingAddress: shippingAddress, // Simplified: use same for billing
        // In a real app, you'd send cartItems: cartStore.items.map(item => ({ productId: item.product.id, quantity: item.quantity, price: item.product.price }))
        // The backend Order model currently doesn't have OrderItems, so this is simplified.
      };

      alert(`用户 ${loggedInUser} 正在结算 ${cartStore.itemCount} 件商品，总计: ¥${cartStore.totalPrice}。收货地址: ${shippingAddress}`);

      try {
        // Assuming ApiService.createOrder exists and works with the simplified OrderRequest DTO
        // The backend OrderController expects OrderRequest: { userId, totalAmount, shippingAddress, billingAddress }
        const response = await ApiService.createOrder(orderData);
        alert(`订单提交成功！订单号: ${response.data.id}。稍后将为您跳转。`);
        cartStore.clearCart();
        // Redirect to an order confirmation page or home
        // Example: router.push({ name: 'OrderConfirmation', params: { orderId: response.data.id } });
        // For now, just log and maybe redirect to home after a delay.
        console.log('Order created:', response.data);
        setTimeout(() => {
          // Need to get router instance if redirecting from setup
          // For simplicity, this example won't redirect from here.
        }, 2000);

      } catch (error) {
        console.error("Error placing order:", error);
        alert("提交订单时发生错误，请重试。" + (error.response?.data?.message || error.message));
      }
    };

    return {
      cartStore,
      defaultImage,
      removeFromCart,
      updateQuantity,
      clearCart,
      checkout
    };
  }
}
</script>

<style scoped>
.view-container {
  padding: 20px;
  max-width: 900px;
  margin: 20px auto;
}
.empty-cart {
  text-align: center;
  padding: 30px;
}
.empty-cart p {
  margin-bottom: 20px;
  font-size: 1.2em;
}
.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
  padding: 15px 0;
}
.cart-item:last-child {
  border-bottom: none;
}
.item-info {
  display: flex;
  align-items: center;
  gap: 15px;
}
.item-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}
.item-info h3 {
  margin: 0 0 5px 0;
  font-size: 1.1em;
}
.item-info p {
  margin: 0;
  color: #555;
}
.item-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}
.quantity-input {
  width: 60px;
  padding: 5px;
  text-align: center;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.cart-summary {
  margin-top: 30px;
  text-align: right;
  padding-top: 20px;
  border-top: 2px solid #ddd;
}
.cart-summary h2 {
  margin-bottom: 20px;
}
.btn {
  padding: 10px 15px;
  text-decoration: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
  border: none;
  margin-left: 10px; /* Spacing between buttons */
}
.btn-primary {
  background-color: #007bff;
  color: white;
}
.btn-primary:hover {
  background-color: #0056b3;
}
.btn-danger {
  background-color: #dc3545;
  color: white;
}
.btn-danger:hover {
  background-color: #c82333;
}
.btn-warning {
  background-color: #ffc107;
  color: #212529;
}
.btn-warning:hover {
  background-color: #e0a800;
}
.btn-success {
  background-color: #28a745;
  color: white;
}
.btn-success:hover {
  background-color: #218838;
}
.btn-checkout {
  font-size: 1.1em;
  padding: 12px 20px;
}
</style>
