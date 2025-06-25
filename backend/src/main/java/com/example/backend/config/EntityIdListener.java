package com.example.backend.config;

import com.example.backend.entity.*;
import com.example.backend.util.IdGenerator;
import jakarta.persistence.PrePersist;

public class EntityIdListener {
    
    @PrePersist
    public void generateId(Object entity) {
        if (entity instanceof User) {
            User user = (User) entity;
            if (user.getUserId() == null) {
                user.setUserId(IdGenerator.generateUserId());
            }
        } else if (entity instanceof Role) {
            Role role = (Role) entity;
            if (role.getRoleId() == null) {
                role.setRoleId(IdGenerator.generateRoleId());
            }
        } else if (entity instanceof DonationRequest) {
            DonationRequest request = (DonationRequest) entity;
            if (request.getRequestId() == null) {
                request.setRequestId(IdGenerator.generateDonationRequestId());
            }
        } else if (entity instanceof HealthCenter) {
            HealthCenter center = (HealthCenter) entity;
            if (center.getCenterId() == null) {
                center.setCenterId(IdGenerator.generateHealthCenterId());
            }
        } else if (entity instanceof BloodInventory) {
            BloodInventory inventory = (BloodInventory) entity;
            if (inventory.getInventoryId() == null) {
                inventory.setInventoryId(IdGenerator.generateBloodInventoryId());
            }
        } else if (entity instanceof Donation) {
            Donation donation = (Donation) entity;
            if (donation.getDonationId() == null) {
                donation.setDonationId(IdGenerator.generateDonationId());
            }
        } else if (entity instanceof ActivityLog) {
            ActivityLog log = (ActivityLog) entity;
            if (log.getLogId() == null) {
                log.setLogId(IdGenerator.generateActivityLogId());
            }
        } else if (entity instanceof Appointment) {
            Appointment appointment = (Appointment) entity;
            if (appointment.getAppointmentId() == null) {
                appointment.setAppointmentId(IdGenerator.generateAppointmentId());
            }
        } else if (entity instanceof Blog) {
            Blog blog = (Blog) entity;
            if (blog.getBlogId() == null) {
                blog.setBlogId(IdGenerator.generateBlogId());
            }
        } else if (entity instanceof MedicalRecord) {
            MedicalRecord record = (MedicalRecord) entity;
            if (record.getRecordId() == null) {
                record.setRecordId(IdGenerator.generateMedicalRecordId());
            }
        } else if (entity instanceof Notification) {
            Notification notification = (Notification) entity;
            if (notification.getNotificationId() == null) {
                notification.setNotificationId(IdGenerator.generateNotificationId());
            }
        } else if (entity instanceof DonationRegistration) {
            DonationRegistration reg = (DonationRegistration) entity;
            if (reg.getId() == null) {
                reg.setId(IdGenerator.generateDonationRegistrationId());
            }
        }
    }
} 