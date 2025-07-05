package com.example.backend.dto;

import com.example.backend.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String role;
    private String name;
    private String email;
    private String userId;
    private String phone;
    private String address;
    private String gender;
}
