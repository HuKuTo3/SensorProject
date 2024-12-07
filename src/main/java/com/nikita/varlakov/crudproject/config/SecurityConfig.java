package com.nikita.varlakov.crudproject.config;

import com.nikita.varlakov.crudproject.constants.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(AppConstants.PUBLIC_PATHS).permitAll()
                    .requestMatchers(AppConstants.SENSORS_BASE_PATH)
                        .hasAnyRole("ADMINISTRATOR", "VIEWER")
                    .requestMatchers(AppConstants.SENSORS_BASE_PATH + "/**")
                        .hasRole("ADMINISTRATOR")
                    .anyRequest().authenticated()
            )
            .formLogin(login -> login
                    .loginPage(AppConstants.USERS_BASE_PATH + AppConstants.USERS_LOGIN_PATH)
                    .loginProcessingUrl(AppConstants.USERS_BASE_PATH + AppConstants.USERS_LOGIN_PATH)
                    .defaultSuccessUrl(AppConstants.SENSORS_BASE_PATH, true)
            )
            .logout(logout -> logout
                    .logoutUrl(AppConstants.LOGOUT_PATH)
                    .logoutSuccessUrl(AppConstants.USERS_BASE_PATH + AppConstants.USERS_LOGIN_PATH)
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}