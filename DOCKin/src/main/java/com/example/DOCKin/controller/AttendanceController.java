package com.example.DOCKin.controller;

import com.example.DOCKin.dto.AttendanceDto;
import com.example.DOCKin.dto.ClockInRequestDto;
import com.example.DOCKin.dto.ClockOutRequestDto;
import com.example.DOCKin.model.MemberUserDetails;
import com.example.DOCKin.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    //출근 (clock-in) api
    @PostMapping("/in")
    public ResponseEntity<AttendanceDto> clockIn(
            @AuthenticationPrincipal MemberUserDetails userDetails,
            @RequestBody ClockInRequestDto requestDto){
        String userId = userDetails.getUsername();
        AttendanceDto newAttendance = attendanceService.clockIn(userId,requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newAttendance);
    }

    //퇴근 (clock-out) api
    @PostMapping("/out")
    public ResponseEntity<AttendanceDto> clockOut(
            @AuthenticationPrincipal MemberUserDetails userDetails,
            @RequestBody ClockOutRequestDto requestDto){
        String userId = userDetails.getUsername();
        AttendanceDto updatedAttendance = attendanceService.clockOut(userId, requestDto);

        return ResponseEntity.ok(updatedAttendance);
    }

    //개인 근태 기록 조회 API
    @GetMapping
    public ResponseEntity<List<AttendanceDto>> getMyAttendanceRecords(
            @AuthenticationPrincipal MemberUserDetails userDetails){
        String userId =userDetails.getUsername();
        List<AttendanceDto> records = attendanceService.getMyAttendanceRecords(userId);

        return ResponseEntity.ok(records);
    }
}
