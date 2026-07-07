package com.security.authentication.service;

import com.security.authentication.dto.RegisterRequest;
import com.security.authentication.model.User;
import com.security.authentication.model.VerificationToken;
import com.security.authentication.repository.UserRepository;
import com.security.authentication.repository.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       VerificationTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(RegisterRequest request) {
        if (userRepository.existsById(request.getIdNumber())) {
            throw new RuntimeException("User with this ID Number already exists!");
        }

        User newUser = new User();
        newUser.setIdNumber(request.getIdNumber());
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(newUser);

        String tokenValue = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(tokenValue, newUser);
        tokenRepository.save(verificationToken);

        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + tokenValue;

        return "Registration successful! Click this link to activate your account: " + verificationLink;
    }
}