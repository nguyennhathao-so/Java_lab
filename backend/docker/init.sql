-- Blood Donation Database Initialization Script
-- Tạo database nếu chưa có
CREATE DATABASE IF NOT EXISTS blood_donation_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
USE blood_donation_db;

-- Đảm bảo session luôn dùng utf8mb4
SET NAMES 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';

-- Bảng roles (tạo trước vì users tham chiếu đến)
CREATE TABLE IF NOT EXISTS `roles` (
  `role_id` varchar(10) NOT NULL,
  `role_name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng users (tạo sau roles)
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `blood_type` varchar(3) NOT NULL,
  `role_id` varchar(10) NOT NULL,
  `last_donation_date` date DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `status` enum('active','inactive') DEFAULT 'active',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `phone` varchar(15) DEFAULT NULL,
  `gender` varchar(5) DEFAULT NULL,
  `address` TEXT DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`),
  KEY `blood_type` (`blood_type`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng blood_types
CREATE TABLE IF NOT EXISTS `blood_types` (
  `blood_type` varchar(3) NOT NULL,
  `can_donate_to` text,
  `can_receive_from` text,
  PRIMARY KEY (`blood_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng health_centers
CREATE TABLE IF NOT EXISTS `health_centers` (
  `center_id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `contact_info` varchar(100) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`center_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng donation_requests
CREATE TABLE IF NOT EXISTS `donation_requests` (
  `request_id` varchar(10) NOT NULL,
  `user_id` varchar(10) DEFAULT NULL,
  `center_id` varchar(10) DEFAULT NULL,
  `blood_type_needed` varchar(3) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `urgency_level` enum('low','medium','high') DEFAULT 'medium',
  `status` enum('open','fulfilled','closed','approved') DEFAULT 'open',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `desired_date` datetime DEFAULT NULL,
  `request_type` enum('donate','receive') NOT NULL DEFAULT 'donate',
  PRIMARY KEY (`request_id`),
  KEY `user_id` (`user_id`),
  KEY `center_id` (`center_id`),
  CONSTRAINT `donation_requests_ibfk_1` FOREIGN KEY (`center_id`) REFERENCES `health_centers` (`center_id`),
  CONSTRAINT `donation_requests_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng donations
CREATE TABLE IF NOT EXISTS `donations` (
  `donation_id` varchar(10) NOT NULL,
  `user_id` varchar(10) DEFAULT NULL,
  `request_id` varchar(10) DEFAULT NULL,
  `donation_type` enum('whole','platelets','plasma') DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`donation_id`),
  KEY `user_id` (`user_id`),
  KEY `request_id` (`request_id`),
  CONSTRAINT `donations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `donations_ibfk_2` FOREIGN KEY (`request_id`) REFERENCES `donation_requests` (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng blood_inventory
CREATE TABLE IF NOT EXISTS `blood_inventory` (
  `inventory_id` varchar(10) NOT NULL,
  `center_id` varchar(10) NOT NULL,
  `blood_type` varchar(3) NOT NULL,
  `component_type` enum('whole','platelets','plasma') NOT NULL,
  `quantity` int NOT NULL DEFAULT '0',
  `expiry_date` date DEFAULT NULL,
  `status` enum('available','reserved','expired','used') DEFAULT 'available',
  `donation_id` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`inventory_id`),
  KEY `center_id` (`center_id`),
  KEY `donation_id` (`donation_id`),
  KEY `blood_type` (`blood_type`,`component_type`),
  CONSTRAINT `blood_inventory_ibfk_1` FOREIGN KEY (`center_id`) REFERENCES `health_centers` (`center_id`),
  CONSTRAINT `blood_inventory_ibfk_2` FOREIGN KEY (`blood_type`) REFERENCES `blood_types` (`blood_type`),
  CONSTRAINT `blood_inventory_ibfk_3` FOREIGN KEY (`donation_id`) REFERENCES `donations` (`donation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng appointments
CREATE TABLE IF NOT EXISTS `appointments` (
  `appointment_id` varchar(10) NOT NULL,
  `user_id` varchar(10) NOT NULL,
  `center_id` varchar(10) NOT NULL,
  `scheduled_time` datetime NOT NULL,
  `status` enum('scheduled','completed','cancelled','no_show') DEFAULT 'scheduled',
  `donation_id` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`appointment_id`),
  KEY `user_id` (`user_id`),
  KEY `center_id` (`center_id`),
  KEY `donation_id` (`donation_id`),
  CONSTRAINT `appointments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `appointments_ibfk_2` FOREIGN KEY (`center_id`) REFERENCES `health_centers` (`center_id`),
  CONSTRAINT `appointments_ibfk_3` FOREIGN KEY (`donation_id`) REFERENCES `donations` (`donation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng activity_logs
CREATE TABLE IF NOT EXISTS `activity_logs` (
  `log_id` varchar(10) NOT NULL,
  `user_id` varchar(10) DEFAULT NULL,
  `action` varchar(255) DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `activity_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng blogs
CREATE TABLE IF NOT EXISTS `blogs` (
  `blog_id` varchar(10) NOT NULL,
  `author_id` varchar(10) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`blog_id`),
  KEY `author_id` (`author_id`),
  CONSTRAINT `blogs_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng medical_records
CREATE TABLE IF NOT EXISTS `medical_records` (
  `record_id` varchar(10) NOT NULL,
  `user_id` varchar(10) DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `blood_pressure` varchar(20) DEFAULT NULL,
  `disease_history` text,
  `checked_date` date DEFAULT NULL,
  `notes` text,
  PRIMARY KEY (`record_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `medical_records_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng notifications
CREATE TABLE IF NOT EXISTS `notifications` (
  `notification_id` varchar(10) NOT NULL,
  `user_id` varchar(10) DEFAULT NULL,
  `message` text,
  `message_type` enum('approved','rejected','reminder') DEFAULT NULL,
  `staff_message` text,
  `status` enum('read','unread') DEFAULT 'unread',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notification_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng donation_registrations (lưu nhiều ngày đăng ký hiến máu cho mỗi user)
CREATE TABLE IF NOT EXISTS donation_registrations (
  id VARCHAR(10) NOT NULL,
  user_id VARCHAR(10) NOT NULL,
  registration_date DATE NOT NULL,
  type ENUM('Hiến máu','Cần máu') NOT NULL,
  status ENUM('Đã duyệt','Từ chối') NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bảng blood_compatibility (lưu thông tin tương thích máu)
CREATE TABLE IF NOT EXISTS blood_compatibility (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  receiver_blood_type VARCHAR(3) NOT NULL,
  transfusion_type VARCHAR(50) NOT NULL,
  blood_component VARCHAR(50),
  compatible_blood_types TEXT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- DỮ LIỆU MẪU CHO CÁC BẢNG CHÍNH

-- Bảng roles
INSERT INTO roles (role_id, role_name) VALUES ('RL1', 'USER'), ('RL2', 'ADMIN'), ('RL3', 'STAFF');

-- Bảng users
INSERT INTO users (user_id, name, email, password, blood_type, role_id, phone, gender, address, location)
VALUES
('US1', 'Nguyen Van A', 'a@gmail.com', '$2a$10$examplehash', 'A+', 'RL1', '0123456789', 'Nam', 'Hà Nội', '21.0285,105.8542'),
('US2', 'Nguyen Van B', 'b@gmail.com', '$2a$10$examplehash', 'B-', 'RL2', '0987654321', 'Nữ', 'Hồ Chí Minh', '10.8231,106.6297'),
('US3', 'Nguyen Van C', 'c@gmail.com', '$2b$10$examplehash', 'O+', 'RL1', '0987654320', 'Nữ', 'Hồ Chí Minh', '10.8231,106.6297');

-- Bảng health_centers
INSERT INTO health_centers (center_id, name, address, contact_info, location) VALUES
('HC1', 'QUẬN HOÀN KIẾM', '26 Lương Ngọc Quyến, HN', '(024) 3718 3154', '21.0341,105.8525'),
('HC2', 'QUẬN THANH XUÂN', '132 Quan Nhân, Hà Nội', '(024) 3207 9699', '21.0061,105.8122'),
('HC3', 'QUẬN ĐỐNG ĐA', 'Số 10, Ngõ 122, Đường Láng', '(024) 3203 0032', '21.0172,105.8165'),
('HC4', 'HUYỆN THANH TRÌ', 'BV ĐK Nông nghiệp, Km13+500, Ngọc Hồi, Hà Nội', '(024) 3200 0407', '20.9477,105.8571');

-- Bảng blood_types
INSERT INTO blood_types (blood_type, can_donate_to, can_receive_from) VALUES
('A+', 'A+,AB+', 'A+,A-,O+,O-'),
('A-', 'A+,A-,AB+,AB-', 'A-,O-'),
('B+', 'B+,AB+', 'B+,B-,O+,O-'),
('B-', 'B+,B-,AB+,AB-', 'B-,O-'),
('O+', 'A+,B+,O+,AB+', 'O+,O-'),
('O-', 'A+,A-,B+,B-,O+,O-,AB+,AB-', 'O-'),
('AB+', 'AB+', 'A+,A-,B+,B-,O+,O-,AB+,AB-'),
('AB-', 'AB+,AB-', 'A-,B-,O-,AB-');

-- Bảng blood_inventory
INSERT INTO blood_inventory (inventory_id, center_id, blood_type, component_type, quantity, status)
VALUES
('BI1', 'HC1', 'A+', 'whole', 25, 'available'),
('BI2', 'HC1', 'A-', 'whole', 15, 'available'),
('BI3', 'HC1', 'B+', 'whole', 30, 'available'),
('BI4', 'HC1', 'B-', 'whole', 12, 'available'),
('BI5', 'HC2', 'O+', 'whole', 45, 'available'),
('BI6', 'HC2', 'O-', 'whole', 18, 'available'),
('BI7', 'HC2', 'AB+', 'whole', 8, 'available'),
('BI8', 'HC2', 'AB-', 'whole', 5, 'available');

-- Bảng donation_requests
INSERT INTO donation_requests (request_id, user_id, center_id, blood_type_needed, quantity, urgency_level, status, request_type)
VALUES
('DR1', 'US1', 'HC1', 'A+', 2, 'high', 'open', 'donate'),
('DR2', 'US2', 'HC2', 'B-', 1, 'medium', 'open', 'receive');

-- Bảng donations
INSERT INTO donations (donation_id, user_id, donation_type, amount, date, status)
VALUES
('DN1', 'US1', 'whole', 350, '2024-06-01', 'completed'),
('DN2', 'US2', 'whole', 450, '2024-06-02', 'completed');

-- Bảng activity_logs
INSERT INTO activity_logs (log_id, user_id, action, ip_address)
VALUES
('AL1', 'US1', 'Đăng nhập', '127.0.0.1'),
('AL2', 'US2', 'Đăng ký', '127.0.0.1');

-- Bảng blogs
INSERT INTO blogs (blog_id, author_id, title, content)
VALUES
('BL1', 'US1', 'Hiến máu cứu người', 'Nội dung bài viết...'),
('BL2', 'US2', 'Lợi ích của hiến máu', 'Nội dung bài viết...');

-- Bảng medical_records
INSERT INTO medical_records (record_id, user_id, weight, blood_pressure, disease_history, checked_date, notes)
VALUES
('MR1', 'US1', 65, '120/80', 'Không', '2024-06-01', 'Khỏe mạnh'),
('MR2', 'US2', 55, '110/70', 'Không', '2024-06-02', 'Khỏe mạnh');

-- Bảng notifications
INSERT INTO notifications (notification_id, user_id, message, message_type, staff_message, status)
VALUES
('NT1', 'US1', 'Yêu cầu hiến máu của bạn tại Bệnh viện Chợ Rẫy đã được chấp nhận.', 'approved', 'Hồ sơ của bạn đã đạt yêu cầu. Vui lòng đến đúng giờ.', 'unread'),
('NT2', 'US2', 'Rất tiếc, yêu cầu hiến máu của bạn chưa được chấp nhận.', 'rejected', 'Khoảng cách thời gian từ lần hiến máu trước chưa đủ 3 tháng.', 'unread'),
('NT3', 'US1', 'Đã đến thời gian có thể hiến máu lại.', 'reminder', NULL, 'read');

-- Dữ liệu mẫu cho donation_registrations
INSERT INTO donation_registrations (id, user_id, registration_date, type, status) VALUES
('DRG1', 'US1', '2024-06-01', 'Hiến máu', 'Đã duyệt'),
('DRG2', 'US1', '2024-06-10', 'Cần máu', 'Từ chối'),
('DRG3', 'US2', '2024-06-05', 'Hiến máu', 'Đã duyệt'),
('DRG4', 'US3', '2024-06-12', 'Cần máu', 'Đã duyệt');

-- Dữ liệu mẫu cho blood_compatibility
-- Insert dữ liệu cho bảng blood_compatibility
-- Định dạng: receiver_blood_type, transfusion_type, blood_component, compatible_blood_types

-- A+ Toàn Phần
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('A+', 'Toàn Phần', NULL, 'A+, A-, O+, O-');

-- A+ Theo Thành Phần Máu - Hồng Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('A+', 'Theo Thành Phần Máu', 'Hồng Cầu', 'A+, A-, O+, O-');

-- A+ Theo Thành Phần Máu - Tiểu Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('A+', 'Theo Thành Phần Máu', 'Tiểu Cầu', 'A+, A-, B+, B-, AB+, AB-, O+, O- (Ưu Tiên A+)');

-- A- Toàn Phần
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('A-', 'Toàn Phần', NULL, 'A-, O-');

-- A- Theo Thành Phần Máu - Hồng Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('A-', 'Theo Thành Phần Máu', 'Hồng Cầu', 'A-, O-');

-- A- Theo Thành Phần Máu - Tiểu Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('A-', 'Theo Thành Phần Máu', 'Tiểu Cầu', 'A-, B-, AB-, O- (Ưu Tiên A-)');

-- B+ Toàn Phần
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('B+', 'Toàn Phần', NULL, 'B+, B-, O+, O-');

-- B+ Theo Thành Phần Máu - Hồng Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('B+', 'Theo Thành Phần Máu', 'Hồng Cầu', 'B+, B-, O+, O-');

-- B+ Theo Thành Phần Máu - Tiểu Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('B+', 'Theo Thành Phần Máu', 'Tiểu Cầu', 'A+, A-, B+, B-, AB+, AB-, O+, O- (Ưu Tiên B+)');

-- B- Toàn Phần
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('B-', 'Toàn Phần', NULL, 'B-, O-');

-- B- Theo Thành Phần Máu - Hồng Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('B-', 'Theo Thành Phần Máu', 'Hồng Cầu', 'B-, O-');

-- B- Theo Thành Phần Máu - Tiểu Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('B-', 'Theo Thành Phần Máu', 'Tiểu Cầu', 'A-, B-, AB-, O- (Ưu Tiên B-)');

-- AB+ Toàn Phần
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('AB+', 'Toàn Phần', NULL, 'A+, A-, B+, B-, AB+, AB-, O+, O-');

-- AB+ Theo Thành Phần Máu - Hồng Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('AB+', 'Theo Thành Phần Máu', 'Hồng Cầu', 'A+, A-, B+, B-, AB+, AB-, O+, O-');

-- AB+ Theo Thành Phần Máu - Tiểu Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('AB+', 'Theo Thành Phần Máu', 'Tiểu Cầu', 'A+, A-, B+, B-, AB+, AB-, O+, O- (Ưu Tiên AB+)');

-- AB- Toàn Phần
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('AB-', 'Toàn Phần', NULL, 'A-, B-, AB-, O-');

-- AB- Theo Thành Phần Máu - Hồng Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('AB-', 'Theo Thành Phần Máu', 'Hồng Cầu', 'A-, B-, AB-, O-');

-- AB- Theo Thành Phần Máu - Tiểu Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('AB-', 'Theo Thành Phần Máu', 'Tiểu Cầu', 'A-, B-, AB-, O- (Ưu Tiên AB-)');

-- O+ Toàn Phần
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('O+', 'Toàn Phần', NULL, 'O+, O-');

-- O+ Theo Thành Phần Máu - Hồng Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('O+', 'Theo Thành Phần Máu', 'Hồng Cầu', 'O+, O-');

-- O+ Theo Thành Phần Máu - Tiểu Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('O+', 'Theo Thành Phần Máu', 'Tiểu Cầu', 'A+, A-, B+, B-, AB+, AB-, O+, O- (Ưu Tiên O+)');

-- O- Toàn Phần
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('O-', 'Toàn Phần', NULL, 'O-');

-- O- Theo Thành Phần Máu - Hồng Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('O-', 'Theo Thành Phần Máu', 'Hồng Cầu', 'O-');

-- O- Theo Thành Phần Máu - Tiểu Cầu
INSERT INTO blood_compatibility (receiver_blood_type, transfusion_type, blood_component, compatible_blood_types) 
VALUES ('O-', 'Theo Thành Phần Máu', 'Tiểu Cầu', 'A-, B-, AB-, O- (Ưu Tiên O-)');


