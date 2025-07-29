package com.jwtauth.jwtauth.auth.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.jwtauth.jwtauth.auth.dto.LoginRequestDto;
import com.jwtauth.jwtauth.auth.dto.RegisterDto;
import com.jwtauth.jwtauth.auth.entity.Role;
import com.jwtauth.jwtauth.auth.entity.User;
import com.jwtauth.jwtauth.auth.repository.AuthRepository;
import com.jwtauth.jwtauth.auth.exception.EmailAlreadyExistsException;
import com.jwtauth.jwtauth.auth.exception.UserNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthRepository authRepository;

    @Test
    public void testSignUp() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testuser");
        registerDto.setEmail("testuser@test.com");
        registerDto.setPassword("password123");

        // When
        User savedUser = authService.signUp(registerDto);

        // Then
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("testuser@test.com", savedUser.getEmail());
        assertEquals(Role.USER, savedUser.getRole());
        assertNotNull(savedUser.getId());

        // Verify user was actually saved to database
        Optional<User> foundUser = authRepository.findByEmail("testuser@test.com");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }   

    @Test
    public void testSignUpWithDuplicateEmail() {
        // Given - create first user
        RegisterDto registerDto1 = new RegisterDto();
        registerDto1.setUsername("user1");
        registerDto1.setEmail("duplicate@test.com");
        registerDto1.setPassword("password123");
        authService.signUp(registerDto1);

        // When - try to create second user with same email
        RegisterDto registerDto2 = new RegisterDto();
        registerDto2.setUsername("user2");
        registerDto2.setEmail("duplicate@test.com");
        registerDto2.setPassword("password456");

        // Then - should throw exception
        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            authService.signUp(registerDto2);
        });
        assertEquals("User already exists, try another email", exception.getMessage());
    }

    @Test
    public void testPromoteUser() {
        // Given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("promoteuser");
        registerDto.setEmail("promoteuser@test.com");
        registerDto.setPassword("password123");
        User user = authService.signUp(registerDto);

        // When
        authService.promoteUser(user.getId(), Role.ADMIN);

        // Then
        Optional<User> foundUser = authRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(Role.ADMIN, foundUser.get().getRole());
    }

    @Test
    public void testPromoteNonExistentUser() {
        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authService.promoteUser(999L, Role.ADMIN);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testGetUsers() {
        // Given
        RegisterDto registerDto1 = new RegisterDto();
        registerDto1.setUsername("user1");
        registerDto1.setEmail("user1@test.com");
        registerDto1.setPassword("password123");
        authService.signUp(registerDto1);

        RegisterDto registerDto2 = new RegisterDto();
        registerDto2.setUsername("user2");
        registerDto2.setEmail("user2@test.com");
        registerDto2.setPassword("password456");
        authService.signUp(registerDto2);

        // When
        var users = authService.getUsers();

        // Then
        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }

    @Test
    public void testLoginWithNonExistentUser() {
        // Given
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("nonexistent@test.com");
        loginRequestDto.setPassword("password123");

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authService.login(loginRequestDto);
        });
        assertEquals("User not found", exception.getMessage());
    }
}
