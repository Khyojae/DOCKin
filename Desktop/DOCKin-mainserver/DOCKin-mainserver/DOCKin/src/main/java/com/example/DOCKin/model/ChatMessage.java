// src/main/java/com/example/DOCKin/model/ChatMessage.java

package com.example.DOCKin.model;

import lombok.Getter;
import lombok.Setter;
// 🛑 여기를 jakarta.persistence.* 로 변경 (가장 중요)
import jakarta.persistence.*;
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