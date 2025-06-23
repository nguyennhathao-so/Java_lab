package com.example.backend.controller;

import com.example.backend.entity.Donation;
import com.example.backend.entity.User;
import com.example.backend.repository.DonationRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "*")
public class DonationController {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> registerDonation(@RequestBody Map<String, Object> payload) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        Donation donation = new Donation();
        donation.setUser(user);
        donation.setAmount(null); // Số lượng máu sẽ được cập nhật sau khi khám

        // Trích xuất và chuyển đổi date
        if (payload.get("donationDate") != null) {
            String dateStr = payload.get("donationDate").toString().replace("T", " ") + ":00";
            donation.setDate(Timestamp.valueOf(dateStr));
        }

        donation.setStatus("Chờ duyệt"); // Trạng thái ban đầu

        // Trích xuất và chuyển đổi donationType
        if (payload.get("donationType") != null) {
            donation.setDonationType(Donation.DonationType.valueOf(payload.get("donationType").toString()));
        }

        Donation savedDonation = donationRepository.save(donation);
        return ResponseEntity.ok(savedDonation);
    }

}