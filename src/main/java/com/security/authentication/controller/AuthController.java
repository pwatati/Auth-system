package com.security.authentication.controller;

import com.security.authentication.dto.LoginRequest;
import com.security.authentication.dto.RegisterRequest;
import com.security.authentication.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.security.authentication.dto.LoginRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String message = authService.registerUser(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        try {
            String result = authService.verifyUserToken(token);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            String message = authService.loginUser(request);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    @PostMapping("/verify-login")
    public ResponseEntity<String> verifyLogin(@RequestBody com.security.authentication.dto.VerifyLoginRequest request) {
        try {
            String message = authService.verifyLoginCode(request.getIdNumber(), request.getCode());
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}