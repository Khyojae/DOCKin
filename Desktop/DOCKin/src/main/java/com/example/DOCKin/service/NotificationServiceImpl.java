package com.example.DOCKin.service;

import com.example.DOCKin.model.User;
import com.example.DOCKin.model.UserDeviceToken;
import com.example.DOCKin.repository.UserDeviceTokenRepository;
import com.example.DOCKin.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final UserDeviceTokenRepository tokenRepository;
    private final UserRepository userRepository;

    /**
     * 클라이언트로부터 받은 FCM 토큰을 등록하거나 업데이트합니다.
     */
    @Override
    @Transactional
    public void registerToken(String userId, String fcmToken, String deviceType) {

        // 1. 사용자 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // 2. 해당 사용자의 기존 토큰 목록 조회
        List<UserDeviceToken> existingTokens = tokenRepository.findByUser(user);

        // 3. 현재 FcmToken과 일치하는 기존 토큰이 있는지 확인
        Optional<UserDeviceToken> matchedTokenOpt = existingTokens.stream()
                .filter(token -> token.getFcmToken().equals(fcmToken))
                .findFirst();

        if (matchedTokenOpt.isPresent()) {
            // 토큰이 이미 등록되어 있다면, 업데이트 불필요
            log.info("FCM Token already registered and matched for user: {}", userId);

        } else {
            // 4. 새로운 토큰 또는 갱신된 토큰이라면, 기존 토큰들을 삭제하고 새로운 토큰을 등록합니다.
            // (보통 FCM 토큰은 기기당 하나만 유지하는 것이 일반적입니다. 기존 토큰은 무효화 처리)

            if (!existingTokens.isEmpty()) {
                // 기존 토큰 삭제 (사용자가 기기를 교체했거나, FCM이 토큰을 갱신했을 가능성)
                tokenRepository.deleteAll(existingTokens);
                log.info("Cleared {} stale FCM tokens for user: {}", existingTokens.size(), userId);
            }

            // 5. 새 토큰 저장 (Android 고정)
            UserDeviceToken newToken = new UserDeviceToken(user, fcmToken, "android");
            tokenRepository.save(newToken);
            log.info("New FCM Token registered for user: {}", userId);
        }
    }

    /**
     * 알림 전송 로직 (FCM Admin SDK를 사용하여 다음 단계에서 구현)
     */

    @Override
    public void sendNotificationToUser(String userId, String title, String body) { // ⭐ 단 하나의 정의만 남깁니다.

        // 1. 사용자 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for notification: " + userId));

        // 2. 해당 사용자의 모든 FCM 토큰 조회
        List<UserDeviceToken> deviceTokens = tokenRepository.findByUser(user);

        if (deviceTokens.isEmpty()) {
            log.warn("No FCM tokens found for user: {}", userId);
            return;
        }

        // 3. 알림 메시지 구성
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        // 4. 모든 토큰에 메시지 전송 요청
        for (UserDeviceToken deviceToken : deviceTokens) {
            Message message = Message.builder()
                    .setToken(deviceToken.getFcmToken())
                    .setNotification(notification)
                    .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);
                log.info("FCM Notification sent successfully to user {} (Token: {}), Response: {}", userId, deviceToken.getFcmToken(), response);
            } catch (Exception e) {
                log.error("Failed to send FCM message to user {}: {}", userId, e.getMessage());
                // 전송 실패 시 해당 토큰을 무효화하는 로직(DB에서 삭제)을 추가할 수 있습니다.
            }
        }
    }
}