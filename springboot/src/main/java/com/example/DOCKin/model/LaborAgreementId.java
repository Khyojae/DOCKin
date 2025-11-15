package com.example.DOCKin.model;

import com.example.DOCKin.model.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

// LaborAgreement 엔티티의 복합 키 정의
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LaborAgreementId implements Serializable {

    private User user; // LaborAgreement의 필드명(user)과 일치해야 함
    private LocalDate agreementMonth; // LaborAgreement의 필드명(agreementMonth)과 일치해야 함
}