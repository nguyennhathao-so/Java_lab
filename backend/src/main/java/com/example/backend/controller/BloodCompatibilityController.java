package com.example.backend.controller;

import com.example.backend.dto.BloodCompatibilityResponse;
import com.example.backend.service.BloodCompatibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-compatibility")
@CrossOrigin(origins = "*")
public class BloodCompatibilityController {

    @Autowired
    private BloodCompatibilityService bloodCompatibilityService;

    @GetMapping("/search")
    public ResponseEntity<List<BloodCompatibilityResponse>> searchBloodCompatibility(
            @RequestParam String bloodType,
            @RequestParam String transfusionType,
            @RequestParam(required = false) String bloodComponent) {
        
        List<BloodCompatibilityResponse> results = bloodCompatibilityService.searchBloodCompatibility(bloodType, transfusionType, bloodComponent);
        return ResponseEntity.ok(results);
    }
} 