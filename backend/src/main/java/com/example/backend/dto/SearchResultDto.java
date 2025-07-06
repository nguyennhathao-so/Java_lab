package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResultDto {
    private String name;
    private String phone;
    private String bloodType;
    private String type; // Hiến máu/Cần máu
    private String address;
    private double distance;
} 