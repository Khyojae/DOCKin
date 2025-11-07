    package com.example.DOCKin.dto;

    import com.fasterxml.jackson.annotation.JsonProperty;
    import lombok.Builder;
    import lombok.Data;
    import lombok.Getter;
    import lombok.Setter;

    import java.time.LocalDateTime;

    @Getter
    @Setter
    @Data
    @Builder
    public class WorkLogsCreateRequestDto {
        private String title;
        private String log_text;

        @JsonProperty("equipment_id")
        private Long equipmentId;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
