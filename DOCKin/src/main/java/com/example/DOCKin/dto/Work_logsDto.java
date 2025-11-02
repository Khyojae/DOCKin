package com.example.DOCKin.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class Work_logsDto {
    private Long log_id;
    private Long user_id;
    private Long equipment_id;
    private String title;
    private String log_text;
    private Date created_at;
    private Date updated_at;

}
