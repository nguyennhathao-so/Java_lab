CREATE DATABASE IF NOT EXISTS blood_donation_db;
USE blood_donation_db;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    address TEXT,
    gender VARCHAR(10) NOT NULL,
    blood_type VARCHAR(10) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample admin user
INSERT INTO users (full_name, email, password, phone_number, address, gender, blood_type, role)
VALUES ('Admin', 'admin@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', '0123456789', 'Admin Address', 'Nam', 'O+', 'ADMIN');

-- Insert sample regular user
INSERT INTO users (full_name, email, password, phone_number, address, gender, blood_type, role)
VALUES ('User Test', 'user@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', '0987654321', 'User Address', 'Nữ', 'A+', 'USER'); 

INSERT INTO users (full_name, email, password, phone_number, address, gender, blood_type, role)
VALUES ('Staff Test', 'staff@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', '0987654320', 'User Address', 'Nữ', 'A+', 'USER'); 

