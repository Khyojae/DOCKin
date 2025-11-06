package com.example.DOCKin.repository;

import com.example.DOCKin.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//회원 정보를 db에서 생성,수정,삭제,조회
public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByUserId(String userId);

    List<Member> findByShipYardArea(String shipYardArea);
}
