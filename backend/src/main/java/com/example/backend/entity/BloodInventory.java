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
@Table(name = "blood_inventory")
public class BloodInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer inventoryId;

    @ManyToOne
    @JoinColumn(name = "center_id", nullable = false)
    private HealthCenter center;

    @Column(name = "blood_type", nullable = false)
    private String bloodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "component_type", nullable = false)
    private ComponentType componentType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InventoryStatus status;

    @ManyToOne
    @JoinColumn(name = "donation_id")
    private Donation donation;

    public enum ComponentType {
        whole, platelets, plasma
    }

    public enum InventoryStatus {
        available, reserved, expired, used
    }
}