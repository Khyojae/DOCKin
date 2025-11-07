// src/main/java/com/example/DOCKin/dto/ChatMessageDto.java

package com.example.DOCKin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private Long messageId;
    private Integer roomId;
    private String senderId;
    private String content;
    private String sentAt; // 날짜 시간 형식으로 전송
    private Type type; // 메시지 타입 (ENTER, TALK, QUIT 등)

    public enum Type {
        ENTER, TALK, QUIT
    }
}