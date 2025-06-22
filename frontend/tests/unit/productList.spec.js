// tests/unit/productList.spec.js
import { shallowMount } from '@vue/test-utils';
import ProductList from '@/views/ProductList.vue';
import { useCartStore } from '@/stores/cart'; // Pinia store
import ApiService from '@/services/ApiService';

// Mock Pinia store
jest.mock('@/stores/cart', () => ({
  useCartStore: jest.fn(() => ({
    addItem: jest.fn(),
    // Mock other getters/actions if ProductList uses them directly
  }))
}));

// Mock ApiService
jest.mock('@/services/ApiService', () => ({
  getProducts: jest.fn(),
}));

// Mock Vue Router (if component uses $router or <router-link>)
const mockRouter = {
  push: jest.fn(),
};

describe('ProductList.vue', () => {
  let wrapper;
  const mockProducts = [
    { id: 1, name: 'Product A', price: 100, description: 'Desc A', imageUrl: 'imgA.jpg' },
    { id: 2, name: 'Product B', price: 200, description: 'Desc B', imageUrl: 'imgB.jpg' },
  ];

  beforeEach(async () => {
    // Reset mocks before each test
    ApiService.getProducts.mockClear();
    useCartStore().addItem.mockClear();

    // Mock successful API response
    ApiService.getProducts.mockResolvedValue({ data: mockProducts });

    wrapper = shallowMount(ProductList, {
      global: {
        mocks: {
          $router: mockRouter // Mock router if needed
        },
        stubs: ['router-link'] // Stub router-link to avoid warnings if not deeply testing navigation
      }
    });
    // Wait for component to finish created hook / async operations
    await wrapper.vm.$nextTick(); // For created hook
    await wrapper.vm.$nextTick(); // Additional tick if there are chained promises
  });

  it('renders product list heading', () => {
    expect(wrapper.find('h1').text()).toBe('Products');
  });

  it('fetches products on created', () => {
    expect(ApiService.getProducts).toHaveBeenCalledTimes(1);
  });

  it('displays products after fetching', async () => {
    // Ensure component has updated after fetch
    expect(wrapper.vm.products.length).toBe(mockProducts.length);
    const productCards = wrapper.findAll('.product-card');
    expect(productCards.length).toBe(mockProducts.length);
    expect(productCards[0].find('h2').text()).toBe(mockProducts[0].name);
  });

  it('displays loading message initially', async () => {
    ApiService.getProducts.mockReturnValue(new Promise(() => {})); // Simulate pending promise
    const localWrapper = shallowMount(ProductList, {
        global: { stubs: ['router-link'] }
    });
    expect(localWrapper.find('.loading').exists()).toBe(true);
  });

  it('displays error message if fetching products fails', async () => {
    ApiService.getProducts.mockRejectedValue(new Error('Network Error'));
    const localWrapper = shallowMount(ProductList, {
        global: { stubs: ['router-link'] }
    });
    await localWrapper.vm.$nextTick(); // created
    await localWrapper.vm.$nextTick(); // fetchProducts error handling
    expect(localWrapper.find('.error-message').exists()).toBe(true);
    expect(localWrapper.find('.error-message').text()).toContain('Failed to load products');
  });

  it('calls cartStore.addItem when "Add to Cart" is clicked', async () => {
    // Assuming products are loaded
    await wrapper.vm.$nextTick(); // Wait for product rendering
    const addToCartButton = wrapper.find('.product-card .btn-primary');
    expect(addToCartButton.exists()).toBe(true); // Ensure button is found

    await addToCartButton.trigger('click');

    expect(useCartStore().addItem).toHaveBeenCalledWith(mockProducts[0]);
  });

  it('calls fetchProducts with filters when applyFilters is called', async () => {
    wrapper.vm.searchName = 'Test';
    await wrapper.vm.applyFilters();
    expect(ApiService.getProducts).toHaveBeenCalledWith({ name: 'Test' });
  });

   it('clears filters and fetches all products when clearFilters is called', async () => {
    wrapper.vm.searchName = 'Test'; // Set some filter
    await wrapper.vm.clearFilters();
    expect(wrapper.vm.searchName).toBe('');
    expect(wrapper.vm.searchCategory).toBe('');
    // It should have been called once on created, then once for clearFilters
    expect(ApiService.getProducts).toHaveBeenCalledTimes(2); // or more specific check for params
    expect(ApiService.getProducts).toHaveBeenLastCalledWith();
  });

  // Add more tests for:
  // - Correct rendering of product details (price, image, description)
  // - Functionality of search/filter inputs
  // - "View Details" button navigation (would require more router mocking or integration test)
  // - Empty state when no products are found
});
