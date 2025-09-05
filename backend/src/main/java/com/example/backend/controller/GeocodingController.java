package com.example.backend.controller;

import com.example.backend.service.GeocodingService;
import com.example.backend.service.LocationUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/geocoding")
@CrossOrigin(origins = "*")
public class GeocodingController {
    
    private final GeocodingService geocodingService;
    private final LocationUpdateService locationUpdateService;
    
    @Autowired
    public GeocodingController(GeocodingService geocodingService, LocationUpdateService locationUpdateService) {
        this.geocodingService = geocodingService;
        this.locationUpdateService = locationUpdateService;
    }
    
    /**
     * Test URL geocoding và xem dữ liệu thực tế từ API
     */
    @GetMapping("/test-url")
    public ResponseEntity<Map<String, Object>> testGeocodingUrl(@RequestParam String address) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String result = geocodingService.testGeocodingUrl(address);
            response.put("success", true);
            response.put("data", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Cập nhật tọa độ cho tất cả user chưa có location
     */
    @PostMapping("/update-all-users")
    public ResponseEntity<Map<String, Object>> updateAllUsersLocations() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            locationUpdateService.updateLocationsForAllUsers();
            response.put("success", true);
            response.put("message", "Đã cập nhật tọa độ cho tất cả user");
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Cập nhật tọa độ cho một user cụ thể
     */
    @PostMapping("/update-user/{userId}")
    public ResponseEntity<Map<String, Object>> updateUserLocation(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = locationUpdateService.updateLocationForUser(userId);
            response.put("success", true);
            response.put("message", success ? "Đã cập nhật tọa độ thành công" : "Không thể cập nhật tọa độ");
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
} 