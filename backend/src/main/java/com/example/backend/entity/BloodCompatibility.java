package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blood_compatibility")
public class BloodCompatibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receiver_blood_type", nullable = false)
    private String receiverBloodType; // Nhóm máu người nhận

    @Column(name = "transfusion_type", nullable = false)
    private String transfusionType; // Loại truyền máu: "Toàn Phần" hoặc "Theo Thành Phần Máu"

    @Column(name = "blood_component")
    private String bloodComponent; // Thành phần máu: "Hồng Cầu", "Tiểu Cầu", null cho toàn phần

    @Column(name = "compatible_blood_types", columnDefinition = "text", nullable = false)
    private String compatibleBloodTypes; // Nhóm máu có thể nhận (phân cách bằng dấu phẩy)
} 