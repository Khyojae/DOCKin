// src/main/java/com/example/DOCKin/model/ChatMember.java

package com.example.DOCKin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "chat_members")
public class ChatMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_id", nullable = false)
    private Integer roomId;

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId; // 사용자의 사번 (FK)

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @Column(name = "last_read_time", nullable = false)
    private LocalDateTime lastReadTime = LocalDateTime.now();
}