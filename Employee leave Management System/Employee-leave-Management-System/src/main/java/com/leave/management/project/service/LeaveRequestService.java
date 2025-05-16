package com.leave.management.project.service;


import com.leave.management.project.dto.LeaveRequestDTO;
import com.leave.management.project.module.LeaveStatus;

import java.util.List;

public interface LeaveRequestService {
    LeaveRequestDTO createLeaveRequest(LeaveRequestDTO request);
    LeaveRequestDTO getLeaveRequestById(Long id);
    List<LeaveRequestDTO> getAllLeaveRequests();
    List<LeaveRequestDTO> getLeaveRequestsByEmployeeId(Long employeeId);
    List<LeaveRequestDTO> getLeaveRequestsByStatus(LeaveStatus status);
    LeaveRequestDTO updateLeaveRequestStatus(Long id, LeaveStatus status);
    void deleteLeaveRequest(Long id);


}
