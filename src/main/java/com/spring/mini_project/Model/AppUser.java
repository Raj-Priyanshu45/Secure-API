package com.spring.mini_project.Model;

import java.time.Instant;
import java.time.LocalDateTime;

import com.spring.mini_project.enums.Provider;
import com.spring.mini_project.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AppUser {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role roles;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private LocalDateTime createdAt;

    private Boolean verified;

    //Refrsh TOken shit
    private String refreshToken;

    private Instant refreshTokenExpiry;

    public AppUser(String email , String username , Role roles , Provider provider , LocalDateTime createdAt){
        this.email = email;
        this.username = username;
        this.roles = roles;
        this.provider = provider;
        this.createdAt = createdAt;
    }

    public AppUser(){}

    public void setEmail(String email){
        this.email = email;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setRoles(Role roles){
        this.roles = roles;
    }

    public void setProvider(Provider provider){
        this.provider = provider;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public void setVerified(Boolean verified){
        this.verified = verified;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void setRefreshTokenExpiry(Instant refreshTokenExpiry){
        this.refreshTokenExpiry= refreshTokenExpiry;
    }

    public Long getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getUsername(){
        return username;
    }

    public Role getRoles(){
        return roles;
    }

    public Provider getProvider(){
        return provider;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public Boolean getVerified(){
        return verified;
    }

    public Instant getRefreshTokenExpiry(){
        return refreshTokenExpiry;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

}

