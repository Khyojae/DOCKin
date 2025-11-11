package com.example.DOCKin.dto;

import com.example.DOCKin.model.Work_logs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Work_logsDto {
    // DTO 필드는 엔티티 필드와 1:1 대응되도록 유지합니다.
    private Long log_id;
    private String user_id;
    private Long equipment_id; // 장비 ID를 직접 표시하기 위해 추가
    private String title;
    private String log_text;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;


    public static Work_logsDto toDto(Work_logs entity){
        // Work_logs 엔티티는 Equipment 객체를 포함합니다. ID만 가져옵니다.
        Long equipmentId = (entity.getEquipment() != null) ? entity.getEquipment().getEquipment_id() : null;

        return Work_logsDto.builder()
                .log_id(entity.getLog_id())
                .user_id(entity.getMember().getUserId())
                // 💡 Work_logs 엔티티의 getEquipment().getEquipment_id()를 사용합니다.
                .equipment_id(equipmentId)
                .title(entity.getTitle())
                // 💡 정확한 Getter: getLog_text()를 사용합니다.
                .log_text(entity.getLog_text())
                .created_at(entity.getCreated_at())
                .updated_at(entity.getUpdated_at())
                .build();
    }
}