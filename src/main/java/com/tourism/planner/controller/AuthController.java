package com.tourism.planner.controller;

import com.tourism.planner.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Single Responsibility Principle (SRP):
 * This controller is responsible ONLY for handling incoming HTTP requests, 
 * translating parameters, managing routing, and determining which views to render.
 * It coordinates with AuthService for all business operations to ensure strict separation of concerns.
 */
@Controller
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam String name, 
                                      @RequestParam String email, 
                                      @RequestParam String password, 
                                      Model model) {
        try {
            authService.registerUser(name, email, password);
            return "redirect:/login?registerSuccess=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred during registration. Please try again.");
            return "register";
        }
    }
}
