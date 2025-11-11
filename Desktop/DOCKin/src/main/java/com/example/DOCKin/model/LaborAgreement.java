package com.example.DOCKin.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "labor_agreements")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// UNIQUE KEY uk_user_month (user_id, agreement_month) 를 반영
@IdClass(LaborAgreementId.class)
public class LaborAgreement {

    // users(user_id) 참조
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // ⭐ User 엔티티가 있어야 합니다.

    @Id
    @Column(name = "agreement_month", nullable = false)
    private LocalDate agreementMonth; // 해당 월의 1일 (YYYY-MM-01)

    @Column(name = "is_signed", nullable = false)
    private Boolean isSigned = false; // 서명 여부

    @Column(name = "signed_at")
    private LocalDateTime signedAt;

    // ... (필요한 생성자 및 메서드 추가)
}