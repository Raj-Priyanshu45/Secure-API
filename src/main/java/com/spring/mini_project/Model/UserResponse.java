package com.spring.mini_project.Model;

import java.time.LocalDateTime;

public class UserResponse {

    private String email;

    private String username;

    private LocalDateTime createdAt;

    public UserResponse(){}

    public UserResponse(String username , String email , LocalDateTime createdAt){
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public String getEmail(){
        return email;
    }

    public String getUsername(){
        return username;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
}
