package com.example.backend.service.impl;

import com.example.backend.dto.BloodCompatibilityResponse;
import com.example.backend.service.BloodCompatibilityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;

@Service
public class BloodCompatibilityServiceImpl implements BloodCompatibilityService {
    @Override
    public List<BloodCompatibilityResponse> searchBloodCompatibility(String bloodType, String transfusionType, String bloodComponent) {
        // TODO: Implement actual logic
        return Collections.emptyList();
    }
} 