package com.example.backend.service;

import com.example.backend.entity.Notification;
import com.example.backend.entity.User;
import com.example.backend.repository.NotificationRepository;
import com.example.backend.repository.UserRepository;
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
        // Lấy notification mới nhất loại approved hoặc rejected
        Notification latest = null;
        List<Notification> notis = notificationRepository.findByUser_UserIdOrderByCreatedAtDesc(user.getUserId());
        for (Notification n : notis) {
            if ("approved".equals(n.getMessageType()) || "rejected".equals(n.getMessageType())) {
                latest = n;
                break;
            }
        }
        if (latest != null && latest.getCreatedAt() != null) {
            long days = ChronoUnit.DAYS.between(latest.getCreatedAt().toLocalDateTime().toLocalDate(), today);
            if (days >= 15) {
                // Kiểm tra nếu đã có reminder nào được tạo sau notification approved/rejected mới nhất thì không tạo nữa
                boolean hasReminderAfter = false;
                for (Notification n : notis) {
                    if ("reminder".equals(n.getMessageType()) && n.getCreatedAt() != null &&
                        n.getCreatedAt().after(latest.getCreatedAt())) {
                        hasReminderAfter = true;
                        break;
                    }
                }
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
        // Lấy số lượng notification hiện tại + 1
        long count = notificationRepository.count() + 1;
        return "NT" + count;
    }
} 