package com.spring.mini_project.Model;

import jakarta.validation.constraints.NotBlank;

public class TokenRequest {
    
    @NotBlank(message="Token can't be blank")
    private String token;

    public void setToken(String token){
        this.token = token;
    } 

    public String getToken(){return token; }
}
