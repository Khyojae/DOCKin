package com.example.DOCKin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users") // SQL 테이블 users와 매핑
public class Member {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; // SQL: user_id와 매핑

    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role; // 아래에 정의된 enum Role 사용

    private String language_code;
    private Boolean tts_enabled;

    @Column(updatable=false)
    private LocalDateTime created_at; // SQL: created_at과 매핑
}

enum Role {
    worker,
    admin
}