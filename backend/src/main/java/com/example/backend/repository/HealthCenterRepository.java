package com.example.backend.repository;

import com.example.backend.entity.HealthCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthCenterRepository extends JpaRepository<HealthCenter, String> {
}