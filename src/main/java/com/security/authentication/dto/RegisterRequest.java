package com.security.authentication.dto;

public class RegisterRequest {
    private Long idNumber;
    private String name;
    private String password;
    private String email;


    public RegisterRequest() {}

    public RegisterRequest(Long idNumber, String name, String password, String email) {
        this.idNumber = idNumber;
        this.name = name;
        this.password = password;
        this.email = email;
    }


    public Long getIdNumber() { return idNumber; }
    public void setIdNumber(Long idNumber) { this.idNumber = idNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}