package com.example.bul.repository;

import com.example.bul.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bul.model.Member; // ✅ 올바른 Member 클래스 임포트
import java.util.List;


public interface AuthorityRepository extends JpaRepository<Authority,Long> {
  List<Authority> findByMember(Member member);
}
