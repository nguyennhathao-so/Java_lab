package com.example.backend.service;

import com.example.backend.dto.RegisterRequest;
import com.example.backend.dto.LoginRequest;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.backend.dto.LoginResponse;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GeocodingService geocodingService;

    public void register(RegisterRequest request) {
        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        // Kiểm tra số điện thoại đã tồn tại
        if (userRepository.existsByPhone(request.getPhoneNumber())) {
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

        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.setRole(userRole);

        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng!"));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new LoginResponse("token", user.getRole().getRoleName(), user.getName(), user.getEmail(),
                    user.getUserId(), user.getPhone(), user.getAddress(), user.getGender());
        } else {
            throw new RuntimeException("Email hoặc mật khẩu không đúng!");
        }
    }
}