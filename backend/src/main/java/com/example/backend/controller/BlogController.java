package com.example.backend.controller;

import com.example.backend.entity.Blog;
import com.example.backend.repository.BlogRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;

    // API lấy danh sách bài viết
    @GetMapping
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    // API đăng bài viết mới
    @PostMapping
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
        // Lấy user từ token đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        blog.setAuthor(user);
        Blog savedBlog = blogRepository.save(blog);
        return ResponseEntity.ok(savedBlog);
    }
} 