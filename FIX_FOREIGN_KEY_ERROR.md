# Khắc phục lỗi Foreign Key Constraint khi xóa User

## Mô tả lỗi
Lỗi xảy ra khi cố gắng xóa user mà có các bản ghi trong bảng `activity_logs` (hoặc các bảng khác) đang tham chiếu đến user đó.

```
Cannot delete or update a parent row: a foreign key constraint fails 
(`blood_donation_db`.`activity_logs`, CONSTRAINT `activity_logs_ibfk_1` 
FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`))
```

## Nguyên nhân
Các ràng buộc khóa ngoại trong database không có `ON DELETE CASCADE`, nên khi xóa user, các bản ghi phụ thuộc vẫn tồn tại và gây ra lỗi.

## Giải pháp

### Giải pháp 1: Sử dụng code đã được cập nhật (Khuyến nghị)
Code đã được cập nhật để xóa các bản ghi phụ thuộc trước khi xóa user:

1. **Backend đã được cập nhật** với:
   - Thêm các repository cần thiết
   - Thêm phương thức xóa theo userId cho tất cả các bảng phụ thuộc
   - Cập nhật AdminController.deleteUser() để xóa tuần tự các bản ghi phụ thuộc

2. **Các bảng được xử lý**:
   - activity_logs
   - notifications  
   - donation_registrations
   - medical_records
   - blogs
   - appointments
   - donations
   - donation_requests

### Giải pháp 2: Cập nhật database với CASCADE DELETE
Chạy script SQL để cập nhật các ràng buộc khóa ngoại:

```bash
# Kết nối vào MySQL container
docker exec -it <mysql_container_name> mysql -u root -p

# Chạy script fix_foreign_keys.sql
source /docker-entrypoint-initdb.d/fix_foreign_keys.sql
```

Hoặc chạy trực tiếp:
```sql
USE blood_donation_db;

-- Xóa các ràng buộc cũ
ALTER TABLE activity_logs DROP FOREIGN KEY activity_logs_ibfk_1;
ALTER TABLE notifications DROP FOREIGN KEY notifications_ibfk_1;
-- ... (các bảng khác)

-- Thêm lại với CASCADE DELETE
ALTER TABLE activity_logs 
ADD CONSTRAINT activity_logs_ibfk_1 
FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;
-- ... (các bảng khác)
```

### Giải pháp 3: Sử dụng init.sql đã cập nhật
File `backend/docker/init.sql` đã được cập nhật với `ON DELETE CASCADE` cho tất cả các ràng buộc khóa ngoại liên quan đến users.

## Cách test

1. **Khởi động lại ứng dụng**:
```bash
cd backend
./mvnw spring-boot:run
```

2. **Test xóa user**:
```bash
curl -X DELETE http://localhost:8082/api/admin/users/US1 \
  -H "Authorization: Bearer <your_jwt_token>"
```

3. **Kiểm tra kết quả**:
- User được xóa thành công
- Các bản ghi phụ thuộc cũng được xóa tự động

## Lưu ý quan trọng

1. **Backup database** trước khi thực hiện các thay đổi
2. **Test kỹ** trong môi trường development trước khi áp dụng production
3. **Xem xét business logic** - đảm bảo việc xóa cascade là phù hợp với yêu cầu nghiệp vụ
4. **Logging** - có thể thêm logging để theo dõi quá trình xóa

## Troubleshooting

Nếu vẫn gặp lỗi:

1. **Kiểm tra database schema**:
```sql
SHOW CREATE TABLE activity_logs;
```

2. **Kiểm tra các ràng buộc**:
```sql
SELECT 
    TABLE_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    DELETE_RULE
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
WHERE 
    REFERENCED_TABLE_NAME = 'users';
```

3. **Kiểm tra logs** của ứng dụng để xem chi tiết lỗi

4. **Restart database container** nếu cần thiết:
```bash
docker-compose down
docker-compose up -d
``` 