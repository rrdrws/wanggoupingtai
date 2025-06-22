// tests/unit/apiService.spec.js
import ApiService from '@/services/ApiService';
import axios from 'axios';

// Mock axios
jest.mock('axios', () => ({
  create: jest.fn(() => ({
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
    interceptors: {
      request: { use: jest.fn() },
      response: { use: jest.fn() }
    }
  }))
}));

// Get a reference to the mocked apiClient instance for detailed mocking per test
const mockAxiosInstance = axios.create();

describe('ApiService', () => {
  beforeEach(() => {
    // Clear all mock implementations and calls before each test
    mockAxiosInstance.get.mockClear();
    mockAxiosInstance.post.mockClear();
    // localStorage.clear(); // Clear localStorage if tests depend on it for tokens
  });

  describe('User services', () => {
    it('registerUser should make a POST request to /users/register', async () => {
      const userData = { username: 'testuser', password: 'password', email: 'test@example.com' };
      mockAxiosInstance.post.mockResolvedValue({ data: { id: 1, ...userData } });

      await ApiService.registerUser(userData);

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/users/register', userData);
    });

    it('loginUser should make a POST request to /users/login', async () => {
      const credentials = { username: 'testuser', password: 'password' };
      mockAxiosInstance.post.mockResolvedValue({ data: { message: 'Login successful', token: 'fake-token' } });

      await ApiService.loginUser(credentials);

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/users/login', credentials);
    });
  });

  describe('Product services', () => {
    it('getProducts should make a GET request to /products', async () => {
      mockAxiosInstance.get.mockResolvedValue({ data: [{ id: 1, name: 'Product 1' }] });

      await ApiService.getProducts();

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/products', { params: {} });
    });

    it('getProducts should pass params correctly', async () => {
      const params = { category: 'electronics' };
      mockAxiosInstance.get.mockResolvedValue({ data: [] });

      await ApiService.getProducts(params);

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/products', { params });
    });

    it('getProductById should make a GET request to /products/:id', async () => {
      const productId = 123;
      mockAxiosInstance.get.mockResolvedValue({ data: { id: productId, name: 'Test Product' } });

      await ApiService.getProductById(productId);

      expect(mockAxiosInstance.get).toHaveBeenCalledWith(`/products/${productId}`);
    });
  });

  describe('Order services', () => {
    it('createOrder should make a POST request to /orders', async () => {
      const orderData = { userId: 1, totalAmount: 100, shippingAddress: '123 Main St' };
      mockAxiosInstance.post.mockResolvedValue({ data: { id: 'order123', ...orderData } });

      await ApiService.createOrder(orderData);

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/orders', orderData);
    });
  });

  // Example for testing interceptor logic (more complex)
  // describe('Axios interceptor', () => {
  //   it('should add Authorization header if token exists in localStorage', async () => {
  //     localStorage.setItem('userToken', 'test-token');
  //     // This test requires deeper inspection of how the interceptor modifies the config.
  //     // Or, you could mock the localStorage and check if it was called.
  //     // For simplicity, this is a conceptual placeholder.
  //     // Actual testing of interceptors might involve checking the config object passed to axios.
  //     const mockRequestUse = axios.create().interceptors.request.use;
  //     expect(mockRequestUse).toHaveBeenCalled();
  //     // More specific assertions would be needed here.
  //   });
  // });
});
