package com.example.backend.repository;

import com.example.backend.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, String> {
    
    List<ActivityLog> findByUser_UserId(String userId);
    
    @Modifying
    @Query("DELETE FROM ActivityLog a WHERE a.user.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);
} 