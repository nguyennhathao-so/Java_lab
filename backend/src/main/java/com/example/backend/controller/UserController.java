package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}/blood-type")
    public String getBloodType(@PathVariable String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return null;
        return user.getBloodType();
    }
} 