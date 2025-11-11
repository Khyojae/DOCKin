package com.example.DOCKin.dto;
//출근 요청 DTO
//attendance
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClockInRequestDto {
    private String inLocation;
}
