package com.example.DOCKin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SafetyEventScheduler {

    private final NotificationService notificationService;
    // ⭐ 참고: 실제 구현 시, 마감 임박 사용자를 조회하기 위한 Repository가 여기에 주입되어야 합니다.

    /**
     * 매일 새벽 1시 정각에 미이수 교육 마감 임박 알림을 전송합니다.
     * Cron 표현식: 0초 0분 1시 *일 *월 ?년
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(readOnly = true)
    public void sendSafetyCourseDeadlineAlerts() {
        log.info("Scheduler started: Sending deadline alerts.");

        // =========================================================
        // ⭐ 이 부분에 실제 마감 임박 사용자를 DB에서 조회하는 로직이 들어갑니다.
        // 예: List<User> targetUsers = enrollmentRepository.findUsersDeadlineIn(3);
        // =========================================================

        // 테스트를 위해 사용자 1001에게 고정 전송 (이전에 토큰 등록한 사용자)
        String targetUserId = "1001";
        String title = "⏰ 안전 교육 마감 임박";
        String body = "필수 안전 교육 이수 마감일이 3일 남았습니다. 지금 바로 확인하세요!";

        try {
            notificationService.sendNotificationToUser(targetUserId, title, body);
        } catch (Exception e) {
            log.error("Failed to send scheduled notification to user {}: {}", targetUserId, e.getMessage());
        }

        log.info("Scheduler finished: Deadline alerts processed.");
    }
}