package com.security.authentication.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "User")
public class User {

    @Id
    private Long idNumber;

    private String name;
    private String password;
    private String email;

    private boolean enabled = false;

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
           name = "user_roles",
   joinColumns = @JoinColumn(name = "user_id_number"),
   inverseJoinColumns = @JoinColumn(name = "role_id"))
   private Set<Role> roles = new HashSet<>();

    public User() {
    }


    public User(Long idNumber, String name, String password, String email) {
        this.idNumber = idNumber;
        this.name = name;
        this.password = password;
        this.enabled = false;
        this.email = email;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}