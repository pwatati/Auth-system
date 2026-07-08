package com.security.authentication.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_codes")
public class LoginCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private Long idNumber;
    private LocalDateTime expiryDate;


    public LoginCode() {}

    public LoginCode(String code, Long idNumber) {
        this.code = code;
        this.idNumber = idNumber;
        this.expiryDate = LocalDateTime.now().plusMinutes(5); // 5-minute window to type the code
    }


    public Long getId() { return id; }
    public String getCode() { return code; }
    public Long getIdNumber() { return idNumber; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
}