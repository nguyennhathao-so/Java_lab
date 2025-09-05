package com.example.backend.service;

import com.example.backend.dto.BloodCompatibilityResponse;
import java.util.List;

public interface BloodCompatibilityService {
    List<BloodCompatibilityResponse> searchBloodCompatibility(String bloodType, String transfusionType, String bloodComponent);
} 