package com.security.authentication.dto;

public class LoginSuccessResponse {
    private final String status = "success";
    private String message;
    private String accessToken;
    private final String tokenType = "Bearer";
    private final long expiresIn = 900;
    private UserDetails user;

    public LoginSuccessResponse(String message, String accessToken, UserDetails user) {
        this.message = message;
        this.accessToken = accessToken;
        this.user = user;
    }


    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
    public long getExpiresIn() { return expiresIn; }
    public UserDetails getUser() { return user; }
}