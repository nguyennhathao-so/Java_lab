package com.example.backend.repository;

import com.example.backend.entity.DonationRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRegistrationRepository extends JpaRepository<DonationRegistration, String> {
    List<DonationRegistration> findByUser_UserIdOrderByRegistrationDateDesc(String userId);
    List<DonationRegistration> findByStatus(String status);
    
    @Modifying
    @Query("DELETE FROM DonationRegistration dr WHERE dr.user.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);
}
