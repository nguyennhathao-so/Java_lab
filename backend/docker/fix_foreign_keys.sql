-- Script để cập nhật các ràng buộc khóa ngoại với CASCADE DELETE
-- Chạy script này để cho phép xóa user và tự động xóa các bản ghi phụ thuộc

USE blood_donation_db;

-- Xóa các ràng buộc khóa ngoại cũ
ALTER TABLE activity_logs DROP FOREIGN KEY activity_logs_ibfk_1;
ALTER TABLE notifications DROP FOREIGN KEY notifications_ibfk_1;
ALTER TABLE donation_registrations DROP FOREIGN KEY donation_registrations_ibfk_1;
ALTER TABLE medical_records DROP FOREIGN KEY medical_records_ibfk_1;
ALTER TABLE blogs DROP FOREIGN KEY blogs_ibfk_1;
ALTER TABLE appointments DROP FOREIGN KEY appointments_ibfk_1;
ALTER TABLE donations DROP FOREIGN KEY donations_ibfk_1;
ALTER TABLE donation_requests DROP FOREIGN KEY donation_requests_ibfk_2;

-- Thêm lại các ràng buộc khóa ngoại với CASCADE DELETE
ALTER TABLE activity_logs 
ADD CONSTRAINT activity_logs_ibfk_1 
FOREIGN KEY (user_id) REFERENCES users (user_id) 
ON DELETE CASCADE;

ALTER TABLE notifications 
ADD CONSTRAINT notifications_ibfk_1 
FOREIGN KEY (user_id) REFERENCES users (user_id) 
ON DELETE CASCADE;

ALTER TABLE donation_registrations 
ADD CONSTRAINT donation_registrations_ibfk_1 
FOREIGN KEY (user_id) REFERENCES users (user_id) 
ON DELETE CASCADE;

ALTER TABLE medical_records 
ADD CONSTRAINT medical_records_ibfk_1 
FOREIGN KEY (user_id) REFERENCES users (user_id) 
ON DELETE CASCADE;

ALTER TABLE blogs 
ADD CONSTRAINT blogs_ibfk_1 
FOREIGN KEY (author_id) REFERENCES users (user_id) 
ON DELETE CASCADE;

ALTER TABLE appointments 
ADD CONSTRAINT appointments_ibfk_1 
FOREIGN KEY (user_id) REFERENCES users (user_id) 
ON DELETE CASCADE;

ALTER TABLE donations 
ADD CONSTRAINT donations_ibfk_1 
FOREIGN KEY (user_id) REFERENCES users (user_id) 
ON DELETE CASCADE;

ALTER TABLE donation_requests 
ADD CONSTRAINT donation_requests_ibfk_2 
FOREIGN KEY (user_id) REFERENCES users (user_id) 
ON DELETE CASCADE;

-- Kiểm tra các ràng buộc đã được tạo
SELECT 
    TABLE_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME,
    DELETE_RULE
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
WHERE 
    REFERENCED_TABLE_NAME = 'users' 
    AND TABLE_SCHEMA = 'blood_donation_db'; 