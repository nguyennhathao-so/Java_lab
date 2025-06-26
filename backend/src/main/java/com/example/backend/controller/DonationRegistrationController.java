package com.example.backend.controller;

import com.example.backend.entity.DonationRegistration;
import com.example.backend.repository.DonationRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donation-registrations")
@CrossOrigin
public class DonationRegistrationController {
    @Autowired
    private DonationRegistrationRepository donationRegistrationRepository;

    @PostMapping
    public DonationRegistration register(@RequestBody DonationRegistration registration) {
        return donationRegistrationRepository.save(registration);
    }
} 