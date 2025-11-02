package com.example.DOCKin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class MemberDto {
    private String userId;
    private String name;
    private String password;
}
