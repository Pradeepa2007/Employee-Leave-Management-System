package com.leave.management.project.service;

import com.leave.management.project.controller.LeaveRequestController;
import com.leave.management.project.module.User;
import com.leave.management.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(LeaveRequestController.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        try {
            userRepository.findAll().forEach(users::add);
            log.info("Retrieved {} users from the database", users.size());
        } catch (Exception e) {
            log.error("Error occurred while fetching users", e);
            throw e;
        }
        return users;
    }
}
