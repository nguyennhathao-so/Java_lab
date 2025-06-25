package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @Column(name = "notification_id")
    private String notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "message_type")
    private String messageType;

    @Column(name = "staff_message", columnDefinition = "text")
    private String staffMessage;
} 