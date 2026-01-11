package com.spring.mini_project.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.mini_project.JWT.JwtService;
import com.spring.mini_project.Model.AppUser;
import com.spring.mini_project.Model.UserInput;
import com.spring.mini_project.Model.UserResponse;
import com.spring.mini_project.Service.AuthCookieService;
import com.spring.mini_project.Service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class CreateUser {

    private final UserService userService;
    private final AuthCookieService cookieService;
    private final JwtService jwtService;
    
    public CreateUser(UserService userService, AuthCookieService cookieService, JwtService jwtService) {
        this.userService = userService;
        this.cookieService = cookieService;
        this.jwtService = jwtService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserInput input, HttpServletResponse response) {

        UserService.UserRegistrationResult result = userService.buildUser(input);
        AppUser user = result.getUser();
        String rawRefreshToken = result.getRawRefreshToken();

        // Now using the RAW refresh token, not the hashed one
        cookieService.setAuthCookies(response, jwtService.generateToken(user), rawRefreshToken);

        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setCreatedAt(user.getCreatedAt());

        return ResponseEntity.status(201).body(userResponse);
    }

    @GetMapping("/res")
    public String none() {
        return "fuck off";
    }
}