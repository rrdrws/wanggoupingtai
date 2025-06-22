package com.example.onlineshopping.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String status; // e.g., PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELED

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private String shippingAddress;
    private String billingAddress;

    // In a more complex scenario, you'd have an OrderItem entity
    // to store details of each product in the order.
    // For simplicity, we might store a summary or skip detailed items for now.
    // @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<OrderItem> orderItems;
}
