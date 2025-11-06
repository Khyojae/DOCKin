package com.example.DOCKin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WorkLogsUpdateRequestDto {
    private String title;
    private String log_text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
