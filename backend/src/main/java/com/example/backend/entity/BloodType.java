package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blood_types")
public class BloodType {
    @Id
    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "can_donate_to", columnDefinition = "text")
    private String canDonateTo;

    @Column(name = "can_receive_from", columnDefinition = "text")
    private String canReceiveFrom;
} 