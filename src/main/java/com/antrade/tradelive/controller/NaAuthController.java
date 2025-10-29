package com.antrade.tradelive.controller;

import com.antrade.tradelive.dto.HelloDTO;
import com.antrade.tradelive.entity.na_auth;
import com.antrade.tradelive.service.NaAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import com.antrade.tradelive.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // Allow CORS for testing; restrict in production
public class NaAuthController {

    @Autowired
    private NaAuthService naAuthService;

    @Autowired
    private JwtUtil jwtUtil;
    // Register a new user
    @PostMapping("/register")
    
    public ResponseEntity<?> register(@RequestBody na_auth user) {
        try {
//        	return ResponseEntity.ok("iii know what to do?");
            na_auth savedUser = naAuthService.createUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", savedUser.getId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Login (authenticate) a user
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String identifier = credentials.get("identifier");
        String password = credentials.get("password");
        if (identifier == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Identifier and password required"));
        }
        Optional<na_auth> userOpt = naAuthService.authenticateUser(identifier, password);
        if (userOpt.isPresent()) {
            na_auth user = userOpt.get();
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities("USER")
                    .build();
            String jwtToken = jwtUtil.generateToken(userDetails);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", jwtToken);  // Return token
            response.put("userId", user.getId());
            response.put("name", user.getName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    // Get user by ID (example GET)
    @GetMapping("/user/{id}")
//    public ResponseEntity<?> getUserById(@PathVariable Long id) {
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        Optional<na_auth> user = naAuthService.getUserById(id);
//        return ResponseEntity.ok("uuuu");
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}