package com.example.DOCKin.service;

//mapToWork_logsDto 메서드를 통해서 게시글 모델엔티티 객체를
//DTO객체로 변환

import com.example.DOCKin.dto.Work_logsDto;
import com.example.DOCKin.model.Work_logs;
import com.example.DOCKin.repository.MemberRepository;
import com.example.DOCKin.repository.Work_logsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class Work_logsService {
    private final MemberRepository memberRepository;
    private final Work_logsRepository work_logsRepository;

    private Work_logsDto mapToWork_logDto(Work_logs work_logs){
        return Work_logsDto.builder()
                .log_id(work_logs.getLog_id())
                .user_id(work_logs.getMember().getUserId())
                .title(work_logs.getTitle())
                .log_text(work_logs.getLog_text())
                .created_at(work_logs.getCreated_at())
                .updated_at(work_logs.getUpdated_at())
                .equipment_id(work_logs.getEquipment().getEquipment_id())
                .build();


    }
}
