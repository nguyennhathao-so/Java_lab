package com.example.backend.controller;

import com.example.backend.entity.DonationRequest;
import com.example.backend.repository.DonationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donation-requests")
@CrossOrigin
public class DonationRequestController {
    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @PostMapping
    public DonationRequest register(@RequestBody DonationRequest request) {
        return donationRequestRepository.save(request);
    }
} 