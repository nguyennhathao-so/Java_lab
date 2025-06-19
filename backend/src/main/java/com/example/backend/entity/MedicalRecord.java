package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_records")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    @Column(name = "disease_history", columnDefinition = "text")
    private String diseaseHistory;

    @Column(name = "checked_date")
    private Date checkedDate;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;
} 