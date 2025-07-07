package com.example.backend.repository;

import com.example.backend.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {
    
    @Modifying
    @Query("DELETE FROM Blog b WHERE b.author.userId = :authorId")
    void deleteByAuthorId(@Param("authorId") String authorId);
} 