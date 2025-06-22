package com.example.onlineshopping.controller;

import com.example.onlineshopping.model.User;
import com.example.onlineshopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new user (basic registration)
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // Basic validation: check if username or email already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Or a custom error response
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Or a custom error response
        }
        // IMPORTANT: In a real app, hash the password before saving.
        // Example using a (hypothetical) passwordEncoder:
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Update user (basic example)
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(userDetails.getFirstName());
            existingUser.setLastName(userDetails.getLastName());
            existingUser.setAddress(userDetails.getAddress());
            existingUser.setPhone(userDetails.getPhone());
            // Add more fields to update as needed
            // Be careful with updating username, email, password - requires more logic
            return ResponseEntity.ok(userRepository.save(existingUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Basic login endpoint (for demonstration, real auth is more complex)
    // TODO: Implement proper JWT authentication and use Spring Security PasswordEncoder
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginDetails) { // Changed return type
        Optional<User> userOptional = userRepository.findByUsername(loginDetails.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // IMPORTANT: This is a placeholder for password checking.
            // In a real application, you MUST hash passwords and compare hashes (e.g., using BCryptPasswordEncoder).
            // NEVER store or compare plain text passwords in production.
            // Example: if (passwordEncoder.matches(loginDetails.getPassword(), user.getPassword())) { ... }
            if (user.getPassword().equals(loginDetails.getPassword())) {
                // Successful login (in a real app, return a token like JWT and user details)
                // For now, return a simple DTO with ID and username
                LoginResponse response = new LoginResponse(user.getId(), user.getUsername(), "Login successful (token placeholder)");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    // Simple DTO for login response
    static class LoginResponse {
        private Long id;
        private String username;
        private String message; // Or token in a real app
        // Getters and Setters
        public LoginResponse(Long id, String username, String message) {
            this.id = id;
            this.username = username;
            this.message = message;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
