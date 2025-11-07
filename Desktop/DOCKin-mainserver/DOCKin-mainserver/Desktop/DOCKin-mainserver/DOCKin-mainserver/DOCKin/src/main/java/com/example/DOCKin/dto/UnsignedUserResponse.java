package com.example.DOCKin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
// 서명하지 않은 사용자 정보를 담는 DTO
public class UnsignedUserResponse {
    private String userId; // 사번
    private String name;   // 이름
}