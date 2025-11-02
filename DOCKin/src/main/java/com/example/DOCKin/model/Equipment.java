package com.example.DOCKin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "equipment") //
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipment_id; // PK

    private String name;

    // 나머지 필드는 필요에 따라 추가
    private String qr_code;
    private String nfc_tag;

    // ...
}