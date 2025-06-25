package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import com.example.backend.config.EntityIdListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donations")
@EntityListeners(EntityIdListener.class)
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_id")
    private String donationId;

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
    private Date date;

    public enum DonationType {
        whole, platelets, plasma
    }
}