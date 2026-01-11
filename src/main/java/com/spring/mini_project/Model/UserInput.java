package com.spring.mini_project.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserInput {
    
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public UserInput(){}

    public UserInput(String username , String email){
        this.username = username;
        this.email = email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
