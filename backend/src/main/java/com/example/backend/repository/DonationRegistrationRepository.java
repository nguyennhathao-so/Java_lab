package com.example.backend.repository;

import com.example.backend.entity.DonationRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRegistrationRepository extends JpaRepository<DonationRegistration, Long> {
    List<DonationRegistration> findByUser_UserIdOrderByRegistrationDateDesc(String userId);
} 