package com.leave.management.project.controller;

import com.leave.management.project.module.User;
import com.leave.management.project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) authentication.getPrincipal();

            log.info("Authenticated user fetched: {}", currentUser.getUsername());
            return ResponseEntity.ok(currentUser);
        } catch (Exception e) {
            log.error("Error fetching authenticated user", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        try {
            List<User> users = userService.allUsers();
            log.info("All users fetched, count: {}", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
