package com.example.backend.service;

import com.example.backend.entity.Donation;
import com.example.backend.entity.Notification;
import com.example.backend.entity.User;
import com.example.backend.repository.DonationRepository;
import com.example.backend.repository.NotificationRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReminderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private DonationRepository donationRepository;

    //@Scheduled(cron = "0 0 8 * * ?") // chạy mỗi ngày lúc 8h sáng
    @Scheduled(fixedDelay = 60000) // chạy mỗi phút
    public void sendDonationReminders() {
        LocalDate today = LocalDate.now();
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            checkAndCreateReminderForUser(user);
        }
    }

    public void checkAndCreateReminderForUser(User user) {
        LocalDate today = LocalDate.now();
        // Lấy lần hiến máu gần nhất đã hoàn thành
        Donation lastDonation = donationRepository.findTopByUser_UserIdAndStatusOrderByDateDesc(
            user.getUserId(), "completed"
        );
        // Lấy danh sách notification của user, mới nhất trước
        List<Notification> notis = notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(user.getUserId());
        // Tìm notification 'approved' hoặc 'rejected' mới nhất
        Notification lastApprovedOrRejected = notis.stream()
            .filter(n -> n.getMessageType() != null && (n.getMessageType().equalsIgnoreCase("approved") || n.getMessageType().equalsIgnoreCase("rejected")))
            .findFirst().orElse(null);
        if (lastApprovedOrRejected != null && lastApprovedOrRejected.getCreatedAt() != null) {
            long days = ChronoUnit.DAYS.between(lastApprovedOrRejected.getCreatedAt().toLocalDateTime().toLocalDate(), today);
            if (days >= 15) {
                // Kiểm tra nếu đã có reminder sau notification này
                boolean hasReminderAfter = notis.stream().anyMatch(n ->
                    "reminder".equals(n.getMessageType()) &&
                    n.getCreatedAt() != null &&
                    n.getCreatedAt().after(lastApprovedOrRejected.getCreatedAt())
                );
                if (!hasReminderAfter) {
                    Notification reminder = new Notification();
                    reminder.setNotificationId(generateNotificationId());
                    reminder.setUser(user);
                    reminder.setMessage("Đã đến thời gian cần chú ý lại thông báo.");
                    reminder.setMessageType("reminder");
                    reminder.setStatus("unread");
                    reminder.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                    reminder.setStaffMessage(null);
                    notificationRepository.save(reminder);
                    return;
                }
            }
        }
        // Logic cũ: dựa vào lần hiến máu gần nhất
        if (lastDonation != null && lastDonation.getDate() != null) {
            long days = ChronoUnit.DAYS.between(lastDonation.getDate().toLocalDateTime().toLocalDate(), today);
            if (days >= 15) {
                boolean hasReminderAfter = notis.stream().anyMatch(n ->
                    "reminder".equals(n.getMessageType()) &&
                    n.getCreatedAt() != null &&
                    n.getCreatedAt().after(lastDonation.getDate())
                );
                if (!hasReminderAfter) {
                    Notification reminder = new Notification();
                    reminder.setNotificationId(generateNotificationId());
                    reminder.setUser(user);
                    reminder.setMessage("Đã đến thời gian có thể hiến máu lại.");
                    reminder.setMessageType("reminder");
                    reminder.setStatus("unread");
                    reminder.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                    reminder.setStaffMessage(null);
                    notificationRepository.save(reminder);
                }
            }
        }
    }

    // Hàm sinh notificationId dạng NT1, NT2, ...
    private String generateNotificationId() {
        return IdGenerator.generateNotificationId();
    }
} 