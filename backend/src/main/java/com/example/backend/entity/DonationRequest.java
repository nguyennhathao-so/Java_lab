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
@Table(name = "donation_requests")
public class DonationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer requestId;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private HealthCenter center;

    @Column(name = "blood_type_needed")
    private String bloodTypeNeeded;

    @Column(name = "quantity")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level")
    private UrgencyLevel urgencyLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public enum UrgencyLevel {
        low, medium, high
    }

    public enum RequestStatus {
        open, fulfilled, closed;

        public String toVietnamese() {
            switch (this) {
                case open:
                    return "Đang chờ";
                case fulfilled:
                    return "Đã đáp ứng";
                case closed:
                    return "Đã đóng";
                default:
                    return this.name();
            }
        }
    }
}