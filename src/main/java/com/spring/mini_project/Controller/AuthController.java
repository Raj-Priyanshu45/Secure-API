package com.spring.mini_project.Controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.mini_project.Model.TokenRequest;
import com.spring.mini_project.JWT.JwtService;
import com.spring.mini_project.Model.AppUser;
import com.spring.mini_project.Model.EmailRequest;
import com.spring.mini_project.Model.MessageResponse;
import com.spring.mini_project.Repo.EmailVerificationRepo;
import com.spring.mini_project.Repo.UserRepo;
import com.spring.mini_project.Service.AuthCookieService;
import com.spring.mini_project.Service.EmailVerificationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final AuthCookieService authCookieService;
    private final com.spring.mini_project.JWT.Filter filter;
    private final EmailVerificationService emailVerificationService;
   
    public AuthController(JwtService jwtService , UserRepo userRepo ,
         AuthCookieService authCookieService ,
         EmailVerificationService emailVerificationService,
          com.spring.mini_project.JWT.Filter filter , EmailVerificationRepo emailRepo){
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.authCookieService = authCookieService;
        this.filter = filter;
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/")
    public void refresh(HttpServletRequest request, HttpServletResponse response) {

        

        String rawRefresh = filter.extractCookies(request , "REFRESH");

        
        if (rawRefresh == null) {
            throw new RuntimeException("Missing refresh token");
        }

        String hashed = jwtService.hashRefreshToken(rawRefresh);

        AppUser user = userRepo.findByRefreshToken(hashed)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (user.getRefreshTokenExpiry().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        // ROTATE
        String newRaw = jwtService.generateRefreshTokenRaw();
        String newHashed = jwtService.hashRefreshToken(newRaw);

        user.setRefreshToken(newHashed);
        user.setRefreshTokenExpiry(Instant.now().plus(7, ChronoUnit.DAYS));
        userRepo.save(user);

        String newAccess = jwtService.generateToken(user);
        authCookieService.setAuthCookies(response, newAccess, newRaw);
    }

    @PostMapping("send-verification")
    public ResponseEntity<MessageResponse> sendVerificationCode(@Valid @RequestBody EmailRequest emailRequest){

        AppUser user = userRepo.findByEmail(emailRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));   
        
        if(user.getVerified()){

            MessageResponse response = new MessageResponse("User is already verified");

            return ResponseEntity.badRequest().body(response);
        }

        emailVerificationService.saveToken(emailRequest.getEmail());

        return ResponseEntity.ok(new MessageResponse("Verification email sent successfully"));
    }

    @PostMapping("verify-token")
    public ResponseEntity<MessageResponse> verifyEmail(@Valid @RequestBody TokenRequest request){

        String email = emailVerificationService.getEmailByToken(request.getToken());

        if(email == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token or Expired"));
        }
        
        boolean isValid = emailVerificationService.verifyToken(request.getToken());

        if(!isValid){
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Invalid or expired token"));
        }

        AppUser user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setVerified(true);
        userRepo.save(user);

        return ResponseEntity.ok(new MessageResponse("Email verified successfully"));
    }

}
