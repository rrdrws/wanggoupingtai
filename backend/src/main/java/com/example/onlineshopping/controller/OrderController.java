package com.example.onlineshopping.controller;

import com.example.onlineshopping.model.Order;
import com.example.onlineshopping.model.User;
import com.example.onlineshopping.repository.OrderRepository;
import com.example.onlineshopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository; // To fetch user for order creation

    // Get all orders (potentially admin only)
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get orders for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build(); // User not found
        }
        List<Order> orders = orderRepository.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    // Create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Optional<User> userOptional = userRepository.findById(orderRequest.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // User not found
        }

        Order order = new Order();
        order.setUser(userOptional.get());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING"); // Default status
        order.setTotalAmount(orderRequest.getTotalAmount()); // This would typically be calculated based on items
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setBillingAddress(orderRequest.getBillingAddress());
        // In a real app, you'd create OrderItem entities here based on cart contents

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    // Update order status (typically admin or system function)
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            return ResponseEntity.ok(orderRepository.save(order));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // (Simplified DTO for order creation request)
    static class OrderRequest {
        private Long userId;
        private java.math.BigDecimal totalAmount;
        private String shippingAddress;
        private String billingAddress;
        // Getters and Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public java.math.BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(java.math.BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public String getShippingAddress() { return shippingAddress; }
        public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
        public String getBillingAddress() { return billingAddress; }
        public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    }
}
