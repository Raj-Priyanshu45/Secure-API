package com.spring.mini_project.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.mini_project.Model.EmailVerification;


public interface EmailVerificationRepo extends JpaRepository<EmailVerification, Long>{
    
    Optional<EmailVerification> findByEmail(String email);

    Optional<EmailVerification> findByToken(String token);
}
