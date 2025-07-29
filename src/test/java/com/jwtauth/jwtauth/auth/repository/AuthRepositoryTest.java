package com.jwtauth.jwtauth.auth.repository;

import java.util.List;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.jwtauth.jwtauth.auth.entity.Role;
import com.jwtauth.jwtauth.auth.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class AuthRepositoryTest {

    @Autowired
    private AuthRepository authRepository;
    
    @Test
    public void testFindByUsername() {
        // Given
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("test");
        user.setRole(Role.USER);
        authRepository.save(user);

        // When
        User foundUser = authRepository.findByUsername("test");
        
        // Then
        assertNotNull(foundUser);
        assertEquals("test", foundUser.getUsername());
        assertEquals("test@test.com", foundUser.getEmail());
        assertEquals(Role.USER, foundUser.getRole());
    }

    @Test
    public void testFindAll() {
        // Given
        User user1 = new User();
        user1.setUsername("test1");
        user1.setEmail("test1@test.com");
        user1.setPassword("test1");
        user1.setRole(Role.USER);
        
        User user2 = new User();
        user2.setUsername("test2");
        user2.setEmail("test2@test.com");
        user2.setPassword("test2");
        user2.setRole(Role.ADMIN);
        
        authRepository.save(user1);
        authRepository.save(user2);

        // When
        List<User> users = authRepository.findAll();
        
        // Then
        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }
    
    @Test
    public void testFindByEmail() {
        // Given
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("test");
        user.setRole(Role.USER);
        authRepository.save(user);

        // When
        var foundUser = authRepository.findByEmail("test@test.com");
        
        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("test", foundUser.get().getUsername());
        assertEquals("test@test.com", foundUser.get().getEmail());
    }
} 