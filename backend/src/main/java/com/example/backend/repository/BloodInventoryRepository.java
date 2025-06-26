package com.example.backend.repository;

import com.example.backend.entity.BloodInventory;
import com.example.backend.entity.HealthCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, String> {

    @Query("SELECT bi FROM BloodInventory bi WHERE bi.status = 'available' GROUP BY bi.bloodType")
    List<BloodInventory> findAvailableBloodByType();

    @Query("SELECT bi.bloodType, SUM(bi.quantity) as totalQuantity FROM BloodInventory bi WHERE bi.status = 'available' GROUP BY bi.bloodType")
    List<Object[]> getBloodTypeSummary();

    List<BloodInventory> findByBloodTypeAndStatus(String bloodType, String status);

    List<BloodInventory> findByCenterAndBloodType(HealthCenter center, String bloodType);

    List<BloodInventory> findByCenter(HealthCenter center);
}