package com.spring.mini_project.Model;

import jakarta.validation.constraints.Email;

public class EmailRequest {
    
    @Email
    private String email;

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }
}
