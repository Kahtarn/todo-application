package com.example.servertodo.controller;

import com.example.servertodo.dto.ApiResponse;
import com.example.servertodo.dto.auth.forgotpassword.SendEmailRequest;
import com.example.servertodo.dto.auth.forgotpassword.VerifyOtpRequest;
import com.example.servertodo.dto.auth.login.LoginRequest;
import com.example.servertodo.dto.auth.login.LoginResponse;
import com.example.servertodo.dto.auth.logout.LogoutRequest;
import com.example.servertodo.dto.auth.register.EmailVerifyRequest;
import com.example.servertodo.dto.auth.register.RegisterRequest;
import com.example.servertodo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody SendEmailRequest request) {
        authService.sendEmailToResetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("OTP code was send, check your mail"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody VerifyOtpRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Reset password successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody LogoutRequest request) {
        authService.logout(request);
        return ResponseEntity.ok(ApiResponse.success("Logout successfully"));
    }
}
