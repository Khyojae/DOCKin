package com.example.DOCKin.repository;
import com.example.DOCKin.model.Work_logs; //
import com.example.DOCKin.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface Work_logsRepository extends JpaRepository<Work_logs,Long>{
    @Transactional
    void deleteAllByMember(Member member);
}
