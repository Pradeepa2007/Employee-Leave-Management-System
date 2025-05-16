package com.leave.management.project.exception;

public class LeaveRequestNotFoundException extends RuntimeException {
    public LeaveRequestNotFoundException(Long id) {
        super("Leave request not found with ID: " + id);
    }
}

