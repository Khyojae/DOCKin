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

        if (userDetails == null) {
            // 실제로는 filter에서 401 처리되지만, 혹시 모를 상황 대비하여 명시적으로 처리합니다.
            // 현재 SecurityConfig에서 authenticated()로 막고 있으므로 403이 발생할 수 있습니다.
            // 여기서는 Spring Security가 401 또는 403을 반환하도록 의존하거나
            // 명시적으로 401을 반환할 수 있습니다. (가장 안전한 처리는 401)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


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
