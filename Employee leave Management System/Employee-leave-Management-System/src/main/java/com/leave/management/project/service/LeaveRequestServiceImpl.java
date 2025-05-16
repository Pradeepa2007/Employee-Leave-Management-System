package com.leave.management.project.service;

import com.leave.management.project.controller.LeaveRequestController;
import com.leave.management.project.dto.LeaveRequestDTO;
import com.leave.management.project.exception.LeaveRequestNotFoundException;
import com.leave.management.project.module.LeaveRequest;
import com.leave.management.project.module.LeaveStatus;
import com.leave.management.project.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private  ModelMapper modelMapper;
    private static final Logger log = LoggerFactory.getLogger(LeaveRequestController.class);

    @Override
    public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO dto) {
        try {
            LeaveRequest request = modelMapper.map(dto, LeaveRequest.class);
            request.setStatus(LeaveStatus.PENDING);
            request.setAppliedDate(LocalDate.now());

            LeaveRequest savedRequest = leaveRequestRepository.save(request);
            log.info("Leave request created with ID: {}", savedRequest.getId());

            return modelMapper.map(savedRequest, LeaveRequestDTO.class);
        } catch (Exception e) {
            log.error("Error creating leave request", e);
            throw e;
        }
    }

    @Override
    public LeaveRequestDTO getLeaveRequestById(Long id) {
        try {
            LeaveRequest request = leaveRequestRepository.findById(id)
                    .orElseThrow(() -> new LeaveRequestNotFoundException(id));
            return modelMapper.map(request, LeaveRequestDTO.class);
        } catch (LeaveRequestNotFoundException e) {
            log.warn("Leave request not found with ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving leave request with ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequests() {
        try {
            return leaveRequestRepository.findAll().stream()
                    .map(req -> modelMapper.map(req, LeaveRequestDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving all leave requests", e);
            throw e;
        }
    }

    @Override
    public List<LeaveRequestDTO> getLeaveRequestsByEmployeeId(Long employeeId) {
        try {
            return leaveRequestRepository.findByEmployeeId(employeeId).stream()
                    .map(req -> modelMapper.map(req, LeaveRequestDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving leave requests for employee ID: {}", employeeId, e);
            throw e;
        }
    }

    @Override
    public List<LeaveRequestDTO> getLeaveRequestsByStatus(LeaveStatus status) {
        try {
            return leaveRequestRepository.findByStatus(status).stream()
                    .map(req -> modelMapper.map(req, LeaveRequestDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving leave requests with status: {}", status, e);
            throw e;
        }
    }

    @Override
    public LeaveRequestDTO updateLeaveRequestStatus(Long id, LeaveStatus status) {
        try {
            LeaveRequest request = leaveRequestRepository.findById(id)
                    .orElseThrow(() -> new LeaveRequestNotFoundException(id));

            request.setStatus(status);
            if (status == LeaveStatus.APPROVED) {
                request.setApprovedDate(LocalDate.now());
            }

            LeaveRequest updated = leaveRequestRepository.save(request);
            log.info("Leave request ID {} updated with status {}", id, status);

            return modelMapper.map(updated, LeaveRequestDTO.class);
        } catch (LeaveRequestNotFoundException e) {
            log.warn("Leave request not found for update, ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Error updating leave request status for ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public void deleteLeaveRequest(Long id) {
        try {
            if (!leaveRequestRepository.existsById(id)) {
                throw new LeaveRequestNotFoundException(id);
            }
            leaveRequestRepository.deleteById(id);
            log.info("Leave request deleted with ID: {}", id);
        } catch (LeaveRequestNotFoundException e) {
            log.warn("Attempted to delete non-existent leave request ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Error deleting leave request with ID: {}", id, e);
            throw e;
        }
    }
}
