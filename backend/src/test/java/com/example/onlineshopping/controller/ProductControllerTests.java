package com.example.onlineshopping.controller;

import com.example.onlineshopping.model.Product;
import com.example.onlineshopping.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest(ProductController.class) // Focus only on ProductController, mocking dependencies
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mocks ProductRepository, Spring Boot will inject this mock
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper; // For converting objects to JSON strings

    @Test
    public void getAllProducts_shouldReturnListOfProducts() throws Exception {
        Product product1 = new Product(1L, "Laptop Pro", "High-end laptop", new BigDecimal("1200.00"), "Electronics", "img1.jpg", 10);
        Product product2 = new Product(2L, "Coffee Maker", "Makes great coffee", new BigDecimal("79.99"), "Appliances", "img2.jpg", 25);
        List<Product> allProducts = Arrays.asList(product1, product2);

        given(productRepository.findAll()).willReturn(allProducts);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(product1.getName())))
                .andExpect(jsonPath("$[1].name", is(product2.getName())));
    }

    @Test
    public void getProductById_whenProductExists_shouldReturnProduct() throws Exception {
        Product product = new Product(1L, "Laptop Pro", "High-end laptop", new BigDecimal("1200.00"), "Electronics", "img1.jpg", 10);
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(product.getName())));
    }

    @Test
    public void getProductById_whenProductDoesNotExist_shouldReturnNotFound() throws Exception {
        given(productRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createProduct_shouldReturnCreatedProduct() throws Exception {
        Product productToCreate = new Product(null, "New Gadget", "Brand new gadget", new BigDecimal("99.99"), "Tech", "new.jpg", 50);
        Product savedProduct = new Product(1L, "New Gadget", "Brand new gadget", new BigDecimal("99.99"), "Tech", "new.jpg", 50);

        given(productRepository.save(any(Product.class))).willReturn(savedProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(savedProduct.getName())));
    }

    @Test
    public void updateProduct_whenProductExists_shouldReturnUpdatedProduct() throws Exception {
        Long productId = 1L;
        Product existingProduct = new Product(productId, "Old Name", "Old desc", BigDecimal.TEN, "OldCat", "old.jpg", 5);
        Product updatedDetails = new Product(null, "Updated Name", "Updated desc", BigDecimal.ONE, "NewCat", "new.jpg", 10);
        Product savedProductAfterUpdate = new Product(productId, "Updated Name", "Updated desc", BigDecimal.ONE, "NewCat", "new.jpg", 10);

        given(productRepository.findById(productId)).willReturn(Optional.of(existingProduct));
        given(productRepository.save(any(Product.class))).willReturn(savedProductAfterUpdate);

        mockMvc.perform(put("/api/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedDetails.getName())))
                .andExpect(jsonPath("$.description", is(updatedDetails.getDescription())));
    }

    @Test
    public void updateProduct_whenProductDoesNotExist_shouldReturnNotFound() throws Exception {
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());
        Product updatedDetails = new Product(null, "Updated Name", "Updated desc", BigDecimal.ONE, "NewCat", "new.jpg", 10);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProduct_whenProductExists_shouldReturnNoContent() throws Exception {
        given(productRepository.existsById(1L)).willReturn(true);
        // BDDMockito.doNothing().when(productRepository).deleteById(1L); // For void methods

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteProduct_whenProductDoesNotExist_shouldReturnNotFound() throws Exception {
        given(productRepository.existsById(1L)).willReturn(false);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    // TODO: Add tests for filtering (e.g., get /api/products?category=Electronics)
}
