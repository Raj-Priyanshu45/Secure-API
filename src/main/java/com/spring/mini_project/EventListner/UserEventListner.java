package com.spring.mini_project.EventListner;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.spring.mini_project.Model.EmailVerificationEvent;
import com.spring.mini_project.Service.MailService;

@Component
public class UserEventListner {
    
    private final MailService mailService;
    public UserEventListner(MailService mailService){
        this.mailService = mailService;
    }

    @EventListener
    public void onEmailVerification(EmailVerificationEvent event) {
        mailService.sendVerificationMail(
            event.getEmail(),
            event.getToken()
        );
    }
}
