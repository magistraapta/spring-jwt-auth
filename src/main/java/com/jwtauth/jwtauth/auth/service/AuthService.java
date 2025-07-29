package com.jwtauth.jwtauth.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtauth.jwtauth.auth.dto.AuthResponse;
import com.jwtauth.jwtauth.auth.dto.LoginRequestDto;
import com.jwtauth.jwtauth.auth.dto.RegisterDto;
import com.jwtauth.jwtauth.auth.dto.UserDto;
import com.jwtauth.jwtauth.auth.entity.Role;
import com.jwtauth.jwtauth.auth.entity.User;
import com.jwtauth.jwtauth.auth.jwt.JwtService;
import com.jwtauth.jwtauth.auth.repository.AuthRepository;
import com.jwtauth.jwtauth.auth.exception.EmailAlreadyExistsException;
import com.jwtauth.jwtauth.auth.exception.UserNotFoundException;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User signUp(RegisterDto registerDto) {
        try {
            User user = new User();
            user.setUsername(registerDto.getUsername());
            user.setEmail(registerDto.getEmail());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            user.setRole(Role.USER);

            if (authRepository.findByEmail(registerDto.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException("User already exists, try another email");
            }

            return authRepository.save(user);
        } catch (EmailAlreadyExistsException e) {
            // Re-throw EmailAlreadyExistsException as is
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
        
    }

    public User adminSignUp(RegisterDto registerDto) {
        try {
            User user = new User();
            user.setUsername(registerDto.getUsername());
            user.setEmail(registerDto.getEmail());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            user.setRole(Role.ADMIN);

            if (authRepository.findByEmail(registerDto.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException("User already exists, try another email");
            }

            return authRepository.save(user);
        } catch (EmailAlreadyExistsException e) {
            // Re-throw EmailAlreadyExistsException as is
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
    }

    public AuthResponse login(LoginRequestDto loginRequestDto) {
        try {
            // First, check if user exists
            User user = authRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

            // Authenticate the user
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getEmail(),
                    loginRequestDto.getPassword()
                )
            );

            // Generate JWT token
            String jwtToken = jwtService.generateToken(user);
            return AuthResponse.builder().token(jwtToken).build();
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (UserNotFoundException e) {
            // Re-throw UserNotFoundException as is
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error: " + e.getMessage());
        }
        
    }

    public List<UserDto> getUsers() {
        try {
            List<User> users = authRepository.findAll();
            return users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
        
    }

    public void promoteUser(Long id, Role role) {
        User user = authRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setRole(role);
        authRepository.save(user);
    }
}
