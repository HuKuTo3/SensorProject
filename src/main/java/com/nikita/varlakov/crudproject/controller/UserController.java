package com.nikita.varlakov.crudproject.controller;

import com.nikita.varlakov.crudproject.constants.AppConstants;
import com.nikita.varlakov.crudproject.dto.*;
import com.nikita.varlakov.crudproject.model.User;
import com.nikita.varlakov.crudproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(AppConstants.USERS_BASE_PATH)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Показать форму регистрации")
    @GetMapping(AppConstants.USERS_REGISTER_PATH)
    public String showRegistrationForm(Model model) {
        model.addAttribute(AppConstants.USER_ATTRIBUTE, new User());
        return AppConstants.USER_REGISTER_VIEW;
    }

    @Operation(summary = "Зарегистрировать пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные"),
        @ApiResponse(responseCode = "409", description = "Пользователь уже существует")
    })
    @PostMapping(AppConstants.USERS_REGISTER_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            User user = userService.createUser(
                request.getUsername(), 
                request.getPassword(), 
                User.Role.valueOf(request.getRole().toUpperCase())
            );
            return ResponseEntity.ok(new UserRegistrationResponse(
                user.getUsername(),
                AppConstants.USER_REGISTRATION_SUCCESS
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Invalid input: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("User registration failed: " + e.getMessage()));
        }
    }

    @Operation(summary = "Показать форму логина")
    @GetMapping(AppConstants.USERS_LOGIN_PATH)
    public String showLoginForm() {
        return AppConstants.USER_LOGIN_VIEW;
    }
}