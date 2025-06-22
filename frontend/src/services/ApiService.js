import axios from 'axios';

const apiClient = axios.create({
  baseURL: '/api', // Base URL will be handled by the proxy in vue.config.js
  headers: {
    'Content-Type': 'application/json'
    // You can add other headers here, like Authorization for JWT tokens
  }
});

// Interceptor to add JWT token to requests if available (example)
apiClient.interceptors.request.use(config => {
  const token = localStorage.getItem('userToken'); // Example: get token from localStorage
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

export default {
  // User services
  registerUser(userData) {
    return apiClient.post('/users/register', userData);
  },
  loginUser(credentials) {
    return apiClient.post('/users/login', credentials);
  },
  getUser(userId) {
    return apiClient.get(`/users/${userId}`);
  },
  // Add other user-related methods as needed

  // Product services
  getProducts(params) { // params for filtering, e.g., { category: 'electronics' }
    return apiClient.get('/products', { params });
  },
  getProductById(productId) {
    return apiClient.get(`/products/${productId}`);
  },
  // Add other product-related methods (create, update, delete - typically admin)

  // Order services
  createOrder(orderData) {
    return apiClient.post('/orders', orderData);
  },
  getOrdersByUserId(userId) {
    return apiClient.get(`/orders/user/${userId}`);
  },
  getOrderById(orderId) {
    return apiClient.get(`/orders/${orderId}`);
  }
  // Add other order-related methods
};
