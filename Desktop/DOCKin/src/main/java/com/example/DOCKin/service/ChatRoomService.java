// src/main/java/com/example/DOCKin/service/ChatRoomService.java

package com.example.DOCKin.service;

import com.example.DOCKin.model.ChatMember;
import com.example.DOCKin.model.ChatMessage;
import com.example.DOCKin.model.ChatRoom;
import com.example.DOCKin.repository.ChatMemberRepository;
import com.example.DOCKin.repository.ChatMessageRepository;
import com.example.DOCKin.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    // User 엔티티를 찾기 위한 MemberRepository가 필요하다고 가정합니다.

    /**
     * 사용자 ID로 참여 중인 채팅방 목록 조회
     */
    public List<ChatRoom> findRoomsByUserId(String userId) {
        // ChatMember 테이블에서 userId가 참여한 모든 방 ID를 찾고, 해당 ID로 ChatRoom을 조회
        List<ChatMember> chatMembers = chatMemberRepository.findByUserId(userId);

        return chatMembers.stream()
                .map(member -> chatRoomRepository.findById(member.getRoomId()).orElse(null))
                .filter(room -> room != null)
                .collect(Collectors.toList());
    }

    /**
     * 1:1 채팅방을 찾거나 새로 생성합니다.
     */
    @Transactional
    public ChatRoom createOrGetOneToOneRoom(String userAId, String userBId) {
        // 1. 두 사용자가 이미 참여하고 있는 1:1 방이 있는지 검색 (복잡한 쿼리 필요, 현재는 단순화)
        // *임시 검색 로직: 두 사용자가 참여하는 1:1 방을 찾았다고 가정하고
        List<ChatMember> roomsForA = chatMemberRepository.findByUserId(userAId);
        for (ChatMember memberA : roomsForA) {
            // 해당 방이 1:1 방인지 확인하는 로직 추가 필요 (현재는 생략)
            if (chatMemberRepository.existsByRoomIdAndUserId(memberA.getRoomId(), userBId)) {
                // 두 사용자가 모두 참여하는 방을 찾음
                return chatRoomRepository.findById(memberA.getRoomId()).orElse(null);
            }
        }

        // 2. 새로운 1:1 채팅방 생성
        ChatRoom newRoom = new ChatRoom();
        newRoom.setIsGroup(false);
        newRoom.setRoomName(userAId + "_" + userBId);

        // 💡 오류 해결: chatRoomRepository.save 결과를 savedRoom 변수에 할당합니다.
        ChatRoom savedRoom = chatRoomRepository.save(newRoom);

        // 3. 두 사용자를 방에 추가
        ChatMember memberA = new ChatMember();
        memberA.setRoomId(savedRoom.getRoomId());
        memberA.setUserId(userAId);

        ChatMember memberB = new ChatMember();
        memberB.setRoomId(savedRoom.getRoomId());
        memberB.setUserId(userBId);

        chatMemberRepository.saveAll(Arrays.asList(memberA, memberB));

        return savedRoom;
    }

    /**
     * 특정 채팅방의 과거 메시지 이력 조회
     */
    public List<ChatMessage> getMessagesByRoomId(Integer roomId) {
        // 최근 메시지 50개 등 페이징 처리 필요
        return chatMessageRepository.findByRoomIdOrderBySentAtAsc(roomId);
    }
}