package com.example.backend.service.impl;

import com.example.backend.dto.RegisterRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterRequest request) {
        // Kiểm tra email đã tồn tại
        if (isEmailExists(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        // Kiểm tra số điện thoại đã tồn tại
        if (isPhoneNumberExists(request.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại đã được sử dụng");
        }

        // Tạo user mới
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setGender(request.getGender());
        user.setBloodType(request.getBloodType());
        user.setRole("USER");

        userRepository.save(user);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isPhoneNumberExists(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
} 