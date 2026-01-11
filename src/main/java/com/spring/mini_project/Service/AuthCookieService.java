package com.spring.mini_project.Service;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.spring.mini_project.JWT.Config;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthCookieService {

    private final Config config;
    public AuthCookieService(Config config){
        this.config = config;
    }
    
    public void setAuthCookies(HttpServletResponse response
        ,String accessToken
        ,String refreshToken
    ){


        @SuppressWarnings("null")
        ResponseCookie access = ResponseCookie.from("JWT",accessToken)
                                    .httpOnly(true)
                                    .secure(false)
                                    .path("/")
                                    .maxAge(Duration.ofMillis(config.getExpiration()))
                                    .sameSite("Lax")
                                    .build();

        @SuppressWarnings("null")
        ResponseCookie refresh = ResponseCookie.from("REFRESH", refreshToken)
                                        .httpOnly(true)
                                        .secure(false)
                                        .path("/auth/")
                                        .maxAge(Duration.ofDays(7))
                                        .sameSite("Lax")
                                        .build();

        response.addHeader("Set-Cookie", access.toString());
        response.addHeader("Set-Cookie", refresh.toString());
    }
}
