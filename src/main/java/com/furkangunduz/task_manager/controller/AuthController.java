package com.furkangunduz.task_manager.controller;

import com.furkangunduz.task_manager.dto.RegisterRequest;
import com.furkangunduz.task_manager.service.UserService;
import com.furkangunduz.task_manager.exception.DuplicateResourceException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request, BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                logger.info("Registration validation failed for email: {}", request.getEmail());
                return "register";
            }
            
            userService.registerUser(request);
            logger.info("User registered successfully: {}", request.getEmail());
            return "redirect:/login?registered";
            
        } catch (DuplicateResourceException e) {
            logger.warn("Duplicate registration attempt: {}", request.getEmail());
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            logger.error("Registration error", e);
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}