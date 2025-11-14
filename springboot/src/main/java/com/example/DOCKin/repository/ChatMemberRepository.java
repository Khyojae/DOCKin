// src/main/java/com/example/DOCKin/repository/ChatMemberRepository.java

package com.example.DOCKin.repository;

import com.example.DOCKin.model.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Integer> {
    List<ChatMember> findByUserId(String userId);
    boolean existsByRoomIdAndUserId(Integer roomId, String userId);
    @Query(value = """
        SELECT cm.room_id FROM chat_members cm
        INNER JOIN chat_rooms cr ON cm.room_id = cr.room_id  // 💡 스키마에 맞춰 'chat_rooms' 사용
        WHERE cr.is_group = false
          AND cm.user_id IN (:userAId, :userBId)
        GROUP BY cm.room_id
        HAVING COUNT(DISTINCT cm.user_id) = 2
        """, nativeQuery = true)
    List<Integer> findOneToOneRoomIdsByUserIds(@Param("userAId") String userAId, @Param("userBId") String userBId);
}