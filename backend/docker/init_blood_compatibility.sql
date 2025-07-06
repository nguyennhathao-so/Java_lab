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