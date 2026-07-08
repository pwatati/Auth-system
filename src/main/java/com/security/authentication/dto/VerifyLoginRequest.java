package com.security.authentication.dto;

public class VerifyLoginRequest {
    private Long idNumber;
    private String code;

    // Constructors
    public VerifyLoginRequest() {}

    public VerifyLoginRequest(Long idNumber, String code) {
        this.idNumber = idNumber;
        this.code = code;
    }

    // Getters and Setters
    public Long getIdNumber() { return idNumber; }
    public void setIdNumber(Long idNumber) { this.idNumber = idNumber; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
