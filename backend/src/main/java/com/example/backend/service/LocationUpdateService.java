package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationUpdateService {
    
    private final UserRepository userRepository;
    private final GeocodingService geocodingService;
    
    @Autowired
    public LocationUpdateService(UserRepository userRepository, GeocodingService geocodingService) {
        this.userRepository = userRepository;
        this.geocodingService = geocodingService;
    }
    
    /**
     * Cập nhật tọa độ cho tất cả user chưa có location
     */
    public void updateLocationsForAllUsers() {
        List<User> usersWithoutLocation = userRepository.findByLocationIsNull();
        
        for (User user : usersWithoutLocation) {
            if (user.getAddress() != null && !user.getAddress().trim().isEmpty()) {
                try {
                    String coordinates = geocodingService.geocodeAddressWithTimeout(user.getAddress());
                    if (coordinates != null) {
                        user.setLocation(coordinates);
                        userRepository.save(user);
                        System.out.println("Đã cập nhật tọa độ cho user " + user.getEmail() + ": " + coordinates);
                    } else {
                        System.out.println("Không thể chuyển đổi địa chỉ cho user " + user.getEmail() + ": " + user.getAddress());
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi cập nhật tọa độ cho user " + user.getEmail() + ": " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Cập nhật tọa độ cho một user cụ thể
     */
    public boolean updateLocationForUser(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }
        
        if (user.getAddress() != null && !user.getAddress().trim().isEmpty()) {
            try {
                String coordinates = geocodingService.geocodeAddressWithTimeout(user.getAddress());
                if (coordinates != null) {
                    user.setLocation(coordinates);
                    userRepository.save(user);
                    System.out.println("Đã cập nhật tọa độ cho user " + user.getEmail() + ": " + coordinates);
                    return true;
                } else {
                    System.out.println("Không thể chuyển đổi địa chỉ cho user " + user.getEmail() + ": " + user.getAddress());
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi cập nhật tọa độ cho user " + user.getEmail() + ": " + e.getMessage());
            }
        }
        
        return false;
    }
} 