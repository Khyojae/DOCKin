package com.example.DOCKin.service;

// 알림 전송 로직이 들어갈 서비스
public interface NotificationService {
    void registerToken(String userId, String fcmToken, String deviceType);
    void sendNotificationToUser(String userId, String title, String body); // 알림 전송 메서드 (나중에 구현)
    // ...
}