package com.spring.mini_project.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.mini_project.Model.AppUser;

import jakarta.transaction.Transactional;

public interface UserRepo extends JpaRepository<AppUser, Long>{
    
    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByRefreshToken(String refreshToken);

    Optional<AppUser> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("""
        UPDATE AppUser u
        SET u.refreshToken = NULL
        WHERE u.username = :username
    """)
    int clearRefreshToken(@Param("username") String username);

}
