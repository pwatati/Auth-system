package com.security.authentication.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    private Long idNumber;

    private String name;
    private String password;
    private String email;

    private boolean enabled = false;
    private String role = "ROLE_USER";

    // Empty constructor for Hibernate
    public User() {
    }

    // Arguments constructor - closes cleanly on line 30
    public User(Long idNumber, String name, String password, String email) {
        this.idNumber = idNumber;
        this.name = name;
        this.password = password;
        this.email = email;
    } // <-- This brace closes the constructor cleanly!

    // --- Getters and Setters live outside the constructor ---

    public Long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Long idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email; // Fixed the 'retuen' typo here
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}