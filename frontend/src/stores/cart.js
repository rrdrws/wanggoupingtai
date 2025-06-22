import { defineStore } from 'pinia';

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: [], // Array of cart items: { product: { id, name, price, ... }, quantity: N }
  }),
  getters: {
    // Get total number of items in the cart
    itemCount: (state) => {
      return state.items.reduce((total, item) => total + item.quantity, 0);
    },
    // Get total price of items in the cart
    totalPrice: (state) => {
      return state.items.reduce((total, item) => {
        return total + (item.product.price * item.quantity);
      }, 0).toFixed(2); // Format to 2 decimal places
    },
    // Get cart items (useful for display)
    cartItems: (state) => {
      return state.items;
    }
  },
  actions: {
    // Add a product to the cart or increment its quantity
    addItem(product, quantity = 1) {
      const existingItem = this.items.find(item => item.product.id === product.id);
      if (existingItem) {
        existingItem.quantity += quantity;
      } else {
        // Ensure product has a price, default to 0 if not (should not happen with backend data)
        const productPrice = typeof product.price === 'number' ? product.price : 0;
        this.items.push({ product: { ...product, price: productPrice }, quantity });
      }
      // Persist cart to localStorage (optional, but good for user experience)
      this.persistCart();
    },
    // Remove a product from the cart
    removeItem(productId) {
      this.items = this.items.filter(item => item.product.id !== productId);
      this.persistCart();
    },
    // Update quantity of an item in the cart
    updateItemQuantity(productId, quantity) {
      const item = this.items.find(item => item.product.id === productId);
      if (item) {
        if (quantity > 0) {
          item.quantity = quantity;
        } else {
          // If quantity is 0 or less, remove the item
          this.removeItem(productId);
        }
      }
      this.persistCart();
    },
    // Clear the cart
    clearCart() {
      this.items = [];
      this.persistCart();
    },
    // Persist cart to localStorage
    persistCart() {
      localStorage.setItem('shoppingCart', JSON.stringify(this.items));
    },
    // Load cart from localStorage (call this when app initializes if using persistence)
    loadCart() {
      const savedCart = localStorage.getItem('shoppingCart');
      if (savedCart) {
        this.items = JSON.parse(savedCart);
      }
    }
  }
});

// Optional: Load cart when the store is initialized (or call it in App.vue's created hook)
// const cartStore = useCartStore();
// cartStore.loadCart();
