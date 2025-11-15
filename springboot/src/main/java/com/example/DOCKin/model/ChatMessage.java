// src/main/java/com/example/DOCKin/model/ChatMessage.java

package com.example.DOCKin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(name = "room_id", nullable = false)
    private Integer roomId;

    @Column(name = "sender_id", nullable = false)
    private String senderId;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;
}