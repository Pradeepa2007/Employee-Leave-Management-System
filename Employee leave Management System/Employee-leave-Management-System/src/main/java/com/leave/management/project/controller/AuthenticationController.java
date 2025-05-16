package com.leave.management.project.controller;

import com.leave.management.project.dto.LoginResponse;
import com.leave.management.project.dto.LoginUserDto;
import com.leave.management.project.dto.RegisterUserDto;
import com.leave.management.project.module.User;
import com.leave.management.project.service.AuthenticationService;
import com.leave.management.project.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        try {
            User registeredUser = authenticationService.signup(registerUserDto);
            log.info("User registered successfully: {}", registeredUser.getUsername());
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            log.error("Error during user registration for username: {}", registerUserDto.getFullName(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());

            log.info("User authenticated successfully: {}", authenticatedUser.getUsername());
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            log.error("Authentication failed for username: {}", loginUserDto.getEmail(), e);
            return ResponseEntity.status(401).build();
        }
    }
}
