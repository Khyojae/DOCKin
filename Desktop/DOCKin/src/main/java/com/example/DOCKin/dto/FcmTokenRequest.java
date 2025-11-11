package com.example.DOCKin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmTokenRequest {
    private String fcmToken;
    private String deviceType; // "android" 또는 "ios"
}