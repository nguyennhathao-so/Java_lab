package com.example.backend.util;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdGenerator {
    
    private static final ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();
    
    // Prefix cho từng bảng
    public static final String USER_PREFIX = "US";
    public static final String ROLE_PREFIX = "RL";
    public static final String DONATION_REQUEST_PREFIX = "DR";
    public static final String HEALTH_CENTER_PREFIX = "HC";
    public static final String BLOOD_INVENTORY_PREFIX = "BI";
    public static final String DONATION_PREFIX = "DN";
    public static final String ACTIVITY_LOG_PREFIX = "AL";
    public static final String APPOINTMENT_PREFIX = "AP";
    public static final String BLOG_PREFIX = "BL";
    public static final String MEDICAL_RECORD_PREFIX = "MR";
    public static final String NOTIFICATION_PREFIX = "NT";
    public static final String DONATION_REGISTRATION_PREFIX = "DRG";
    
    /**
     * Tạo ID mới cho entity
     * @param prefix Prefix của bảng (ví dụ: "US" cho User)
     * @return ID theo format: prefix + số thứ tự (ví dụ: US1, US2, ...)
     */
    public static String generateId(String prefix) {
        AtomicInteger counter = counters.computeIfAbsent(prefix, k -> new AtomicInteger(0));
        int nextNumber = counter.incrementAndGet();
        return prefix + nextNumber;
    }
    
    /**
     * Tạo ID cho User
     */
    public static String generateUserId() {
        return generateId(USER_PREFIX);
    }
    
    /**
     * Tạo ID cho Role
     */
    public static String generateRoleId() {
        return generateId(ROLE_PREFIX);
    }
    
    /**
     * Tạo ID cho DonationRequest
     */
    public static String generateDonationRequestId() {
        return generateId(DONATION_REQUEST_PREFIX);
    }
    
    /**
     * Tạo ID cho HealthCenter
     */
    public static String generateHealthCenterId() {
        return generateId(HEALTH_CENTER_PREFIX);
    }
    
    /**
     * Tạo ID cho BloodInventory
     */
    public static String generateBloodInventoryId() {
        return generateId(BLOOD_INVENTORY_PREFIX);
    }
    
    /**
     * Tạo ID cho Donation
     */
    public static String generateDonationId() {
        return generateId(DONATION_PREFIX);
    }
    
    /**
     * Tạo ID cho ActivityLog
     */
    public static String generateActivityLogId() {
        return generateId(ACTIVITY_LOG_PREFIX);
    }
    
    /**
     * Tạo ID cho Appointment
     */
    public static String generateAppointmentId() {
        return generateId(APPOINTMENT_PREFIX);
    }
    
    /**
     * Tạo ID cho Blog
     */
    public static String generateBlogId() {
        return generateId(BLOG_PREFIX);
    }
    
    /**
     * Tạo ID cho MedicalRecord
     */
    public static String generateMedicalRecordId() {
        return generateId(MEDICAL_RECORD_PREFIX);
    }
    
    /**
     * Tạo ID cho Notification
     */
    public static String generateNotificationId() {
        return generateId(NOTIFICATION_PREFIX);
    }
    
    /**
     * Tạo ID cho DonationRegistration
     */
    public static String generateDonationRegistrationId() {
        return generateId(DONATION_REGISTRATION_PREFIX);
    }
    
    /**
     * Set counter cho prefix cụ thể (dùng cho initializer)
     */
    public static void setCounter(String prefix, int value) {
        counters.put(prefix, new AtomicInteger(value));
    }
    
    /**
     * Reset counter cho prefix cụ thể (dùng cho testing)
     */
    public static void resetCounter(String prefix) {
        counters.remove(prefix);
    }
    
    /**
     * Reset tất cả counters (dùng cho testing)
     */
    public static void resetAllCounters() {
        counters.clear();
    }
} 