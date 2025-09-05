package com.example.backend.config;

import com.example.backend.util.IdGenerator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class IdGeneratorInitializer {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    @Transactional
    public void initialize() {
        // Khởi tạo cho tất cả entities
        initializeCounter(IdGenerator.USER_PREFIX, "User", "userId");
        initializeCounter(IdGenerator.ROLE_PREFIX, "Role", "roleId");
        initializeCounter(IdGenerator.DONATION_REQUEST_PREFIX, "DonationRequest", "requestId");
        initializeCounter(IdGenerator.HEALTH_CENTER_PREFIX, "HealthCenter", "centerId");
        initializeCounter(IdGenerator.BLOOD_INVENTORY_PREFIX, "BloodInventory", "inventoryId");
        initializeCounter(IdGenerator.DONATION_PREFIX, "Donation", "donationId");
        initializeCounter(IdGenerator.ACTIVITY_LOG_PREFIX, "ActivityLog", "logId");
        initializeCounter(IdGenerator.APPOINTMENT_PREFIX, "Appointment", "appointmentId");
        initializeCounter(IdGenerator.BLOG_PREFIX, "Blog", "blogId");
        initializeCounter(IdGenerator.MEDICAL_RECORD_PREFIX, "MedicalRecord", "recordId");
        initializeCounter(IdGenerator.NOTIFICATION_PREFIX, "Notification", "notificationId");
    }

    private void initializeCounter(String prefix, String entityName, String idFieldName) {
        String queryString = String.format("SELECT e.%s FROM %s e WHERE e.%s LIKE :prefix", idFieldName, entityName,
                idFieldName);
        Query query = entityManager.createQuery(queryString);
        query.setParameter("prefix", prefix + "%");

        List<String> ids = query.getResultList();

        long maxId = 0;
        Pattern pattern = Pattern.compile(prefix + "(\\d+)");

        for (String id : ids) {
            Matcher matcher = pattern.matcher(id);
            if (matcher.matches()) {
                long currentId = Long.parseLong(matcher.group(1));
                if (currentId > maxId) {
                    maxId = currentId;
                }
            }
        }

        IdGenerator.setCounter(prefix, (int) maxId);
    }
}