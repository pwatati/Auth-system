package com.security.authentication.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    private Long idNumber;

    private String name;
    private String password;
    private String email;

    private boolean enabled = false;

    @Column(name = "roles", nullable = false)
    private String roles = "ROLE_USER";

    public User() {
    }

    public User(Long idNumber, String name, String password, String email) {
        this.idNumber = idNumber;
        this.name = name;
        this.password = password;
        this.email = email;
        this.enabled = false;
        this.roles = "ROLE_USER";
    }
}