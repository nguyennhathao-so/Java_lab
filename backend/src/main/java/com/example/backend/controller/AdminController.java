package com.example.backend.controller;

import com.example.backend.entity.Donation;
import com.example.backend.entity.DonationRequest;
import com.example.backend.entity.BloodInventory;
import com.example.backend.entity.User;
import com.example.backend.entity.Notification;
import com.example.backend.entity.DonationRegistration;
import com.example.backend.repository.DonationRepository;
import com.example.backend.repository.DonationRequestRepository;
import com.example.backend.repository.BloodInventoryRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.NotificationRepository;
import com.example.backend.repository.DonationRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @Autowired
    private BloodInventoryRepository bloodInventoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DonationRegistrationRepository donationRegistrationRepository;

    // Get all blood donations for approval
    @GetMapping("/blood-donations")
    public ResponseEntity<List<Donation>> getBloodDonations() {
        List<Donation> donations = donationRepository.findAllWithUserOrderByDateDesc();
        return ResponseEntity.ok(donations);
    }

    // Get blood donations history
    @GetMapping("/blood-donations-history")
    public ResponseEntity<List<Donation>> getBloodDonationsHistory() {
        List<Donation> donations = donationRepository.findAllWithUserOrderByDateDesc();
        return ResponseEntity.ok(donations);
    }

    // Get approved donations
    @GetMapping("/approved-donations")
    public ResponseEntity<List<Donation>> getApprovedDonations() {
        List<Donation> donations = donationRepository.findAllWithUserOrderByDateDesc();
        return ResponseEntity.ok(donations);
    }

    // Get blood requests (need blood)
    @GetMapping("/blood-requests")
    public ResponseEntity<List<DonationRequest>> getBloodRequests() {
        List<DonationRequest> requests = donationRequestRepository.findAllWithCenterOrderByCreatedAtDesc();
        return ResponseEntity.ok(requests);
    }

    // Get blood inventory summary
    @GetMapping("/blood-inventory")
    public ResponseEntity<Map<String, Object>> getBloodInventory() {
        List<Object[]> summary = bloodInventoryRepository.getBloodTypeSummary();
        Map<String, Object> response = new HashMap<>();

        // Initialize all blood types with 0
        String[] bloodTypes = { "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-" };
        Map<String, Integer> bloodTypeCounts = new HashMap<>();
        for (String type : bloodTypes) {
            bloodTypeCounts.put(type, 0);
        }

        // Update with actual data
        for (Object[] result : summary) {
            String bloodType = (String) result[0];
            Long quantity = (Long) result[1];
            bloodTypeCounts.put(bloodType, quantity.intValue());
        }

        response.put("bloodTypeCounts", bloodTypeCounts);
        response.put("totalUnits", bloodTypeCounts.values().stream().mapToLong(Integer::longValue).sum());

        return ResponseEntity.ok(response);
    }

    // Update blood inventory
    @PostMapping("/blood-inventory/update")
    public ResponseEntity<?> updateBloodInventory(@RequestBody Map<String, Object> request) {
        try {
            String bloodType = (String) request.get("bloodType");
            Integer quantity = (Integer) request.get("quantity");

            // For now, just return success - you can implement actual update logic later
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Blood inventory updated successfully");
            response.put("bloodType", bloodType);
            response.put("quantity", quantity);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating blood inventory: " + e.getMessage());
        }
    }

    // Delete donation
    @DeleteMapping("/donations/{id}")
    public ResponseEntity<?> deleteDonation(@PathVariable String id) {
        try {
            donationRepository.deleteById(id);
            return ResponseEntity.ok().body("Donation deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting donation: " + e.getMessage());
        }
    }

    // Approve donation
    @PostMapping("/donations/{id}/approve")
    public ResponseEntity<?> approveDonation(@PathVariable String id) {
        try {
            // For now, just return success - you can implement actual approval logic later
            return ResponseEntity.ok().body("Donation approved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error approving donation: " + e.getMessage());
        }
    }

    // Delete blood request
    @DeleteMapping("/blood-requests/{id}")
    public ResponseEntity<?> deleteBloodRequest(@PathVariable String id) {
        try {
            donationRequestRepository.deleteById(id);
            return ResponseEntity.ok().body("Blood request deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting blood request: " + e.getMessage());
        }
    }

    // Approve blood request
    @PostMapping("/blood-requests/{id}/approve")
    public ResponseEntity<?> approveBloodRequest(@PathVariable String id) {
        try {
            // For now, just return success - you can implement actual approval logic later
            return ResponseEntity.ok().body("Blood request approved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error approving blood request: " + e.getMessage());
        }
    }

    // Get total user count
    @GetMapping("/users/count")
    public ResponseEntity<Map<String, Long>> getUserCount() {
        long userCount = userRepository.count();
        Map<String, Long> response = new HashMap<>();
        response.put("userCount", userCount);
        return ResponseEntity.ok(response);
    }

    // Get total requests count from donations and donation_requests
    @GetMapping("/requests/count")
    public ResponseEntity<Map<String, Long>> getTotalRequestsCount() {
        long donationCount = donationRepository.count();
        long donationRequestCount = donationRequestRepository.count();
        long totalRequests = donationCount + donationRequestCount;
        
        Map<String, Long> response = new HashMap<>();
        response.put("totalRequests", totalRequests);
        return ResponseEntity.ok(response);
    }

    // Get all notifications (admin)
    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAllByOrderByCreatedAtDesc();
        return ResponseEntity.ok(notifications);
    }

    // Lấy lịch sử đăng ký hiến/cần máu của user
    @GetMapping("/donation-registrations/{userId}")
    public ResponseEntity<List<DonationRegistration>> getDonationRegistrations(@PathVariable String userId) {
        List<DonationRegistration> list = donationRegistrationRepository.findByUser_UserIdOrderByRegistrationDateDesc(userId);
        return ResponseEntity.ok(list);
    }

    // Get notifications by user
    @GetMapping("/notifications/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(@PathVariable String userId) {
        List<Notification> notifications = notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(notifications);
    }
}