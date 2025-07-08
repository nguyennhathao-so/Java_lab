package com.example.backend.repository;

import com.example.backend.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, String> {

    @Query("SELECT d FROM Donation d JOIN FETCH d.user u ORDER BY d.date DESC")
    List<Donation> findAllWithUserOrderByDateDesc();

    @Query("SELECT d FROM Donation d JOIN FETCH d.user u WHERE d.status = :status ORDER BY d.date DESC")
    List<Donation> findByStatus(String status);

    List<Donation> findByUser_UserId(String userId);
    
    Donation findTopByUser_UserIdAndStatusOrderByDateDesc(String userId, String status);
    
    @Modifying
    @Query("DELETE FROM Donation d WHERE d.user.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);
}