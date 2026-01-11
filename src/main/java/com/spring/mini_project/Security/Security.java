package com.spring.mini_project.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.spring.mini_project.JWT.Filter;
import com.spring.mini_project.OAuth2.OAuth2LoginHandler;

@EnableWebSecurity
@Configuration
public class Security {
    
    private final Filter jwtFilter;
    private final OAuth2LoginHandler handler;
    public Security(Filter jwtFilter , OAuth2LoginHandler handler){
        this.jwtFilter = jwtFilter;
        this.handler = handler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .oauth2Login(oauth -> oauth.disable())
                .sessionManagement(session
                                    -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/").permitAll()
                    .requestMatchers("admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter , UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(auth -> auth.successHandler(handler))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
