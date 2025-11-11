package com.example.DOCKin.controller;

import com.example.DOCKin.dto.UnsignedUserResponse;
import com.example.DOCKin.service.SafetyAgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/safety/admin/agreement")
@RequiredArgsConstructor
public class SafetyAgreementAdminController {

    private final SafetyAgreementService safetyAgreementService;

    /**
     * 관리자: 특정 월의 미서명 근로자 목록 조회
     * GET /api/safety/admin/agreement/unsigned?month=YYYY-MM
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')") // ⭐ 관리자 권한 필요
    @GetMapping("/unsigned")
    public ResponseEntity<List<UnsignedUserResponse>> getUnsignedWorkers(
            @RequestParam("month") String month
    ) {
        LocalDate targetMonth;
        try {
            // YYYY-MM 문자열을 해당 월의 1일로 변환
            targetMonth = YearMonth.parse(month).atDay(1);
        } catch (DateTimeParseException e) {
            // 날짜 형식 오류 시 400 Bad Request 반환
            return ResponseEntity.badRequest().build();
        }

        List<UnsignedUserResponse> unsignedWorkers = safetyAgreementService.getUnsignedWorkers(targetMonth);
        return ResponseEntity.ok(unsignedWorkers);
    }
}