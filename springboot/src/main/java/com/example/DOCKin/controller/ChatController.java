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
        // 1. 메시지 타입에 따라 처리 로직 분기 및 DB 저장
        if (ChatMessageDto.Type.ENTER.equals(message.getType())) {
            // ENTER 타입: DB 저장 불필요 (NULL 오류 방지), 브로드캐스트할 내용만 설정
            message.setContent(message.getSenderId() + " 님이 입장하셨습니다.");

        } else if (ChatMessageDto.Type.TALK.equals(message.getType())) {
            // TALK 타입: 실제 대화 메시지만 DB에 저장 (CONTENT가 null이 아님)
            // 💡 FOREIGN KEY 오류를 해결하려면, 이 코드를 실행하기 전에
            //    /api/chat/room 엔드포인트로 방이 먼저 생성되어야 합니다.
            chatService.saveMessage(message);
        }
        // (DTO 객체 변환 문제가 해결되었다면 DTO를 바로 사용합니다.)
        template.convertAndSend("/topic/chatroom/" + message.getRoomId(), message);
    }
}