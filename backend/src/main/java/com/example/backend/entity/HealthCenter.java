package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.backend.config.EntityIdListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "health_centers")
@EntityListeners(EntityIdListener.class)
public class HealthCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_id")
    private String centerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "location")
    private String location;
}