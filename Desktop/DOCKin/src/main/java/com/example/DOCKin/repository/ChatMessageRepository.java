// src/main/java/com/example/DOCKin/repository/ChatMessageRepository.java

package com.example.DOCKin.repository;

import com.example.DOCKin.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomIdOrderBySentAtAsc(Integer roomId);
}