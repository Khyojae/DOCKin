// src/main/java/com/example/DOCKin/service/ChatService.java

package com.example.DOCKin.service;

import com.example.DOCKin.dto.ChatMessageDto;
import com.example.DOCKin.model.ChatMessage;
import com.example.DOCKin.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void saveMessage(ChatMessageDto dto) {
        ChatMessage message = new ChatMessage();
        message.setRoomId(dto.getRoomId());
        message.setSenderId(dto.getSenderId());
        message.setContent(dto.getContent());
        message.setSentAt(LocalDateTime.now());

        chatMessageRepository.save(message);
    }

    // TODO: 채팅방 생성, 채팅방 목록 조회, 과거 메시지 조회 로직 추가 필요
}