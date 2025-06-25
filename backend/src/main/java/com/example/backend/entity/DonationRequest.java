package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.example.backend.entity.DonationRequest.UrgencyLevel;

@Entity
@Table(name = "donation_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DonationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer requestId;

    @ManyToOne
    @JoinColumn(name = "center_id")
    @JsonManagedReference
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
    @JsonManagedReference
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type")
    private RequestType requestType;

    public enum RequestType {
        donate, receive;
    }

    public enum UrgencyLevel {
        low, medium, high;

        @com.fasterxml.jackson.annotation.JsonCreator
        public static UrgencyLevel fromString(String value) {
            if (value == null)
                throw new IllegalArgumentException("Cấp độ khẩn cấp không được để trống");
            try {
                return UrgencyLevel.valueOf(value.toLowerCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Cấp độ khẩn cấp không hợp lệ: " + value);
            }
        }
    }

    public enum RequestStatus {
        open, approved, fulfilled, closed;

        public String toVietnamese() {
            switch (this) {
                case open:
                    return "Đang chờ";
                case approved:
                    return "Đã duyệt";
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
