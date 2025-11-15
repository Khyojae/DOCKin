package com.example.DOCKin.service;

import com.example.DOCKin.dto.WorkLogsCreateRequestDto;
import com.example.DOCKin.dto.WorkLogsUpdateRequestDto;
import com.example.DOCKin.dto.Work_logsDto;
import com.example.DOCKin.model.Equipment;
import com.example.DOCKin.model.Member;
import com.example.DOCKin.model.Work_logs;
import com.example.DOCKin.repository.EquipmentRepository;
import com.example.DOCKin.repository.MemberRepository;
import com.example.DOCKin.repository.Work_logsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션 설정
public class Work_logsService {
    private final MemberRepository memberRepository;
    private final Work_logsRepository work_logsRepository;
    private final SttApiService sttApiService;
    private final EquipmentRepository equipmentRepository;
    /**
     * DTO 변환 메소드: Entity -> DTO
     */
    private Work_logsDto mapToWork_logDto(Work_logs work_logs){
        return Work_logsDto.builder()
                .log_id(work_logs.getLog_id())
                .user_id(work_logs.getMember().getUserId())
                .title(work_logs.getTitle())
                .log_text(work_logs.getLog_text())
                .created_at(work_logs.getCreated_at())
                .updated_at(work_logs.getUpdated_at())
                .equipment_id(work_logs.getEquipment() != null ? work_logs.getEquipment().getEquipment_id() : null)
                .build();
    }

    // --- 1. CREATE: 새 작업 일지 생성 ---
    @Transactional
    public Work_logsDto createWorkLog(String userId, WorkLogsCreateRequestDto createDto){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found : "+userId));

        Equipment equipment = null;
        if (createDto.getEquipmentId() != null) { // WorkLogsCreateRequestDto에 getEquipmentId()가 있다고 가정
            equipment = equipmentRepository.findById(createDto.getEquipmentId())
                    .orElse(null);
        }
        Work_logs newLog = Work_logs.builder()
                .member(member)
                .title(createDto.getTitle()) // 오타 수정 완료
                .log_text(createDto.getLog_text())
                .equipment(equipment) // Equipment는 현재 null로 가정
                .build();

        Work_logs savedLog = work_logsRepository.save(newLog);
        return mapToWork_logDto(savedLog);
    }

    // --- 2. READ ALL: 개인 작업 일지 전체 조회 ---
    public List<Work_logsDto> getMyWorkLogs(String userId){
        Member loggedInMember = memberRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("User not found: "+userId));

        // 1. 현재 사용자의 구역 정보 추출
        String targetArea = loggedInMember.getShipYardArea(); // Member 엔티티의 Getter 사용

        // 2. 해당 구역에 속한 모든 Member 리스트 조회
        List<Member> areaMembers = memberRepository.findByShipYardArea(targetArea);

        // 3. 해당 구역의 모든 Member가 작성한 Work_logs를 조회
        List<Work_logs> logs = work_logsRepository.findByMemberIn(areaMembers);

        return logs.stream()
                .map(this::mapToWork_logDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Work_logsDto processSttAndSave(String userId, MultipartFile file, WorkLogsCreateRequestDto metadata){
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(()->new RuntimeException("사용자를 찾을 수 없습니다."));

        Equipment equipment = null;
        if(metadata.getEquipmentId() != null) {
            equipment = equipmentRepository.findById(metadata.getEquipmentId())
                    .orElse(null);
        }


        String transcribedText = sttApiService.callSttApi(file);

        Work_logs newLog = Work_logs.builder()
                .member(member)
                .title(metadata.getTitle()!= null ? metadata.getTitle() : "작업일지")
                .log_text(transcribedText)
                .equipment(equipment)
                .build();

        newLog = work_logsRepository.save(newLog);
        return Work_logsDto.toDto(newLog);
    }

    // --- 3. READ SINGLE: 특정 작업 일지 상세 조회 ---
    public Work_logsDto getWorkLog(String userId, Long logId){
        Work_logs log = work_logsRepository.findById(logId)
                .orElseThrow(()-> new IllegalArgumentException("Work log not found with ID: " +logId));

        // 1. 현재 로그인한 사용자 정보 조회
        Member loggedInMember = memberRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("User not found: "+userId));

        // 2. 구역 정보 추출 및 비교
        String loggedInArea = loggedInMember.getShipYardArea();
        String logAuthorArea = log.getMember().getShipYardArea();

        // 3. 보안 검증: 현재 사용자와 로그 작성자가 같은 구역인지 확인
        if (!loggedInArea.equals(logAuthorArea)){
            throw new IllegalArgumentException("You are not authorized to view this log. (Different Area)");
        }

        return mapToWork_logDto(log);
    }

    // --- 4. UPDATE: 작업 일지 수정 ---
    @Transactional
    public Work_logsDto updateWorkLog(String userId, Long logId, WorkLogsUpdateRequestDto updateDto){
        Work_logs log = work_logsRepository.findById(logId)
                .orElseThrow(()-> new IllegalArgumentException("Work log not found with ID: "+logId));

        // 보안 검증: 현재 사용자가 작성자인지 확인
        if(!log.getMember().getUserId().equals(userId)){
            throw new IllegalArgumentException("You are not authorized to update this log.");
        }

        // 데이터 업데이트 (updated_at은 @LastModifiedDate에 의해 자동으로 업데이트됨)
        log.setTitle(updateDto.getTitle());
        log.setLog_text(updateDto.getLog_text());
        // save()를 호출하지 않아도 @Transactional에 의해 변경사항이 DB에 반영됨 (Dirty Checking)

        return mapToWork_logDto(log);
    }

    // --- 5. DELETE: 작업 일지 삭제 ---
    @Transactional
    public void deleteWorkLog(String userId, Long logId){
        Work_logs log = work_logsRepository.findById(logId)
                .orElseThrow(()-> new IllegalArgumentException("Work log not found with ID: "+ logId));

        // 보안 검증: 현재 사용자가 작성자인지 확인
        if(!log.getMember().getUserId().equals(userId)){
            throw new IllegalArgumentException("You are not authorized to delete this log.");
        }

        work_logsRepository.delete(log);
    }
}