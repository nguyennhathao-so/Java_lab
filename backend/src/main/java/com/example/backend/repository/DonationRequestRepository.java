package com.example.backend.repository;

import com.example.backend.entity.DonationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.sql.Timestamp;

@Repository
public interface DonationRequestRepository extends JpaRepository<DonationRequest, String> {

    @Query("SELECT dr FROM DonationRequest dr JOIN FETCH dr.center c ORDER BY dr.createdAt DESC")
    List<DonationRequest> findAllWithCenterOrderByCreatedAtDesc();

    List<DonationRequest> findByStatus(DonationRequest.RequestStatus status);

    @Query("SELECT dr FROM DonationRequest dr LEFT JOIN FETCH dr.user u LEFT JOIN FETCH dr.center c WHERE dr.status = :status ORDER BY dr.createdAt DESC")
    List<DonationRequest> findByStatusWithUserAndCenter(@Param("status") DonationRequest.RequestStatus status);

    long countByStatus(DonationRequest.RequestStatus status);
}