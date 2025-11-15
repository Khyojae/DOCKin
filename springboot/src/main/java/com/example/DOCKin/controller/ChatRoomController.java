// src/main/java/com/example/DOCKin/controller/ChatRoomController.java

package com.example.DOCKin.controller;

import com.example.DOCKin.model.ChatRoom;
import com.example.DOCKin.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController // REST API 컨트롤러
@RequiredArgsConstructor
@RequestMapping("/api/chat/room") // 기본 경로 설정
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /**
     * 1:1 채팅방을 찾거나 새로 생성합니다.
     * 클라이언트로부터 userAId와 userBId를 포함하는 DTO를 받습니다.
     * 💡 ChatRoomService의 메서드를 직접 사용합니다.
     */
    @PostMapping
    public ChatRoom createOrGetOneToOneRoom(@RequestBody ChatRoomRequestDto request) {

        // userAId와 userBId를 서비스로 전달
        return chatRoomService.createOrGetOneToOneRoom(
                request.getUserAId(),
                request.getUserBId()
        );
    }

    // 💡 테스트를 위한 간단한 Request DTO (별도 파일 생성 필요)
    private static class ChatRoomRequestDto {
        private String userAId;
        private String userBId;

        // Getter 및 Setter, NoArgsConstructor 등 필요 (Lombok 사용 권장)
        public String getUserAId() { return userAId; }
        public String getUserBId() { return userBId; }
        // ...
    }
}