package com.tourism.planner.service;

import com.tourism.planner.entity.User;
import com.tourism.planner.factory.UserFactory;
import com.tourism.planner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Single Responsibility Principle (SRP):
 * This service class handles exclusively the business logic related to authentication.
 * It delegates data access to UserRepository, object creation to UserFactory, 
 * and is entirely decoupled from HTTP request handling, which is the sole job of the AuthController.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, UserFactory userFactory, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String name, String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        // Using Factory Pattern for object creation
        User newUser = userFactory.createStandardUser(name, email, encodedPassword);
        
        userRepository.save(newUser);
    }
}
