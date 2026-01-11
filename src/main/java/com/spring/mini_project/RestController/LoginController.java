package com.spring.mini_project.RestController;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.mini_project.Service.UserService;


@RestController
public class LoginController {

    private final UserService userService;
    

    public LoginController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/")
    public String greet(){
        return "Hello";     
    }

    @GetMapping("/tell")
    public String hello(){
        return "fuck ur mother";     
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutEnpoint(Principal principal){

        String user = principal.getName();

        userService.deleteToken(user);
        return ResponseEntity.noContent().build();
    }
    
}
