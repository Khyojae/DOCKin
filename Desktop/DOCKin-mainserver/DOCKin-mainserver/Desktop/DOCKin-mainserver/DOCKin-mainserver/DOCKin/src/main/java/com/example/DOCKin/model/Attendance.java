package com.example.DOCKin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="attendance")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="user_id",nullable=false)
    private Member member;

    @Column(name="clock_in_time",nullable=false)
    private LocalDateTime clockInTime;

    @Column(name="clock_out_time")
    private LocalDateTime clockOutTime;

    @Column(name="work_date",nullable = false)
    private LocalDate workDate;

    @Column(name="status",length=20,nullable=false)
    private String status;

    @Column(name="in_location")
    private String inLocation;

    @Column(name="out_location")
    private String outLocation;
}
