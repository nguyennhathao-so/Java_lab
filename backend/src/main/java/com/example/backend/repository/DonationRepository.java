package com.example.backend.repository;

import com.example.backend.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Integer> {

    @Query("SELECT d FROM Donation d JOIN FETCH d.user u ORDER BY d.date DESC")
    List<Donation> findAllWithUserOrderByDateDesc();

    List<Donation> findByUser_UserId(Integer userId);
}