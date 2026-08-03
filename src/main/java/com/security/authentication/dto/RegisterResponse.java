package com.security.authentication.dto;

public class RegisterResponse {
    private final String status = "success";
    private String message;
    private final String step = "AWAITING_ACTIVATION";
    private Long idNumber;
    private String name;
    private String maskedEmail;
    private final int linkExpiryMinutes = 15;

    public RegisterResponse(String message, Long idNumber, String name, String email) {
        this.message = message;
        this.idNumber = idNumber;
        this.name = name;
        this.maskedEmail = maskEmail(email);
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "******";
        String[] parts = email.split("@");
        if (parts[0].length() <= 2) return parts[0].charAt(0) + "****@" + parts[1];
        return parts[0].charAt(0) + "****" + parts[0].charAt(parts[0].length() - 1) + "@" + parts[1];
    }


    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public String getStep() { return step; }
    public Long getIdNumber() { return idNumber; }
    public String getName() { return name; }
    public String getMaskedEmail() { return maskedEmail; }
    public int getLinkExpiryMinutes() { return linkExpiryMinutes; }
}