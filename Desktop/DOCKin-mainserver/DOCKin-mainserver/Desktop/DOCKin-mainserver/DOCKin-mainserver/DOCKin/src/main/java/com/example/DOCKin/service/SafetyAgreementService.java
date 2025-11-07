package com.example.DOCKin.service;

import com.example.DOCKin.repository.LaborAgreementRepository;
import com.example.DOCKin.repository.UserRepository;
import com.example.DOCKin.dto.UnsignedUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SafetyAgreementService {

    private final LaborAgreementRepository laborAgreementRepository;
    private final UserRepository userRepository;

    /**
     * 특정 월에 근로 동의서를 서명하지 않은 근로자 목록을 조회합니다.
     */
    public List<UnsignedUserResponse> getUnsignedWorkers(LocalDate targetMonth) {
        // 1. 해당 월에 서명 완료한 사용자들의 ID 목록을 조회
        List<String> signedUserIds = laborAgreementRepository.findSignedUserIdsByMonth(targetMonth);

        // 2. 전체 사용자 중에서 서명한 사용자 ID 목록에 포함되지 않은 근로자 목록을 조회
        if (signedUserIds.isEmpty()) {
            // 서명한 사람이 아무도 없다면, 모든 사용자를 반환합니다.
            return userRepository.findAllUserIdsAndNames();
        } else {
            // 서명한 사용자 목록을 제외합니다.
            return userRepository.findUnsignedUsersByIds(signedUserIds);
        }
    }
}