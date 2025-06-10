package com.example.backend.service;

import com.example.backend.dto.RegisterRequest;

public interface IUserService {
    /**
     * Đăng ký người dùng mới
     * @param request thông tin đăng ký
     * @throws RuntimeException nếu email hoặc số điện thoại đã tồn tại
     */
    void register(RegisterRequest request);
    
    /**
     * Kiểm tra email đã tồn tại
     * @param email email cần kiểm tra
     * @return true nếu email đã tồn tại, false nếu chưa
     */
    boolean isEmailExists(String email);
    
    /**
     * Kiểm tra số điện thoại đã tồn tại
     * @param phoneNumber số điện thoại cần kiểm tra
     * @return true nếu số điện thoại đã tồn tại, false nếu chưa
     */
    boolean isPhoneNumberExists(String phoneNumber);
} 