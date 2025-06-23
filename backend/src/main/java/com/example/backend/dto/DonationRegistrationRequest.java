package com.example.backend.dto;

import com.example.backend.entity.Donation.DonationType;
import lombok.Data;
import java.sql.Date;

@Data
public class DonationRegistrationRequest {
    private Date donationDate;
    private DonationType donationType;
}