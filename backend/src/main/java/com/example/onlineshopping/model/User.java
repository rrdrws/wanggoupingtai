package com.example.onlineshopping.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // In a real app, this should be hashed

    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    // Consider adding roles for authorization (e.g., ROLE_USER, ROLE_ADMIN)
    // private String roles;
}
