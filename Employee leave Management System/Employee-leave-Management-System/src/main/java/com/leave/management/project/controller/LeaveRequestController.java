package com.leave.management.project.controller;

import com.leave.management.project.dto.LeaveRequestDTO;
import com.leave.management.project.module.LeaveStatus;
import com.leave.management.project.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;
    private static final Logger log = LoggerFactory.getLogger(LeaveRequestController.class);

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<LeaveRequestDTO> createLeave(@RequestBody LeaveRequestDTO dto) {
        try {
            LeaveRequestDTO created = leaveRequestService.createLeaveRequest(dto);
            log.info("Leave request created for employeeId: {}", dto.getEmployeeId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Failed to create leave request for employeeId: {}", dto.getEmployeeId(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestDTO> getById(@PathVariable Long id) {
        try {
            LeaveRequestDTO request = leaveRequestService.getLeaveRequestById(id);
            log.info("Fetched leave request with ID: {}", id);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            log.error("Failed to fetch leave request with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<LeaveRequestDTO>> getAll() {
        try {
            List<LeaveRequestDTO> list = leaveRequestService.getAllLeaveRequests();
            log.info("Fetched all leave requests, total: {}", list.size());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Failed to fetch all leave requests", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequestDTO>> getByEmployee(@PathVariable Long employeeId) {
        try {
            List<LeaveRequestDTO> list = leaveRequestService.getLeaveRequestsByEmployeeId(employeeId);
            log.info("Fetched leave requests for employeeId: {}, total: {}", employeeId, list.size());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Failed to fetch leave requests for employeeId: {}", employeeId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeaveRequestDTO>> getByStatus(@PathVariable LeaveStatus status) {
        try {
            List<LeaveRequestDTO> list = leaveRequestService.getLeaveRequestsByStatus(status);
            log.info("Fetched leave requests with status: {}, total: {}", status, list.size());
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Failed to fetch leave requests with status: {}", status, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<LeaveRequestDTO> updateStatus(@PathVariable Long id, @RequestParam LeaveStatus status) {
        try {
            LeaveRequestDTO updated = leaveRequestService.updateLeaveRequestStatus(id, status);
            log.info("Updated status of leave request ID: {} to {}", id, status);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Failed to update status of leave request ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable Long id) {
        try {
            leaveRequestService.deleteLeaveRequest(id);
            log.info("Deleted leave request with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to delete leave request with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
