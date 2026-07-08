package com.security.authentication.service;

import com.security.authentication.dto.RegisterRequest;
import com.security.authentication.model.User;
import com.security.authentication.model.VerificationToken;
import com.security.authentication.repository.UserRepository;
import com.security.authentication.repository.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.security.authentication.dto.LoginRequest;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import com.security.authentication.model.LoginCode;
import com.security.authentication.repository.LoginCodeRepository;
import java.util.Random;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginCodeRepository loginCodeRepository; // Added for 2FA tracking

    public AuthService(UserRepository userRepository,
                       VerificationTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder,
                       LoginCodeRepository loginCodeRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginCodeRepository = loginCodeRepository;
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

    public String verifyUserToken(String tokenValue) {
        VerificationToken verificationToken = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid activation token link!"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken);
            throw new RuntimeException("This activation link has expired. Please register again.");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken);

        return "Account verified and activated successfully! You can now log in.";
    }

    @Transactional
    public String loginUser(LoginRequest request) {
        // 1. Validate credentials
        User user = userRepository.findById(request.getIdNumber())
                .orElseThrow(() -> new RuntimeException("Invalid ID Number or Password!"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account is not verified yet! Please check your verification link.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid ID Number or Password!");
        }


        loginCodeRepository.deleteByIdNumber(user.getIdNumber());


        String sixDigitCode = generateSixDigitCode();
        LoginCode loginCode = new LoginCode(sixDigitCode, user.getIdNumber());
        loginCodeRepository.save(loginCode);


        System.out.println(">>> 2FA CODE SENT TO EMAIL [" + user.getEmail() + "]: " + sixDigitCode);

        return " Verified! A 6-digit login verification code has been dispatched to your email (" + user.getEmail() + ").";
    }

    @Transactional
    public String verifyLoginCode(Long idNumber, String submittedCode) {

        LoginCode loginCode = loginCodeRepository.findByIdNumberAndCode(idNumber, submittedCode)
                .orElseThrow(() -> new RuntimeException("Invalid verification code! Please try again."));


        if (loginCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            loginCodeRepository.delete(loginCode);
            throw new RuntimeException("This verification code has expired . Please log in again.");
        }


        loginCodeRepository.delete(loginCode);


        User user = userRepository.findById(idNumber).get();
        return " Verification Successful! Welcome " + user.getName() + ".";
    }

    private String generateSixDigitCode() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }
}