package com.example.DOCKin.repository;

import com.example.DOCKin.dto.UnsignedUserResponse;
import com.example.DOCKin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    // 전체 사용자를 DTO로 조회
    @Query("SELECT new com.example.DOCKin.dto.UnsignedUserResponse(u.userId, u.name) FROM User u")
    List<UnsignedUserResponse> findAllUserIdsAndNames();

    // 특정 ID 목록에 포함되지 않은(서명하지 않은) 사용자의 ID와 이름을 조회
    @Query("SELECT new com.example.DOCKin.dto.UnsignedUserResponse(u.userId, u.name) FROM User u WHERE u.userId NOT IN :signedUserIds")
    List<UnsignedUserResponse> findUnsignedUsersByIds(@Param("signedUserIds") List<String> signedUserIds);
}