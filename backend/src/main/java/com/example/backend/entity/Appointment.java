package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.sql.Date;
import com.example.backend.config.EntityIdListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
@EntityListeners(EntityIdListener.class)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private String appointmentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "center_id", nullable = false)
    private HealthCenter center;

    @Column(name = "scheduled_time", nullable = false)
    private Timestamp scheduledTime;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "donation_id")
    private Donation donation;
} 