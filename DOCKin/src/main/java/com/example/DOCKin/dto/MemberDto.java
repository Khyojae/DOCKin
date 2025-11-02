package com.example.DOCKin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class MemberDto {
    private Long id;
    private String name;
    private String password;
}
