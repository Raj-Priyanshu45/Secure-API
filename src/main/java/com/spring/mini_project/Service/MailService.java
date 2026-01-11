package com.spring.mini_project.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    
    private final JavaMailSender mailSender;
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendVerificationMail(String to, String token) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Verify your email");
        msg.setText(
            "Use the token below to verify your email:\n\n" +
            token + "\n\n" +
            "This token expires in 30 minutes."
        );

        mailSender.send(msg);
    }
}
