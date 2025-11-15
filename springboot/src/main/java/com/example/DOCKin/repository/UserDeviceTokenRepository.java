package com.example.DOCKin.repository;

import com.example.DOCKin.model.User;
import com.example.DOCKin.model.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {

    // 특정 사용자의 모든 토큰 조회 (알림 전송 시 필요)
    List<UserDeviceToken> findByUser(User user);

    // 특정 사용자와 토큰 값으로 기존 토큰을 찾아 업데이트할 때 사용
    UserDeviceToken findByUserAndFcmToken(User user, String fcmToken);
    List<String> findFcmTokenByUser(User user);
}