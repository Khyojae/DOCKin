package com.example.DOCKin.controller;

import com.example.DOCKin.model.MemberUserDetails;
import com.example.DOCKin.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/notifications")
@RequiredArgsConstructor
public class NotificationTestController {

    private final NotificationService notificationService;

    /**
     * 특정 사용자에게 FCM 테스트 알림을 즉시 전송하는 엔드포인트입니다.
     * POST /api/test/notifications/send
     * (관리자 전용)
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')") // 테스트 오남용 방지를 위해 관리자 권한 요구
    @PostMapping("/send")
    public ResponseEntity<String> sendTestNotification(
            @AuthenticationPrincipal MemberUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body("User not authenticated.");
        }

        String targetUserId = userDetails.getUsername(); // 현재 로그인한 사용자에게 전송
        String title = "🚨 최종 테스트 완료 알림";
        String body = "오프라인 동기화, 알림 기능 구현이 완료되었습니다! (Test by " + targetUserId + ")";

        try {
            // NotificationService의 실제 전송 로직 호출
            notificationService.sendNotificationToUser(targetUserId, title, body);
            return ResponseEntity.ok("Notification sent request successful to user: " + targetUserId);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("FCM Send Failed: " + e.getMessage());
        }
    }
}