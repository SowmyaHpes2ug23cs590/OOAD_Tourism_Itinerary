package com.tourism.planner.factory;

import com.tourism.planner.entity.User;
import org.springframework.stereotype.Component;

/**
 * Creational Design Pattern: Factory Pattern
 * 
 * This Factory class abstracts the creation logic of User objects.
 * Instead of instantiating 'new User()' directly throughout the codebase,
 * the application delegates user creation to this factory. 
 * If future requirements demand creating specialized user types (e.g., AdminUser, StandardUser),
 * the factory can be easily extended to produce the appropriate subclass 
 * based on input parameters without changing the calling code.
 */
@Component
public class UserFactory {

    public User createStandardUser(String name, String email, String encodedPassword) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        return user;
    }
}
