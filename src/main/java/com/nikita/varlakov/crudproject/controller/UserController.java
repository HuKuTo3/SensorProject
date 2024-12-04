package com.nikita.varlakov.crudproject.controller;

import com.nikita.varlakov.crudproject.model.User;
import com.nikita.varlakov.crudproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Показать форму регистрации")
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @Operation(summary = "Зарегистрировать пользователя")
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, @RequestParam("role") String role) {
        userService.createUser(user.getUsername(), user.getPassword(), User.Role.valueOf(role.toUpperCase()));
        return "redirect:/users/login";
    }

    @Operation(summary = "Показать форму логина")
    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }
}