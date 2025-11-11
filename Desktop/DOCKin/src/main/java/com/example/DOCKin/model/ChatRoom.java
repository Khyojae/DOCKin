// src/main/java/com/example/DOCKin/model/ChatRoom.java

package com.example.DOCKin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    @Column(name = "room_name", length = 100)
    private String roomName;

    @Column(name = "is_group", nullable = false)
    private Boolean isGroup; // 1:1 채팅방(false) 또는 단체방(true)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


}