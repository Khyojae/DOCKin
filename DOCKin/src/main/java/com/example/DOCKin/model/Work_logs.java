package com.example.DOCKin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)    //정할 때 마다 기록됨

public class Work_logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long log_id;

    private String title;

    @Column(name = "log_text", columnDefinition = "TEXT")
    private String log_text;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="equipment_id")
    private Equipment equipment;

}
