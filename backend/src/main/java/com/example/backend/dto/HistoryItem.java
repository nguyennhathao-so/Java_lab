package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryItem {
    private String type; // "Hiến máu" hoặc "Cần máu"
    private Integer id;
    private String name; // Tên người dùng hoặc tên trung tâm
    private String contact; // SĐT người dùng hoặc liên hệ trung tâm
    private String bloodType;
    private Integer amount;
    private String status;
    private Date date;
    private String gender; // Thêm giới tính
    private String email; // Thêm email
}