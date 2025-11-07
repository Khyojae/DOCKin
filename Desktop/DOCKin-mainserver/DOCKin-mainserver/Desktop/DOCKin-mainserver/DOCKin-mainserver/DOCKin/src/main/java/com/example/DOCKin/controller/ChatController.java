// src/main/java/com/example/DOCKin/controller/ChatController.java

package com.example.DOCKin.controller;

import com.example.DOCKin.dto.ChatMessageDto;
import com.example.DOCKin.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template; // 특정 Broker로 메시지를 전송
    private final ChatService chatService;

    /**
     * 클라이언트가 /app/chat/message 로 메시지를 발행하면 이 메서드가 처리
     * 처리 후, /topic/chatroom/{roomId} 경로를 구독하는 모든 클라이언트에게 메시지 전송
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {
        // 1. 메시지 DB 저장 (비동기 처리 가능)
        chatService.saveMessage(message);

        // 2. 메시지 타입에 따라 처리 로직 분기
        if (ChatMessageDto.Type.ENTER.equals(message.getType())) {
            message.setContent(message.getSenderId() + " 님이 입장하셨습니다.");
        }

        // 3. 해당 채팅방을 구독하는 클라이언트들에게 메시지 브로드캐스트
        template.convertAndSend("/topic/chatroom/" + message.getRoomId(), message);
    }
}