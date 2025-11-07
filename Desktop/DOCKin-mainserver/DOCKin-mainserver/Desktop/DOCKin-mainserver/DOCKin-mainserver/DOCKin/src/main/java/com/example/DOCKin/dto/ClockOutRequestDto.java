package com.example.DOCKin.dto;

import lombok.Builder;
import lombok.Data;

//attendance
//퇴근요청dto
@Data
@Builder
public class ClockOutRequestDto {
    private String outLocation;
}
