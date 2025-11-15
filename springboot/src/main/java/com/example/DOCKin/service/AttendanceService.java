package com.example.DOCKin.service;

import com.example.DOCKin.dto.AttendanceDto;
import com.example.DOCKin.dto.ClockInRequestDto;
import com.example.DOCKin.dto.ClockOutRequestDto;
import com.example.DOCKin.model.Attendance;
import com.example.DOCKin.model.Member;
import com.example.DOCKin.repository.AttendanceRepository;
import com.example.DOCKin.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;

    private AttendanceDto mapToDto(Attendance entity){
        Duration totalWorkTime = null;
        if(entity.getClockOutTime()!=null){
            totalWorkTime=Duration.between(entity.getClockInTime(), entity.getClockOutTime());
        }
     return AttendanceDto.builder()
             .id(entity.getId())
             .userId(entity.getMember().getUserId())
             .clockInTime(entity.getClockInTime())
             .clockOutTime(entity.getClockOutTime())
             .totalWorkTime(totalWorkTime)
             .status(entity.getStatus())
             .inLocation(entity.getInLocation())
             .outLocation(entity.getOutLocation())
             .build();
    }

    //출근로직
    @Transactional
    public AttendanceDto clockIn(String userId, ClockInRequestDto requestDto){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("User not found: "+userId));

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        if(attendanceRepository.findByMemberAndWorkDate(member,today).isPresent()){
            throw new IllegalArgumentException("이미 오늘 출근 처리가 완료되었습니다.");
        }

        String status="NORMAL";
        if(now.toLocalTime().isAfter(java.time.LocalTime.of(9,0))){
            status="LATE";
        }

        Attendance attendance = Attendance.builder()
                .member(member)
                .clockInTime(now)
                .workDate(today)
                .status(status)
                .inLocation(requestDto.getInLocation())
                .build();

        return mapToDto(attendanceRepository.save(attendance));
    }


    //퇴근로직
    @Transactional
    public AttendanceDto clockOut(String userId, ClockOutRequestDto requestDto){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("User not found: "+userId));
    LocalDate today = LocalDate.now();

    Attendance attendance = attendanceRepository.findByMemberAndWorkDate(member, today)
            .orElseThrow(()-> new IllegalArgumentException("출근처리를 먼저하세요."));

    if(attendance.getClockOutTime()!=null){
        throw new IllegalArgumentException("이미 오늘 퇴근 처리가 완료되었습니다.");
    }

    attendance.setClockOutTime(LocalDateTime.now());
    attendance.setOutLocation(requestDto.getOutLocation());

    return mapToDto(attendanceRepository.save(attendance));
    }

    //개인 근퇴 기록 조회
    @Transactional(readOnly=true)
    public List<AttendanceDto> getMyAttendanceRecords(String userId){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("User not found:"+ userId));

        List<Attendance> records = attendanceRepository.findByMemberOrderByWorkDateDesc(member);

        return records.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
