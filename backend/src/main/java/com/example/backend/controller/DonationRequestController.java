package com.example.backend.controller;

import com.example.backend.entity.DonationRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.DonationRequestRepository;
import com.example.backend.repository.UserRepository;
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
@RequestMapping("/api/admin")
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
    public ResponseEntity<?> createDonationRequest(@RequestBody DonationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        request.setUser(user);
        request.setStatus(DonationRequest.RequestStatus.open);
        request.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        DonationRequest savedRequest = donationRequestRepository.save(request);

        return new ResponseEntity<>(savedRequest, HttpStatus.CREATED);
    }

}
