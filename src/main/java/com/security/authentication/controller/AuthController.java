package com.security.authentication.controller;

import com.security.authentication.dto.*;
import com.security.authentication.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = authService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {

            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage(), "BAD_REQUEST"));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam String token) {
        try {
            String message = authService.verifyUserToken(token);

            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {

            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage(), "INVALID_TOKEN"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginInitialResponse response = authService.loginUser(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {

            return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage(), "UNAUTHORIZED"));
        }
    }

    @PostMapping("/verify-login")
    public ResponseEntity<?> verifyLogin(@RequestBody VerifyLoginRequest request) {
        try {
            LoginSuccessResponse response = authService.verifyLoginCode(request.getIdNumber(), request.getCode());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {

            return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage(), "UNAUTHORIZED"));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            String message = authService.initiatePasswordReset(request);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {

            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage(), "NOT_FOUND"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            String message = authService.completePasswordReset(request);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {

            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage(), "BAD_REQUEST"));
        }
    }
}