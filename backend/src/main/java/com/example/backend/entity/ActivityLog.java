package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import com.example.backend.config.EntityIdListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activity_logs")
@EntityListeners(EntityIdListener.class)
public class ActivityLog {
    @Id
    @Column(name = "log_id")
    private String logId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "action")
    private String action;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "timestamp")
    private Timestamp timestamp;
} 