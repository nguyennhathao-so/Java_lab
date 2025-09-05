package com.example.backend.repository;

import com.example.backend.entity.Notification;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findAllByOrderByCreatedAtDesc();
    List<Notification> findByUser_UserIdOrderByCreatedAtDesc(String userId);
    boolean existsByUser_UserIdAndMessageTypeAndStatus(String userId, String messageType, String status);
    
    // Method để đếm số lượng notifications
    long count();
    
    // Method để save notification
    Notification save(Notification notification);
    
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.user.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);
} 