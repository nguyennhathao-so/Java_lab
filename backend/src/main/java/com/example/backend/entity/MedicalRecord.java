package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Date;

@Data
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

    @Column(name = "disease_history")
    private String diseaseHistory;

    @Column(name = "checked_date")
    private Date checkedDate;

    @Column(name = "notes")
    private String notes;
}
