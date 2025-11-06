    package com.example.DOCKin.dto;

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
        private Long equipmentId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
