package com.nikita.varlakov.crudproject.service;

import com.nikita.varlakov.crudproject.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    User createUser(String username, String password, User.Role role);
}
