package com.spring.mini_project.Model;

public class EmailVerificationEvent {

    private final String email;
    private final String token;

    public EmailVerificationEvent(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() { return email; }
    public String getToken() { return token; }
}

