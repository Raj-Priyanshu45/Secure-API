package com.spring.mini_project.OAuth2;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.spring.mini_project.JWT.JwtService;
import com.spring.mini_project.Model.AppUser;
import com.spring.mini_project.Repo.UserRepo;
import com.spring.mini_project.Service.AuthCookieService;
import com.spring.mini_project.enums.Provider;
import com.spring.mini_project.enums.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginHandler extends SimpleUrlAuthenticationSuccessHandler{
    
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final AuthCookieService cookieService;
    public OAuth2LoginHandler(JwtService jwtService , UserRepo userRepo , AuthCookieService cookieService){
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.cookieService = cookieService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request , 
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException
    {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String rawRefresh = jwtService.generateRefreshTokenRaw();
        String hashedRefresh = jwtService.hashRefreshToken(rawRefresh);

        String email = user.getAttribute("email");
        String provider = authentication
                                .getAuthorities()
                                .toString()
                                .contains("github")
                                ? "GITHUB" : "GOOGLE";

        AppUser newUser = userRepo.findByEmail(email)
                            .orElseGet(() ->{
                                AppUser newOne = new AppUser();

                                newOne.setUsername(extractNameByEmail(email));
                                newOne.setEmail(email);
                                newOne.setProvider(Provider.valueOf(provider));
                                newOne.setVerified(true);
                                newOne.setRoles(Role.USER);
                                newOne.setRefreshToken(hashedRefresh);
                                newOne.setCreatedAt(LocalDateTime.now());
                                newOne.setRefreshTokenExpiry(Instant.now().plus(7, ChronoUnit.DAYS));
                                return userRepo.save(newOne);
                            });

        String accessToken = jwtService.generateToken(newUser);

        cookieService.setAuthCookies(response, accessToken, rawRefresh);
                                
    }

    private String extractNameByEmail(String email){

        int i = 0;
        if(email == null || email.equals("")){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        while(email.charAt(i) != '@'){
            sb.append(email.charAt(i));
            i++;
        }

        return sb.toString();
    }

}
