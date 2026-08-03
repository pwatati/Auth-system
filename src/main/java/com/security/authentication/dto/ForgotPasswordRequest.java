package com.security.authentication.dto;

public class ForgotPasswordRequest {
    private Long idNumber;

    public ForgotPasswordRequest() {}
    public ForgotPasswordRequest(Long idNumber) { this.idNumber = idNumber; }

    public Long getIdNumber() { return idNumber; }
    public void setIdNumber(Long idNumber) { this.idNumber = idNumber; }
}