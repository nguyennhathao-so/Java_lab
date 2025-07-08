package com.example.backend.controller;

import com.example.backend.dto.SearchRequestDto;
import com.example.backend.dto.SearchResultDto;
import com.example.backend.entity.DonationRegistration;
import com.example.backend.entity.HealthCenter;
import com.example.backend.entity.User;
import com.example.backend.entity.DonationRequest;
import com.example.backend.repository.DonationRegistrationRepository;
import com.example.backend.repository.DonationRequestRepository;
import com.example.backend.repository.HealthCenterRepository;
import com.example.backend.repository.DonationRequestRepository;
import com.example.backend.service.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class SearchController {
    @Autowired
    private DonationRegistrationRepository donationRegistrationRepository;
    @Autowired
    private GeocodingService geocodingService;
    @Autowired
    private HealthCenterRepository healthCenterRepository;
    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @PostMapping("/search-donors")
    public List<SearchResultDto> searchDonors(@RequestBody SearchRequestDto request) {
        // 1. Lấy tọa độ người tìm kiếm
        String location = request.getLocation();
        double[] searchLatLng;
        if (location.matches("^-?\\d+(\\.\\d+)?,-?\\d+(\\.\\d+)?$")) {
            // Đã là tọa độ
            String[] parts = location.split(",");
            searchLatLng = new double[]{Double.parseDouble(parts[0]), Double.parseDouble(parts[1])};
        } else {
            // Là địa chỉ, cần geocode
            String latlng = geocodingService.geocodeAddressWithTimeout(location);
            String[] parts = latlng.split(",");
            searchLatLng = new double[]{Double.parseDouble(parts[0]), Double.parseDouble(parts[1])};
        }

        // 2. Lấy danh sách yêu cầu hiến/cần máu đã hoàn thành (fulfilled)
        List<com.example.backend.entity.DonationRequest> requests = donationRequestRepository.findByStatus(com.example.backend.entity.DonationRequest.RequestStatus.fulfilled);
        List<SearchResultDto> results = new ArrayList<>();
        for (com.example.backend.entity.DonationRequest dr : requests) {
            User user = dr.getUser();
            if (user == null) continue;
            if (!user.getBloodType().equalsIgnoreCase(request.getBloodType())) continue;
            if (user.getLocation() == null) continue;
            String[] userLatLng = user.getLocation().split(",");
            if (userLatLng.length != 2) continue;
            double lat, lng;
            try {
                lat = Double.parseDouble(userLatLng[0]);
                lng = Double.parseDouble(userLatLng[1]);
            } catch (NumberFormatException e) {
                continue;
            }
            double distance = haversine(searchLatLng[0], searchLatLng[1], lat, lng);
            if (distance <= request.getMaxDistance()) {
                String typeStr = dr.getRequestType() == com.example.backend.entity.DonationRequest.RequestType.donate ? "Hiến máu" : "Cần máu";
                results.add(new SearchResultDto(
                    user.getName(),
                    user.getPhone(),
                    user.getBloodType(),
                    typeStr,
                    user.getAddress(),
                    Math.round(distance * 100.0) / 100.0 // Làm tròn 2 chữ số thập phân
                ));
            }
        }
        // Sắp xếp theo khoảng cách tăng dần
        results.sort(Comparator.comparingDouble(SearchResultDto::getDistance));
        return results;
    }

    @GetMapping("/health-centers")
    public List<HealthCenter> getAllHealthCenters() {
        return healthCenterRepository.findAll();
    }

    // Hàm tính khoảng cách Haversine
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in Km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
} 