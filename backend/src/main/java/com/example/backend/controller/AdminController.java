package com.example.backend.controller;

import com.example.backend.entity.Donation;
import com.example.backend.entity.DonationRequest;
import com.example.backend.entity.HealthCenter;
import com.example.backend.entity.BloodInventory;
import com.example.backend.entity.User;
import com.example.backend.repository.*;
import com.example.backend.dto.HistoryItem;
import com.example.backend.dto.DonationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = { "http://localhost:8081", "http://127.0.0.1:8081" })
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
    private HealthCenterRepository healthCenterRepository;

    // Get all blood donations for approval
    @GetMapping("/blood-donations")
    public ResponseEntity<List<Donation>> getBloodDonations() {
        List<Donation> donations = donationRepository.findByStatus("Chờ duyệt");
        return ResponseEntity.ok(donations);
    }

    // === API CHÍNH CHO CÁC TRANG QUẢN LÝ HIẾN MÁU ===
    @GetMapping("/donations/status/{status}")
    public ResponseEntity<List<DonationResponse>> getDonationsByStatus(@PathVariable String status) {
        List<Donation> donations = donationRepository.findByStatus(status);
        List<DonationResponse> donationResponses = donations.stream().map(donation -> {
            User user = donation.getUser();
            return new DonationResponse(
                    donation.getDonationId(),
                    user,
                    null,
                    donation.getDonationType() != null ? donation.getDonationType().name() : null,
                    donation.getAmount(),
                    donation.getDate() != null ? new java.sql.Date(donation.getDate().getTime()) : null,
                    donation.getStatus());
        }).collect(Collectors.toList());
        return ResponseEntity.ok(donationResponses);
    }

    // Endpoint để cập nhật trạng thái của một yêu cầu hiến máu
    @PutMapping("/donations/{id}/status")
    public ResponseEntity<Void> updateDonationStatus(@PathVariable Integer id, @RequestBody String status) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        donation.setStatus(status);
        donationRepository.save(donation);
        return ResponseEntity.ok().build();
    }

    // API Lấy Lịch sử Hiến máu (có thể dùng lại API theo status)
    @GetMapping("/blood-donations-history")
    public ResponseEntity<List<Donation>> getBloodDonationsHistory() {
        // Trả về các lượt có status "Hoàn thành" hoặc "Đã hủy"
        List<Donation> completed = donationRepository.findByStatus("Hoàn thành");
        List<Donation> cancelled = donationRepository.findByStatus("Đã hủy");
        completed.addAll(cancelled);
        return ResponseEntity.ok(completed);
    }

    // API Lấy các lượt đã duyệt (Chờ khám) (có thể dùng lại API theo status)
    @GetMapping("/approved-donations")
    public ResponseEntity<List<DonationResponse>> getApprovedDonations() {
        return getDonationsByStatus("Chờ khám");
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoryItem>> getCombinedHistory() {
        List<HistoryItem> history = new ArrayList<>();

        // 1. Lấy lịch sử hiến máu (Donation)
        List<Donation> completedDonations = donationRepository.findByStatus("Hoàn thành");
        List<Donation> cancelledDonations = donationRepository.findByStatus("Đã hủy");
        List<Donation> donationHistory = new ArrayList<>();
        donationHistory.addAll(completedDonations);
        donationHistory.addAll(cancelledDonations);

        history.addAll(donationHistory.stream().map(d -> {
            User user = d.getUser();
            return new HistoryItem(
                    "Hiến máu",
                    d.getDonationId(),
                    user != null ? user.getName() : "N/A",
                    user != null ? user.getPhone() : "N/A",
                    user != null ? user.getBloodType() : "N/A",
                    d.getAmount(),
                    d.getStatus(),
                    d.getDate() != null ? new java.sql.Date(d.getDate().getTime()) : null,
                    user != null ? user.getGender() : "N/A",
                    user != null ? user.getEmail() : "N/A");
        }).collect(Collectors.toList()));

        // 2. Lấy lịch sử yêu cầu cần máu (DonationRequest)
        List<DonationRequest> fulfilledRequests = donationRequestRepository
                .findByStatus(DonationRequest.RequestStatus.fulfilled);
        List<DonationRequest> closedRequests = donationRequestRepository
                .findByStatus(DonationRequest.RequestStatus.closed);
        List<DonationRequest> approvedRequests = donationRequestRepository
                .findByStatus(DonationRequest.RequestStatus.approved);
        List<DonationRequest> requestHistory = new ArrayList<>();
        requestHistory.addAll(fulfilledRequests);
        requestHistory.addAll(closedRequests);
        requestHistory.addAll(approvedRequests);

        history.addAll(requestHistory.stream()
                .filter(r -> r.getRequestType() == DonationRequest.RequestType.receive)
                .map(r -> {
                    HealthCenter center = r.getCenter();
                    User user = r.getUser();
                    java.sql.Date sqlDate = r.getCreatedAt() != null ? new java.sql.Date(r.getCreatedAt().getTime())
                            : null;

                    String name = user != null ? user.getName() : (center != null ? center.getName() : "N/A");
                    String contact = user != null ? user.getPhone()
                            : (center != null ? center.getContactInfo() : "N/A");
                    String gender = user != null ? user.getGender() : "N/A";
                    String email = user != null ? user.getEmail() : "N/A";

                    return new HistoryItem(
                            "Cần máu",
                            r.getRequestId(),
                            name,
                            contact,
                            r.getBloodTypeNeeded(),
                            r.getQuantity(),
                            r.getStatus().toVietnamese(),
                            sqlDate,
                            gender,
                            email);
                }).collect(Collectors.toList()));

        // Sắp xếp theo ngày giảm dần
        history.sort((a, b) -> {
            if (a.getDate() == null && b.getDate() == null)
                return 0;
            if (a.getDate() == null)
                return 1;
            if (b.getDate() == null)
                return -1;
            return b.getDate().compareTo(a.getDate());
        });

        return ResponseEntity.ok(history);
    }

    // Get blood requests (need blood)
    @GetMapping("/blood-requests")
    public List<DonationRequest> getOpenRequests(@RequestParam(required = false) String status) {
        try {
            if (status != null) {
                DonationRequest.RequestStatus enumStatus = DonationRequest.RequestStatus.valueOf(status);
                return donationRequestRepository.findByStatusWithUserAndCenter(enumStatus);
            }
            return donationRequestRepository.findByStatusWithUserAndCenter(DonationRequest.RequestStatus.open);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trạng thái không hợp lệ: " + status);
        }
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
        response.put("totalUnits", bloodTypeCounts.values().stream().mapToInt(Integer::intValue).sum());

        return ResponseEntity.ok(response);
    }

    // Update blood inventory
    @PostMapping("/blood-inventory/update")
    public ResponseEntity<?> updateBloodInventory(@RequestBody Map<String, Object> request) {
        try {
            String bloodType = (String) request.get("bloodType");
            Integer quantity = (Integer) request.get("quantity");
            Integer centerId = (Integer) request.get("centerId");

            HealthCenter center = healthCenterRepository.findById(centerId)
                    .orElseThrow(() -> new RuntimeException("Health center not found"));

            List<BloodInventory> inventories = bloodInventoryRepository
                    .findByBloodTypeAndStatus(bloodType, BloodInventory.InventoryStatus.available)
                    .stream().filter(inv -> inv.getCenter().getCenterId().equals(centerId)).toList();

            BloodInventory inventory = null;
            if (!inventories.isEmpty()) {
                inventory = inventories.get(0);
                inventory.setQuantity(inventory.getQuantity() + quantity);
            } else {
                inventory = new BloodInventory();
                inventory.setCenter(center);
                inventory.setBloodType(bloodType);
                inventory.setComponentType(BloodInventory.ComponentType.whole);
                inventory.setQuantity(quantity);
                inventory.setStatus(BloodInventory.InventoryStatus.available);
            }
            bloodInventoryRepository.save(inventory);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Blood inventory updated successfully");
            response.put("bloodType", bloodType);
            response.put("quantity", inventory.getQuantity());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating blood inventory: " + e.getMessage());
        }
    }

    @DeleteMapping("/donations/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Integer id) {
        donationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Delete blood request
    @DeleteMapping("/blood-requests/{id}")
    public ResponseEntity<?> deleteBloodRequest(@PathVariable Integer id) {
        donationRequestRepository.deleteById(id);
        return ResponseEntity.ok().body("Blood request deleted successfully");
    }

    // Approve blood request
    @PostMapping("/blood-requests/{id}/approve")
    public ResponseEntity<?> approveBloodRequest(@PathVariable Integer id) {
        DonationRequest request = donationRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation request not found with id: " + id));

        if (request.getRequestType() == DonationRequest.RequestType.receive) {
            // Cần máu: chỉ cập nhật status
            request.setStatus(DonationRequest.RequestStatus.fulfilled);
            donationRequestRepository.save(request);
            return ResponseEntity.ok(Map.of("message", "Blood request fulfilled (no donation created)"));
        }

        // Hiến máu: tạo bản ghi mới ở donations
        request.setStatus(DonationRequest.RequestStatus.approved);
        donationRequestRepository.save(request);

        Donation donation = new Donation();
        donation.setUser(request.getUser());
        donation.setRequest(request);
        donation.setDonationType(Donation.DonationType.valueOf(request.getBloodTypeNeeded())); // hoặc mapping phù hợp
        donation.setDate(new java.sql.Timestamp(System.currentTimeMillis()));
        donation.setStatus("Chờ khám");
        donationRepository.save(donation);

        return ResponseEntity.ok(Map.of("message", "Blood request approved and donation created successfully"));
    }

    @GetMapping("/health-centers")
    public ResponseEntity<List<HealthCenter>> getHealthCenters() {
        return ResponseEntity.ok(healthCenterRepository.findAll());
    }

    @PostMapping("/donations/{id}/complete")
    public ResponseEntity<?> completeDonation(@PathVariable Integer id, @RequestBody Map<String, Object> payload) {
        try {
            Donation donation = donationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Donation not found with id: " + id));

            // Cập nhật các trường từ payload
            if (payload.containsKey("amount")) {
                donation.setAmount((Integer) payload.get("amount"));
            }

            // Cập nhật trạng thái và lưu
            donation.setStatus("Hoàn thành");
            donationRepository.save(donation);

            return ResponseEntity.ok(Collections.singletonMap("message", "Donation completed successfully"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error completing donation: " + e.getMessage());
        }
    }
}