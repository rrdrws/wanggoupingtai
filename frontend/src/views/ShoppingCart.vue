<template>
  <div class="shopping-cart view-container">
    <h1>Shopping Cart</h1>
    <div v-if="cartStore.itemCount === 0" class="empty-cart">
      <p>Your shopping cart is empty.</p>
      <router-link to="/products" class="btn btn-primary">Continue Shopping</router-link>
    </div>
    <div v-else>
      <div class="cart-items">
        <div v-for="item in cartStore.cartItems" :key="item.product.id" class="cart-item">
          <div class="item-info">
            <img :src="item.product.imageUrl || defaultImage" :alt="item.product.name" class="item-image">
            <div>
              <h3>{{ item.product.name }}</h3>
              <p>Price: ${{ item.product.price }}</p>
            </div>
          </div>
          <div class="item-controls">
            <label :for="'quantity-' + item.product.id">Quantity:</label>
            <input
              type="number"
              :id="'quantity-' + item.product.id"
              :value="item.quantity"
              @change="updateQuantity(item.product.id, $event.target.value)"
              min="1"
              class="quantity-input"
            >
            <p>Subtotal: ${{ (item.product.price * item.quantity).toFixed(2) }}</p>
            <button @click="removeFromCart(item.product.id)" class="btn btn-danger">Remove</button>
          </div>
        </div>
      </div>
      <div class="cart-summary">
        <h2>Total: ${{ cartStore.totalPrice }}</h2>
        <button @click="checkout" class="btn btn-success btn-checkout">Proceed to Checkout</button>
        <button @click="clearCart" class="btn btn-warning">Clear Cart</button>
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
      if(confirm('Are you sure you want to clear the entire cart?')) {
        cartStore.clearCart();
      }
    };

    const checkout = async () => {
      if (cartStore.itemCount === 0) {
        alert('Your cart is empty. Add some products before checking out.');
        return;
      }

      // Basic check for logged-in user (placeholder - real auth state needed)
      const loggedInUser = localStorage.getItem('loggedInUser');
      const userId = localStorage.getItem('userId'); // Assume userId is stored on login

      if (!loggedInUser || !userId) {
        alert('Please login to proceed with checkout.');
        // Optionally redirect to login page: this.$router.push('/login');
        // For setup context, router needs to be accessed differently if not using `this`.
        // import { useRouter } from 'vue-router'; const router = useRouter(); router.push('/login');
        return;
      }

      // Simulate gathering shipping/billing - in real app, this would be a form
      const shippingAddress = prompt("Enter your shipping address:", "123 Main St, Anytown, USA");
      if (!shippingAddress) {
        alert("Shipping address is required.");
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

      alert(`Proceeding to checkout for ${loggedInUser} with ${cartStore.itemCount} items, total: $${cartStore.totalPrice}. Shipping to: ${shippingAddress}`);

      try {
        // Assuming ApiService.createOrder exists and works with the simplified OrderRequest DTO
        // The backend OrderController expects OrderRequest: { userId, totalAmount, shippingAddress, billingAddress }
        const response = await ApiService.createOrder(orderData);
        alert(`Order placed successfully! Order ID: ${response.data.id}. You will be redirected shortly.`);
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
        alert("There was an error placing your order. Please try again. " + (error.response?.data?.message || error.message));
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
