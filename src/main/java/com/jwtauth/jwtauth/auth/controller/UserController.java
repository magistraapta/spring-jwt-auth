package com.jwtauth.jwtauth.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtauth.jwtauth.auth.dto.UserDto;
import com.jwtauth.jwtauth.auth.entity.User;
import com.jwtauth.jwtauth.auth.service.AuthService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private AuthService authService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(authService.getUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser() {
        org.springframework.security.core.Authentication auth = 
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated()) {
            StringBuilder authorities = new StringBuilder();
            authorities.append("User: ").append(auth.getName()).append("\n");
            authorities.append("Authorities: ");
            auth.getAuthorities().forEach(authority -> 
                authorities.append(authority.getAuthority()).append(", "));
            
            return ResponseEntity.ok(authorities.toString());
        }
        
        return ResponseEntity.ok("Not authenticated");
    }
}
