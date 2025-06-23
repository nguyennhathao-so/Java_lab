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
@Table(name = "donations")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_id")
    private Integer donationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private DonationRequest request;

    @Enumerated(EnumType.STRING)
    @Column(name = "donation_type")
    private DonationType donationType;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "status")
    private String status;

    public enum DonationType {
        whole, platelets, plasma
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}