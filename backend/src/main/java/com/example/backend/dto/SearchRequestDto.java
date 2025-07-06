package com.example.backend.dto;

import lombok.Data;

@Data
public class SearchRequestDto {
    private String bloodType;
    private String location; // Địa chỉ hoặc "lat,lon"
    private double maxDistance; // Đơn vị: Km
} 