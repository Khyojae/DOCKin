package com.example.DOCKin.repository;

import com.example.DOCKin.model.LaborAgreement;
import com.example.DOCKin.model.LaborAgreementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LaborAgreementRepository extends JpaRepository<LaborAgreement, LaborAgreementId> {

    // 해당 월에 서명한 모든 사용자의 ID(String) 목록을 조회합니다.
    @Query("SELECT la.user.userId FROM LaborAgreement la WHERE la.agreementMonth = :month AND la.isSigned = TRUE")
    List<String> findSignedUserIdsByMonth(@Param("month") LocalDate month);
}