package com.spring.mini_project.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.mini_project.JWT.JwtService;
import com.spring.mini_project.Model.AppUser;
import com.spring.mini_project.Model.UserInput;
import com.spring.mini_project.Repo.UserRepo;
import com.spring.mini_project.enums.Provider;
import com.spring.mini_project.enums.Role;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepo userRepo, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // Change return type to include both raw and user
    public UserRegistrationResult buildUser(UserInput input) {
        AppUser user = new AppUser();
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setProvider(Provider.LOCAL);
        user.setRoles(Role.USER);
        user.setVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        
        String rawRefresh = jwtService.generateRefreshTokenRaw();
        String hashedRefresh = jwtService.hashRefreshToken(rawRefresh);

        user.setRefreshToken(hashedRefresh);
        user.setRefreshTokenExpiry(Instant.now().plus(7, ChronoUnit.DAYS));
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        AppUser savedUser = userRepo.save(user);
        
        return new UserRegistrationResult(savedUser, rawRefresh);
    }

    // Inner class to hold both the user and raw refresh token
    public static class UserRegistrationResult {
        private final AppUser user;
        private final String rawRefreshToken;

        public UserRegistrationResult(AppUser user, String rawRefreshToken) {
            this.user = user;
            this.rawRefreshToken = rawRefreshToken;
        }

        public AppUser getUser() {
            return user;
        }

        public String getRawRefreshToken() {
            return rawRefreshToken;
        }
    }

    public void deleteToken(String username){

        userRepo.clearRefreshToken(username);

        userRepo.clearRefreshToken(username);
    }
}