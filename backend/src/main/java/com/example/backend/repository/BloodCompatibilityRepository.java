package com.example.backend.repository;

import com.example.backend.entity.BloodCompatibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodCompatibilityRepository extends JpaRepository<BloodCompatibility, Long> {
    
    @Query("SELECT bc FROM BloodCompatibility bc WHERE bc.receiverBloodType = :bloodType AND bc.transfusionType = :transfusionType")
    List<BloodCompatibility> findByBloodTypeAndTransfusionType(@Param("bloodType") String bloodType, @Param("transfusionType") String transfusionType);
    
    @Query("SELECT bc FROM BloodCompatibility bc WHERE bc.receiverBloodType = :bloodType AND bc.transfusionType = :transfusionType AND bc.bloodComponent = :bloodComponent")
    List<BloodCompatibility> findByBloodTypeAndTransfusionTypeAndComponent(@Param("bloodType") String bloodType, @Param("transfusionType") String transfusionType, @Param("bloodComponent") String bloodComponent);
} 