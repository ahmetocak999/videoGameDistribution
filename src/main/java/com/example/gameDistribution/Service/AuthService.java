package com.example.gameDistribution.Service;


import com.example.gameDistribution.DTO.AuthDTO;
import com.example.gameDistribution.Entity.AuthEntity;
import com.example.gameDistribution.Entity.UserEntity;
import com.example.gameDistribution.Mapper.AuthMapper;
import com.example.gameDistribution.Repository.AuthRepo;
import com.example.gameDistribution.Repository.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.HashMap;


@Service
public class AuthService {
    private final UserRepo userRepo;
    private final AuthRepo authRepo;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(UserRepo userRepo, AuthRepo authRepo, AuthMapper authMapper, PasswordEncoder passwordEncoder, AuthenticationManager authManager, JwtService jwtService) {
        this.userRepo = userRepo;
        this.authRepo = authRepo;
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public void register(AuthDTO authDTO) {
        // 1. AuthEntity oluştur
        AuthEntity auth = new AuthEntity();
        auth.setUsername(authDTO.getUsername());
        auth.setPassword(passwordEncoder.encode(authDTO.getPassword()));
        auth.setRole("ROLE_USER");
        auth = authRepo.save(auth); // ID burada oluşuyor

        // 2. UserEntity oluştur ve authId ata
        UserEntity user = new UserEntity();
        user.setName(authDTO.getUsername());
        user.setAverageRating(0);
        user.setTotalPlayTime(0);
        user.setAuthId(auth.getId());
        userRepo.save(user);
    }


    public String verify(AuthDTO authDTO) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword())
        );

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("ROLE_USER");

            return jwtService.generateToken(authDTO.getUsername(), role);
        }

        throw new BadCredentialsException("Invalid credentials");
    }
}


