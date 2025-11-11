// src/main/java/com/example/DOCKin/config/WebSocketConfig.java

package com.example.DOCKin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // WebSocket 기반 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 메시지를 받을(구독할) 경로 접두사
        registry.enableSimpleBroker("/topic");

        // 클라이언트가 메시지를 보낼(발행할) 경로 접두사
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 연결을 위한 엔드포인트 설정
        // 클라이언트는 ws://localhost:8081/ws/chat 으로 연결
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*") // 모든 출처 허용 (배포 시 보안 강화 필요)
                .withSockJS(); // SockJS 지원 (웹소켓을 지원하지 않는 브라우저를 위해)
    }
}