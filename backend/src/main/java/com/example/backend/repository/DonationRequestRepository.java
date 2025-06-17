package com.example.backend.repository;

import com.example.backend.entity.DonationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRequestRepository extends JpaRepository<DonationRequest, Integer> {

    @Query("SELECT dr FROM DonationRequest dr JOIN FETCH dr.center c ORDER BY dr.createdAt DESC")
    List<DonationRequest> findAllWithCenterOrderByCreatedAtDesc();

    List<DonationRequest> findByStatus(DonationRequest.RequestStatus status);
}