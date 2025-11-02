package com.example.DOCKin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class Work_logsDto {
    private Long log_id;
    private String user_id;
    private Long equipment_id;
    private String title;
    private String log_text;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
