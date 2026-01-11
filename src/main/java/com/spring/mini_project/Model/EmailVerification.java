package com.spring.mini_project.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;

@Entity
public class EmailVerification {
    
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    private LocalDateTime expiry;

    @Email
    private String email;

    public EmailVerification(){}

    public void setToken(String token){
        this.token = token;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setExpiry(LocalDateTime expiry){
        this.expiry = expiry;
    }

    public Long getId(){
        return id;
    }

    public String getToken(){
        return token;
    }

    public LocalDateTime getExpiry(){
        return expiry;
    }

    public String getEmail(){
        return email;
    }
}
