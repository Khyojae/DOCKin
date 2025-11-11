package com.example.DOCKin.service;

import com.example.DOCKin.dto.UnsignedUserResponse;
import com.example.DOCKin.model.LaborAgreement;
import com.example.DOCKin.model.LaborAgreementId;
import com.example.DOCKin.model.User;
import com.example.DOCKin.repository.LaborAgreementRepository;
import com.example.DOCKin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SafetyAgreementService {

    private final LaborAgreementRepository laborAgreementRepository;
    private final UserRepository userRepository;

    /**
     * 관리자: 특정 월의 미서명 근로자 목록을 조회합니다.
     */
    public List<UnsignedUserResponse> getUnsignedWorkers(LocalDate targetMonth) {
        // ... (기존 코드 유지)
        List<String> signedUserIds = laborAgreementRepository.findSignedUserIdsByMonth(targetMonth);

        if (signedUserIds.isEmpty()) {
            return userRepository.findAllUserIdsAndNames();
        } else {
            return userRepository.findUnsignedUsersByIds(signedUserIds);
        }
    }

    /**
     * 근로자: 현재 월의 근로 동의서에 서명 처리합니다. (POST /agreement/sign)
     */
    @Transactional // ⭐ 쓰기 작업이므로 @Transactional 재정의
    public void signAgreement(String userId) {

        // 현재 월의 첫째 날 (YYYY-MM-01)을 서명 대상으로 설정
        LocalDate currentMonthStart = YearMonth.now().atDay(1);

        // 1. User 엔티티 조회 (관계 매핑 및 복합 키 생성에 필요)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // 2. 해당 월의 기존 서명 기록 조회 (복합 키 사용)
        LaborAgreementId id = new LaborAgreementId(user, currentMonthStart);
        Optional<LaborAgreement> existingAgreement = laborAgreementRepository.findById(id);

        if (existingAgreement.isPresent()) {
            LaborAgreement agreement = existingAgreement.get();
            if (!agreement.getIsSigned()) {
                // 미서명 상태라면 업데이트 (LaborAgreement 엔티티에 Setter 필요)
                agreement.setIsSigned(true);
                agreement.setSignedAt(LocalDateTime.now());
                laborAgreementRepository.save(agreement);
            }
        } else {
            // 3. 기록이 없다면 새로 생성하고 바로 서명 처리 (첫 서명)
            // LaborAgreement 엔티티에 @Builder 및 @Setter가 필요합니다.
            LaborAgreement newAgreement = LaborAgreement.builder()
                    .user(user)
                    .agreementMonth(currentMonthStart)
                    .isSigned(true)
                    .signedAt(LocalDateTime.now())
                    .build();

            laborAgreementRepository.save(newAgreement);
        }
    }
}