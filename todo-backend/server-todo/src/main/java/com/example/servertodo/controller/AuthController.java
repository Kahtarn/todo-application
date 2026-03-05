package com.example.servertodo.controller;

import com.example.servertodo.dto.ApiResponse;
import com.example.servertodo.dto.auth.login.LoginRequest;
import com.example.servertodo.dto.auth.login.LoginResponse;
import com.example.servertodo.dto.auth.register.EmailVerifyRequest;
import com.example.servertodo.dto.auth.register.RegisterRequest;
import com.example.servertodo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Register successfully. Please verify your email."));
    }

    @PostMapping("/verify-register")
    public ResponseEntity<ApiResponse<String>> verifyRegister(@Valid @RequestBody EmailVerifyRequest request) {
        authService.verify(request);
        return ResponseEntity.ok(ApiResponse.success("Verify successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successfully", response));
    }
}
