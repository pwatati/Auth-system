package com.security.authentication.dto;

public class LoginRequest {
    private Long idNumber;
    private String password;


    public LoginRequest() {}

    public LoginRequest(Long idNumber, String password) {
        this.idNumber = idNumber;
        this.password = password;
    }

    public Long getIdNumber() { return idNumber; }
    public void setIdNumber(Long idNumber) { this.idNumber = idNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}