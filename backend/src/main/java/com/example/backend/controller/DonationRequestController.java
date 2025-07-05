package com.example.backend.controller;

import com.example.backend.entity.DonationRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.DonationRequestRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.dto.DonationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donation-requests")
@CrossOrigin(origins = { "http://localhost:8081", "http://127.0.0.1:8081" })
public class DonationRequestController {

    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<DonationRequest>> getAllDonationRequests() {
        List<DonationRequest> requests = donationRequestRepository.findAll();
        return ResponseEntity.ok(requests);
    }

    @PostMapping
    public ResponseEntity<?> createDonationRequest(@RequestBody DonationRequestDto dto) {
        System.out.println("DTO requestType: " + dto.getRequestType());
        DonationRequest request = new DonationRequest();
        request.setBloodTypeNeeded(dto.getBloodTypeNeeded());
        request.setQuantity(dto.getQuantity());

        // Xử lý urgency level
        if (dto.getUrgencyLevel() != null && !dto.getUrgencyLevel().isBlank()) {
            request.setUrgencyLevel(DonationRequest.UrgencyLevel.fromString(dto.getUrgencyLevel()));
        } else {
            request.setUrgencyLevel(DonationRequest.UrgencyLevel.medium); // mặc định
        }

        // Xử lý request type
        if (dto.getRequestType() != null && !dto.getRequestType().isBlank()) {
            request.setRequestType(DonationRequest.RequestType.valueOf(dto.getRequestType()));
        } else {
            request.setRequestType(DonationRequest.RequestType.donate); // mặc định là hiến máu
        }

        request.setStatus(DonationRequest.RequestStatus.open);
        request.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // Xử lý desired date nếu có
        if (dto.getDesiredDate() != null && !dto.getDesiredDate().isBlank()) {
            try {
                String dateStr = dto.getDesiredDate().replace("T", " ") + ":00";
                request.setDesiredDate(Timestamp.valueOf(dateStr));
            } catch (Exception e) {
                System.out.println("Error parsing desired date: " + e.getMessage());
                // Không set desired date nếu parse lỗi
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        request.setUser(user);

        donationRequestRepository.save(request);
        System.out.println("Entity requestType: " + request.getRequestType());

        // Trả về message phù hợp với loại yêu cầu
        String message = request.getRequestType() == DonationRequest.RequestType.receive
                ? "Yêu cầu cần máu đã được ghi nhận!"
                : "Yêu cầu hiến máu đã được ghi nhận!";

        return ResponseEntity.ok(message);
    }

}
