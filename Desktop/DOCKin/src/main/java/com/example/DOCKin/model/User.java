package com.example.DOCKin.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    // users 테이블의 user_id (VARCHAR(50) PRIMARY KEY)에 매핑
    @Id
    private String userId;

    @Column(name = "name")
    private String name;

    @Column(name = "password", nullable = false)
    private String password; // 암호화된 비밀번호 저장

    @Column(name = "role", nullable = false)
    private String role; // 예: ROLE_ADMIN, ROLE_USER

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "tts_enabled")
    private Boolean ttsEnabled;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String shipYardArea;

    // 편의상 @Builder나 생성자는 생략합니다.
    // ...
}