package com.example.DOCKin.controller;

import com.example.DOCKin.dto.FcmTokenRequest;
import com.example.DOCKin.model.MemberUserDetails;
import com.example.DOCKin.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 모바일 클라이언트의 FCM 토큰을 등록하거나 업데이트합니다.
     * POST /api/notifications/token
     */
    @PostMapping("/token")
    public ResponseEntity<Void> registerToken(
            @AuthenticationPrincipal MemberUserDetails userDetails,
            @RequestBody FcmTokenRequest request) {

        if (userDetails == null) {
            return ResponseEntity.status(401).build(); // 인증되지 않은 사용자
        }

        String userId = userDetails.getUsername();

        // 서비스에 토큰 등록 요청
        notificationService.registerToken(userId, request.getFcmToken(), request.getDeviceType());

        return ResponseEntity.ok().build();
    }
}