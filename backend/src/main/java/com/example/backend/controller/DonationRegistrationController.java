package com.example.backend.controller;

import com.example.backend.entity.DonationRegistration;
import com.example.backend.entity.User;
import com.example.backend.entity.DonationRequest;
import com.example.backend.entity.Donation;
import com.example.backend.repository.DonationRegistrationRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.DonationRequestRepository;
import com.example.backend.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donation-registrations")
@CrossOrigin(origins = { "http://localhost:8081", "http://127.0.0.1:8081" })
public class DonationRegistrationController {

    @Autowired
    private DonationRegistrationRepository donationRegistrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @Autowired
    private DonationRepository donationRepository;

    // Đăng ký hiến máu - tạo DonationRegistration
    @PostMapping("/donate")
    public ResponseEntity<?> registerDonation(@RequestBody Map<String, Object> request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Lưu vào donation_registrations (lịch sử đăng ký)
            DonationRegistration registration = new DonationRegistration();
            registration.setId(generateId("DRG"));
            registration.setUser(user);
            registration.setRegistrationDate(new Date(System.currentTimeMillis()));
            registration.setType("Hiến máu");
            registration.setStatus("Chờ duyệt");
            donationRegistrationRepository.save(registration);
            System.out.println("DEBUG: Đã lưu donation_registrations thành công");

            // Lưu vào donation_requests (để admin duyệt)
            System.out.println("DEBUG: Bắt đầu tạo donationRequest");
            DonationRequest donationRequest = new DonationRequest();

            donationRequest.setRequestId(generateId("DR"));
            System.out.println("DEBUG: setRequestId xong: " + donationRequest.getRequestId());

            donationRequest.setUser(user);
            System.out.println("DEBUG: setUser xong");

            donationRequest.setRequestType(DonationRequest.RequestType.donate);
            System.out.println("DEBUG: setRequestType xong");

            donationRequest.setStatus(DonationRequest.RequestStatus.open);
            System.out.println("DEBUG: setStatus xong");

            donationRequest.setUrgencyLevel(DonationRequest.UrgencyLevel.medium);
            System.out.println("DEBUG: setUrgencyLevel xong");

            donationRequest.setBloodTypeNeeded((String) request.get("bloodType"));
            System.out.println("DEBUG: setBloodTypeNeeded xong: " + donationRequest.getBloodTypeNeeded());

            // Xử lý quantity với kiểu dữ liệu an toàn
            Object quantityObj = request.get("quantity");
            Integer quantity = 350; // mặc định
            if (quantityObj instanceof Integer) {
                quantity = (Integer) quantityObj;
            } else if (quantityObj instanceof Number) {
                quantity = ((Number) quantityObj).intValue();
            } else if (quantityObj != null) {
                try {
                    quantity = Integer.parseInt(quantityObj.toString());
                } catch (NumberFormatException e) {
                    System.out.println("DEBUG: Lỗi parse quantity, dùng giá trị mặc định: " + e.getMessage());
                }
            }
            donationRequest.setQuantity(quantity);
            System.out.println("DEBUG: setQuantity xong: " + donationRequest.getQuantity());

            donationRequest.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            System.out.println("DEBUG: setCreatedAt xong");

            // Xử lý desiredDate nếu có
            if (request.get("desiredDate") != null) {
                try {
                    String dateStr = request.get("desiredDate").toString().replace("T", " ") + ":00";
                    donationRequest.setDesiredDate(Timestamp.valueOf(dateStr));
                    System.out.println("DEBUG: setDesiredDate xong: " + donationRequest.getDesiredDate());
                } catch (Exception ex) {
                    System.out.println("DEBUG: Lỗi parse desiredDate: " + ex.getMessage());
                }
            }

            System.out.println("DEBUG: Trước khi save donationRequest");
            donationRequestRepository.save(donationRequest);
            System.out.println("DEBUG: Đã lưu donationRequest thành công với ID: " + donationRequest.getRequestId());

            return ResponseEntity.ok("Đăng ký hiến máu thành công! Vui lòng chờ duyệt.");
        } catch (Exception e) {
            System.err.println("ERROR trong registerDonation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // Đăng ký cần máu - tạo DonationRequest trực tiếp
    @PostMapping("/need-blood")
    public ResponseEntity<?> registerNeedBlood(@RequestBody Map<String, Object> request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            DonationRequest donationRequest = new DonationRequest();
            donationRequest.setRequestId(generateId("DR"));
            donationRequest.setUser(user);
            donationRequest.setBloodTypeNeeded((String) request.get("bloodType"));

            // Xử lý quantity an toàn
            Object quantityObj = request.get("quantity");
            Integer quantity = 350;
            if (quantityObj instanceof Integer) {
                quantity = (Integer) quantityObj;
            } else if (quantityObj instanceof Number) {
                quantity = ((Number) quantityObj).intValue();
            } else if (quantityObj != null) {
                try {
                    quantity = Integer.parseInt(quantityObj.toString());
                } catch (NumberFormatException e) {
                    System.out.println("DEBUG: Lỗi parse quantity, dùng giá trị mặc định: " + e.getMessage());
                }
            }
            donationRequest.setQuantity(quantity);

            // Chỉ nhận urgencyLevel là 'high' hoặc 'low'
            String urgency = (String) request.get("urgencyLevel");
            DonationRequest.UrgencyLevel urgencyLevel;
            if ("high".equalsIgnoreCase(urgency)) {
                urgencyLevel = DonationRequest.UrgencyLevel.high;
            } else {
                urgencyLevel = DonationRequest.UrgencyLevel.low;
            }
            donationRequest.setUrgencyLevel(urgencyLevel);

            donationRequest.setRequestType(DonationRequest.RequestType.receive);
            donationRequest.setStatus(DonationRequest.RequestStatus.open);
            donationRequest.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            if (request.get("desiredDate") != null) {
                String dateStr = request.get("desiredDate").toString().replace("T", " ") + ":00";
                try {
                    donationRequest.setDesiredDate(Timestamp.valueOf(dateStr));
                } catch (Exception ex) {
                    System.out.println("Lỗi parse desiredDate: " + ex.getMessage());
                }
            }

            // Nếu có location thì lưu vào trường center hoặc custom field nếu cần
            // (Bỏ qua nếu không dùng)

            donationRequestRepository.save(donationRequest);

            return ResponseEntity.ok("Đăng ký cần máu thành công! Vui lòng chờ duyệt.");
        } catch (Exception e) {
            System.err.println("ERROR trong registerNeedBlood: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // Lấy danh sách đăng ký hiến máu theo user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DonationRegistration>> getUserRegistrations(@PathVariable String userId) {
        List<DonationRegistration> registrations = donationRegistrationRepository
                .findByUser_UserIdOrderByRegistrationDateDesc(userId);
        return ResponseEntity.ok(registrations);
    }

    // Duyệt đăng ký hiến máu - chuyển thành Donation
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveDonationRegistration(@PathVariable String id) {
        try {
            DonationRegistration registration = donationRegistrationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Registration not found"));

            // Tạo Donation từ DonationRegistration
            Donation donation = new Donation();
            donation.setDonationId(generateId("DN"));
            donation.setUser(registration.getUser());
            donation.setDonationType(Donation.DonationType.whole);
            donation.setAmount(350); // Mặc định
            donation.setDate(new Timestamp(System.currentTimeMillis()));
            donation.setStatus("Chờ khám");

            donationRepository.save(donation);

            // Cập nhật trạng thái đăng ký
            registration.setStatus("Đã duyệt");
            donationRegistrationRepository.save(registration);

            return ResponseEntity.ok("Đã duyệt đăng ký hiến máu thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // Từ chối đăng ký hiến máu
    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectDonationRegistration(@PathVariable String id) {
        try {
            DonationRegistration registration = donationRegistrationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Registration not found"));

            registration.setStatus("Từ chối");
            donationRegistrationRepository.save(registration);

            return ResponseEntity.ok("Đã từ chối đăng ký hiến máu!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // Lấy tất cả đăng ký hiến máu (cho admin)
    @GetMapping("/all")
    public ResponseEntity<List<DonationRegistration>> getAllRegistrations() {
        List<DonationRegistration> registrations = donationRegistrationRepository.findAll();
        return ResponseEntity.ok(registrations);
    }

    private String generateId(String prefix) {
        return prefix + System.currentTimeMillis();
    }
}