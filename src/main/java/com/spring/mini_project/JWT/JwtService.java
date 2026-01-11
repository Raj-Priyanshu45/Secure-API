package com.spring.mini_project.JWT;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.spring.mini_project.Model.AppUser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    private final SecretKey secret;
    private final long expTime;
    public JwtService(Config config){
        this.secret = Keys.hmacShaKeyFor(
                    Base64.getDecoder().decode(config.getSecret())
        );
        this.expTime = config.getExpiration();
    }

    @SuppressWarnings("deprecation")
    public String generateToken(AppUser user){

        return Jwts.builder()
                    .setSubject(user.getUsername())     
                    .claim("role", user.getRoles().name())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expTime))
                    .signWith(secret)
                    .compact();

    }

    public String generateRefreshTokenRaw() {
    return UUID.randomUUID().toString() + UUID.randomUUID().toString();
}

public String hashRefreshToken(String rawToken) {
    if (rawToken == null) {
        throw new IllegalArgumentException("rawToken cannot be null");
    }

    try {
        byte[] hash = MessageDigest
                .getInstance("SHA-256")
                .digest(rawToken.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}

}
