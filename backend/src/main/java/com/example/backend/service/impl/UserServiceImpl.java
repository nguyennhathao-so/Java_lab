package com.example.backend.service.impl;

import com.example.backend.dto.RegisterRequest;
import com.example.backend.entity.User;
import com.example.backend.entity.Role;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.service.IUserService;
import com.example.backend.service.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final GeocodingService geocodingService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository, GeocodingService geocodingService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.geocodingService = geocodingService;
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
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setGender(request.getGender());
        user.setBloodType(request.getBloodType());
        
        // Chuyển đổi địa chỉ thành tọa độ sử dụng Nominatim API
        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            try {
                String coordinates = geocodingService.geocodeAddressWithTimeout(request.getAddress());
                if (coordinates != null) {
                    user.setLocation(coordinates);
                    System.out.println("Đã chuyển đổi địa chỉ '" + request.getAddress() + "' thành tọa độ: " + coordinates);
                } else {
                    System.out.println("Không thể chuyển đổi địa chỉ: " + request.getAddress());
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi chuyển đổi địa chỉ: " + e.getMessage());
                // Không throw exception để không làm gián đoạn quá trình đăng ký
            }
        }
        
        // Lấy role 'USER' từ database
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.setRole(userRole);

        userRepository.save(user);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isPhoneNumberExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }
}