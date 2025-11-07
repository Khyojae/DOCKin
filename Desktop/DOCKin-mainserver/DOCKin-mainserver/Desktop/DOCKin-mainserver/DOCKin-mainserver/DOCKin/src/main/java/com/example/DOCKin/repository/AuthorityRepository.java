package com.example.DOCKin.repository;

import com.example.DOCKin.model.Authority;
import com.example.DOCKin.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//회원정보를 사용해 해당 회원에게 부여된 권한목록 조회
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    List<Authority> findByMember(Member member);
}
