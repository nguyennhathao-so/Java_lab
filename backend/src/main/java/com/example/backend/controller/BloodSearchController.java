package com.example.backend.controller;

import com.example.backend.service.BloodSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blood")
public class BloodSearchController {
    @Autowired
    private BloodSearchService bloodSearchService;

    @GetMapping("/search")
    public ResponseEntity<?> searchBlood(
        @RequestParam double lat,
        @RequestParam double lng,
        @RequestParam double radius,
        @RequestParam(required = false) String bloodType
    ) {
        List<Map<String, Object>> result = bloodSearchService.search(lat, lng, radius, bloodType);
        return ResponseEntity.ok(result);
    }
} 