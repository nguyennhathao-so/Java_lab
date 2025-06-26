package com.example.backend.service;

import com.example.backend.entity.HealthCenter;
import com.example.backend.entity.BloodInventory;
import com.example.backend.repository.HealthCenterRepository;
import com.example.backend.repository.BloodInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BloodSearchService {
    @Autowired
    private HealthCenterRepository healthCenterRepository;
    @Autowired
    private BloodInventoryRepository bloodInventoryRepository;

    public List<Map<String, Object>> search(double lat, double lng, double radius, String bloodType) {
        List<HealthCenter> centers = healthCenterRepository.findByDistance(lat, lng, radius);
        List<Map<String, Object>> result = new ArrayList<>();
        for (HealthCenter center : centers) {
            List<BloodInventory> inventories;
            if (bloodType != null && !bloodType.isEmpty()) {
                inventories = bloodInventoryRepository.findByCenterAndBloodType(center, bloodType);
            } else {
                inventories = bloodInventoryRepository.findByCenter(center);
            }
            if (!inventories.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("center", center);
                map.put("inventories", inventories);
                result.add(map);
            }
        }
        return result;
    }
} 