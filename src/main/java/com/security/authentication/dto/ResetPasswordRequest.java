package com.security.authentication.dto;

public class ResetPasswordRequest {
    private Long idNumber;
    private String code;
    private String newPassword;

    public ResetPasswordRequest() {}

    public Long getIdNumber() { return idNumber; }
    public void setIdNumber(Long idNumber) { this.idNumber = idNumber; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}