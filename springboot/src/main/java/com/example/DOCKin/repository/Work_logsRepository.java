package com.example.DOCKin.repository;

import com.example.DOCKin.model.Member;
import com.example.DOCKin.model.Work_logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface Work_logsRepository extends JpaRepository<Work_logs,Long>{
    @Transactional
    void deleteAllByMember(Member member);

    List<Work_logs> findByMember(Member member);
    List<Work_logs> findByMemberIn(Collection<Member> members);
}
