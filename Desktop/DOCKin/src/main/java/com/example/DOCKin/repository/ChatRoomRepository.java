// src/main/java/com/example/DOCKin/repository/ChatRoomRepository.java

package com.example.DOCKin.repository;

import com.example.DOCKin.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    // ChatRoom 엔티티와 PK 타입인 Integer를 사용합니다.
}