// src/main/java/com/example/DOCKin/repository/ChatMemberRepository.java

package com.example.DOCKin.repository;

import com.example.DOCKin.model.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Integer> {
    List<ChatMember> findByUserId(String userId);
    boolean existsByRoomIdAndUserId(Integer roomId, String userId);
}