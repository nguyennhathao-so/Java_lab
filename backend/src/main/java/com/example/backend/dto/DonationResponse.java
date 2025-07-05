package com.example.backend.dto;

import com.example.backend.entity.User;
import java.util.Date;

public class DonationResponse {
    private Integer donationId;
    private User user;
    private Integer requestId;
    private String donationType;
    private Integer amount;
    private Date date;
    private String status;

    // Constructors
    public DonationResponse() {
    }

    public DonationResponse(Integer donationId, User user, Integer requestId, String donationType, Integer amount,
            Date date, String status) {
        this.donationId = donationId;
        this.user = user;
        this.requestId = requestId;
        this.donationType = donationType;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    // Getters and Setters
    public Integer getDonationId() {
        return donationId;
    }

    public void setDonationId(Integer donationId) {
        this.donationId = donationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getDonationType() {
        return donationType;
    }

    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}