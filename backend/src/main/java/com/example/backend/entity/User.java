package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.sql.Timestamp;
import com.example.backend.config.EntityIdListener;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(EntityIdListener.class)
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "blood_type", nullable = false)
    private String bloodType;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "last_donation_date")
    private Date lastDonationDate;

    // Nếu muốn map location (kiểu point), cần custom type, tạm thời để String hoặc
    // byte[]
    @Column(name = "location")
    private String location;

    @Column(name = "status")
    private String status;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "phone")
    private String phone;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private java.util.List<DonationRegistration> donationRegistrations;
}