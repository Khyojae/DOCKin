package com.example.DOCKin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
public class AttendanceDto {
    private Long id;

    private String userId;

    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;

    //서버에서 계산하여 클라이언트에 제공
    private Duration totalWorkTime;

    private String status;
    private String inLocation;
    private String outLocation;
}
