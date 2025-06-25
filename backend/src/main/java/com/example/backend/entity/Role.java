package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.backend.config.EntityIdListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@EntityListeners(EntityIdListener.class)
public class Role {
    @Id
    @Column(name = "role_id")
    private String roleId;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;
}
