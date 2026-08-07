package com.security.authentication.dto;

import com.security.authentication.model.Role;
import java.util.Set;

public class UserDetails {

    private Long idNumber;
    private String name;
    private String email;
    private Set<String> roles;

    public UserDetails() {
    }

    public UserDetails(Long idNumber, String name, String email, Set<String> roles) {
        this.idNumber = idNumber;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}