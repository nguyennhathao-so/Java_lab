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
        List<User> users = userRepository.findByLastDonationDateIsNotNull();
        for (User user : users) {
            Date lastDonation = user.getLastDonationDate();
            if (lastDonation != null) {
                long days = ChronoUnit.DAYS.between(lastDonation.toLocalDate(), today);
                if (days > 15) {
                    boolean exists = notificationRepository.existsByUser_UserIdAndMessageTypeAndStatus(
                            user.getUserId(), "reminder", "unread");
                    if (!exists) {
                        Notification n = new Notification();
                        n.setNotificationId(generateNotificationId());
                        n.setUser(user);
                        n.setMessage("Đã đến thời gian có thể hiến máu lại.");
                        n.setMessageType("reminder");
                        n.setStatus("unread");
                        n.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                        n.setStaffMessage(null);
                        notificationRepository.save(n);
                    }
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