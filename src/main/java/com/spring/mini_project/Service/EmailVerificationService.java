package com.spring.mini_project.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.spring.mini_project.Model.EmailVerification;
import com.spring.mini_project.Model.EmailVerificationEvent;
import com.spring.mini_project.Repo.EmailVerificationRepo;

@Service
public class EmailVerificationService {
    
    private final ApplicationEventPublisher publisher;
    private final EmailVerificationRepo emailRepo;
    public EmailVerificationService(EmailVerificationRepo emailRepo , ApplicationEventPublisher publisher) {
        this.emailRepo = emailRepo;
        this.publisher = publisher;
    }


    public void saveToken(String email){
        
        emailRepo.findByEmail(email).ifPresent(emailRepo::delete);

        String token = UUID.randomUUID().toString();

        EmailVerification newRequest = new EmailVerification();

        newRequest.setEmail(email);
        newRequest.setToken(token);
        newRequest.setExpiry(LocalDateTime.now().plusMinutes(30));

        emailRepo.save(newRequest);

        publisher.publishEvent(new EmailVerificationEvent(email, token));
    }

    public boolean verifyToken(String token){

        return emailRepo.findByToken(token)
                    .map(verify ->
                        {
                            if(verify.getExpiry().isAfter(LocalDateTime.now())){

                                emailRepo.delete(verify);
                                return true;
                            }
                            emailRepo.delete(verify);
                            return false;
                        }
                    ).orElse(false);
    }

    public String getEmailByToken(String token){
        return emailRepo.findByToken(token)
                    .map(EmailVerification::getEmail)
                    .orElse(null);
    }
}
