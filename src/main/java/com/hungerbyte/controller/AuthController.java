package com.hungerbyte.controller;

import com.hungerbyte.dto.ApiResponse;
import com.hungerbyte.dto.AuthRequest;
import com.hungerbyte.dto.AuthResponse;
import com.hungerbyte.dto.RegisterRequest;
import com.hungerbyte.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok("Registration successful! Welcome to HungerByte.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful!", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully"));
    }
}
