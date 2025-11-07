package com.example.DOCKin.service;

import org.springframework.web.multipart.MultipartFile;

// 💡 가상의 STT API 호출 서비스
public interface SttApiService {
    String callSttApi(MultipartFile audioFile);
}