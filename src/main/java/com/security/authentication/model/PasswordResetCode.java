package com.security.authentication.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_codes")
public class PasswordResetCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private Long idNumber;
    private LocalDateTime expiryDate;

    public PasswordResetCode() {}

    public PasswordResetCode(String code, Long idNumber) {
        this.code = code;
        this.idNumber = idNumber;
        this.expiryDate = LocalDateTime.now().plusMinutes(3);
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public Long getIdNumber() { return idNumber; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
}