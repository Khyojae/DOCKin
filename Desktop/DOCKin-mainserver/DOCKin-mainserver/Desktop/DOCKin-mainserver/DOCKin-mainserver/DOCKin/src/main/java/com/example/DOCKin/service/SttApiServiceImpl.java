package com.example.DOCKin.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SttApiServiceImpl implements SttApiService {
    @Override
    public String callSttApi(MultipartFile audioFile){
        String fileName = audioFile.getOriginalFilename();
        System.out.println("Debug stt api 호출 요청 파일명: "+ fileName);
        return "STT 더미 변환 결과: " + fileName + "의 내용입니다. 펌프 점검 완료.";
    }
}
