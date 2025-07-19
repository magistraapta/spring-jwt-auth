package com.jwtauth.jwtauth.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtauth.jwtauth.auth.dto.AuthResponse;
import com.jwtauth.jwtauth.auth.dto.LoginRequestDto;
import com.jwtauth.jwtauth.auth.dto.RegisterDto;
import com.jwtauth.jwtauth.auth.entity.User;
import com.jwtauth.jwtauth.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(authService.signUp(registerDto));
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<User> adminSignUp(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(authService.adminSignUp(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    
}