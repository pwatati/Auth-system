package com.security.authentication.dto;

public class ErrorResponse {
    private final String status = "error";
    private String message;
    private String errorType;

    public ErrorResponse(String message, String errorType) {
        this.message = message;
        this.errorType = errorType;
    }


    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public String getErrorType() { return errorType; }
}