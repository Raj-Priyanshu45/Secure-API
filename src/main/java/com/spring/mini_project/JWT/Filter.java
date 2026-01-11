package com.spring.mini_project.JWT;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Filter extends OncePerRequestFilter{
    
    private final SecretKey secret;
    public Filter(Config config){
        this.secret = Keys.hmacShaKeyFor(
            Base64.getDecoder().decode(config.getSecret()));
    }

    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request,
                                    @SuppressWarnings("null") HttpServletResponse response, 
                                    @SuppressWarnings("null") FilterChain filterChain) throws ServletException, IOException {
        
        

        String token = extractCookies(request , "JWT");

        if(token == null){
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("JWT COOKIE = " + token);


        try{
            Claims claims = Jwts.parser()
                            .verifyWith(secret)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();


            
            String username = claims.getSubject();
            String role = claims.get("role" , String.class);

            GrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + role);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(authority)
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

        }catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT");
            return;
        }


        filterChain.doFilter(request, response);
    }


    public String extractCookies(HttpServletRequest request , String name){

        if(request.getCookies() == null){
            return null;
        }

        for(jakarta.servlet.http.Cookie cookie : request.getCookies()){

            if(name.equals(cookie.getName())){
                return cookie.getValue();
            }
        }

            return null;
    }

    @Override
    protected boolean shouldNotFilter(@SuppressWarnings("null") HttpServletRequest request){

        String path = request.getServletPath();
        return path.startsWith("/auth")
                || path.startsWith("/oauth2")
                || path.startsWith("/login");
    }
}
