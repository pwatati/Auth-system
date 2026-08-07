package com.security.authentication.service;

import com.security.authentication.dto.*;
import com.security.authentication.model.*;
import com.security.authentication.repository.*;
import com.security.authentication.config.Security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginCodeRepository loginCodeRepository;
    private final PasswordResetCodeRepository resetCodeRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       VerificationTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder,
                       LoginCodeRepository loginCodeRepository,
                       PasswordResetCodeRepository resetCodeRepository,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginCodeRepository = loginCodeRepository;
        this.resetCodeRepository = resetCodeRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) {
        if (userRepository.existsById(request.getIdNumber())) {
            throw new RuntimeException("User with this ID Number already exists!");
        }

        User newUser = new User();
        newUser.setIdNumber(request.getIdNumber());
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        // Automatically defaults to "ROLE_USER" inside the entity, or set explicitly:
        newUser.setRoles("ROLE_USER");

        // Save immediately to MySQL
        userRepository.save(newUser);

        String tokenValue = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(tokenValue, newUser);
        tokenRepository.save(verificationToken);

        String verificationLink = "http://localhost:8080/api/auth/verify?token=" + tokenValue;
        System.out.println(">>> ACTIVATION LINK DISPATCHED: " + verificationLink);

        return new RegisterResponse(
                "Registration successful! Please check your email to activate your account.",
                newUser.getIdNumber(),
                newUser.getName(),
                newUser.getEmail()
        );
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
    public LoginInitialResponse loginUser(LoginRequest request) {
        User user = userRepository.findById(request.getIdNumber())
                .orElseThrow(() -> new RuntimeException("Invalid ID Number or Password!"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account is not verified yet! Please check your verification link in your email.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid ID Number or Password!");
        }

        loginCodeRepository.deleteByIdNumber(user.getIdNumber());

        String sixDigitCode = generateSixDigitCode();
        LoginCode loginCode = new LoginCode(sixDigitCode, user.getIdNumber());
        loginCodeRepository.save(loginCode);

        System.out.println(">>> 2FA CODE SENT TO EMAIL [" + user.getEmail() + "]: " + sixDigitCode);

        return new LoginInitialResponse(
                "Credentials verified! A 6-digit verification code has been generated.",
                user.getIdNumber(),
                user.getEmail()
        );
    }

    @Transactional
    public LoginSuccessResponse verifyLoginCode(Long idNumber, String submittedCode) {
        LoginCode loginCode = loginCodeRepository.findByIdNumberAndCode(idNumber, submittedCode)
                .orElseThrow(() -> new RuntimeException("Invalid verification code! Please try again."));

        if (loginCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            loginCodeRepository.delete(loginCode);
            throw new RuntimeException("This verification code has expired. Please log in again.");
        }

        loginCodeRepository.delete(loginCode);
        User user = userRepository.findById(idNumber)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Pass roles as simple String to JWT generator
        String token = jwtUtil.generateToken(user.getIdNumber(), user.getName(), user.getRoles());

        Set<String> roleNames = Collections.singleton(user.getRoles());

        UserDetails userDto = new UserDetails(user.getIdNumber(), user.getName(), user.getEmail(), roleNames);

        return new LoginSuccessResponse("Verification successful. Welcome!", token, userDto);
    }

    @Transactional
    public String initiatePasswordReset(ForgotPasswordRequest request) {
        User user = userRepository.findById(request.getIdNumber())
                .orElseThrow(() -> new RuntimeException("No account registered with this ID Number!"));

        resetCodeRepository.deleteByIdNumber(user.getIdNumber());

        String resetCodeStr = generateSixDigitCode();
        PasswordResetCode resetCodeEntity = new PasswordResetCode(resetCodeStr, user.getIdNumber());
        resetCodeRepository.save(resetCodeEntity);

        System.out.println(">>> PASSWORD RESET CODE SENT TO [" + user.getEmail() + "]: " + resetCodeStr);
        return "A password reset verification code has been dispatched to your email (" + user.getEmail() + ").";
    }

    @Transactional
    public String completePasswordReset(ResetPasswordRequest request) {
        PasswordResetCode resetCode = resetCodeRepository.findByIdNumberAndCode(request.getIdNumber(), request.getCode())
                .orElseThrow(() -> new RuntimeException("Invalid password reset code! Verification failed."));

        if (resetCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            resetCodeRepository.delete(resetCode);
            throw new RuntimeException("This reset code has expired. Please try again.");
        }

        resetCodeRepository.delete(resetCode);

        User user = userRepository.findById(request.getIdNumber())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Verification successful. Password reset successful!";
    }

    private String generateSixDigitCode() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }
}