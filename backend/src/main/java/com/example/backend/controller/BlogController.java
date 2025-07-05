package com.example.backend.controller;

import com.example.backend.entity.Blog;
import com.example.backend.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    @Autowired
    private BlogRepository blogRepository;

    // API lấy danh sách bài viết
    @GetMapping
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    // API đăng bài viết mới
    @PostMapping
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
        Blog savedBlog = blogRepository.save(blog);
        return ResponseEntity.ok(savedBlog);
    }
} 