package com.example.backend.controller;

import com.example.backend.entity.MedicalRecord;
import com.example.backend.entity.User;
import com.example.backend.repository.MedicalRecordRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/medical-records")
@CrossOrigin(origins = "*")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private UserRepository userRepository;

    // Tạo mới medical record
    @PostMapping("")
    public ResponseEntity<?> createMedicalRecord(@RequestBody Map<String, Object> request) {
        try {
            String userId = (String) request.get("user_id");
            String diseaseHistory = (String) request.get("disease_history");
            User user = userRepository.findById(userId).orElse(null);

            MedicalRecord record = new MedicalRecord();
            record.setUser(user);
            record.setDiseaseHistory(diseaseHistory);
            record.setCheckedDate(new Date(System.currentTimeMillis()));
            // set các trường khác nếu cần

            medicalRecordRepository.save(record);
            return ResponseEntity.ok("Medical record created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating medical record: " + e.getMessage());
        }
    }

    // Lấy record mới nhất của user
    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<?> getLatestMedicalRecord(@PathVariable String userId) {
        MedicalRecord record = medicalRecordRepository.findTopByUser_UserIdOrderByCheckedDateDesc(userId);
        return ResponseEntity.ok(record);
    }
}