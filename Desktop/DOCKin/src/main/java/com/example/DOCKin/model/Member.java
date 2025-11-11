package com.example.DOCKin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private String userId; // SQL: user_id와 매핑

    private String name;
    private String password;

    @Column(nullable=false)
    private String role;

    private String language_code;
    private Boolean tts_enabled;

    @Column(updatable=false)
    private LocalDateTime created_at; // SQL: created_at과 매핑

    @Column(nullable = false)
    private String shipYardArea; // 예: "제8조선소"
}

