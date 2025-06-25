package com.example.backend.dto;

import com.example.backend.entity.Donation.DonationType;
import lombok.Data;

import java.security.Timestamp;
import java.sql.Date;

@Data
public class DonationRegistrationRequest {
    private Timestamp donationDate;
    private DonationType donationType;
}