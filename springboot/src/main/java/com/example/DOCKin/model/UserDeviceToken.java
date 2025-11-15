package com.example.DOCKin.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_device_tokens", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "fcm_token"})
})
@Getter
@Setter // 토큰 업데이트 시 Setter 필요
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User 엔티티 참조

    @Column(name = "fcm_token", nullable = false)
    private String fcmToken;

    @Column(name = "device_type")
    private String deviceType; // 'android', 'ios'

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // 사용 편의를 위해 생성자 추가
    public UserDeviceToken(User user, String fcmToken, String deviceType) {
        this.user = user;
        this.fcmToken = fcmToken;
        this.deviceType = deviceType;
    }
}