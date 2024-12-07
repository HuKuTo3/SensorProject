package com.nikita.varlakov.crudproject.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class UserRegistrationResponse {
    private String username;
    private String message;
}