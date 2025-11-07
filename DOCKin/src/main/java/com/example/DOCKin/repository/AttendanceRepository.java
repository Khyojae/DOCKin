package com.example.DOCKin.repository;

import com.example.DOCKin.model.Attendance;
import com.example.DOCKin.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    //서비스에서 로그인한 멤버 객체 받기
    List<Attendance> findByMemberOrderByWorkDateDesc(Member member);

    Optional<Attendance> findByMemberAndWorkDate(Member member, LocalDate workDate);

    List<Attendance> findByMemberAndWorkDateBetween(Member member, LocalDate startDate, LocalDate endDate);
}
