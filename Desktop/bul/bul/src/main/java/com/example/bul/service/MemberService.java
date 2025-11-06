package com.example.bul.service;

import com.example.bul.model.Member;
import com.example.bul.repository.ArticleRepository;
import com.example.bul.repository.MemberRepository;
import dto.MemberDto;
import dto.MemberForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean checkPassword(Long id, String password){
        Member member = memberRepository.findById(id).orElseThrow();
        return passwordEncoder.matches(password, member.getPassword());
    }

    public void updatePassword(Long id, String password){
        Member member = memberRepository.findById(id).orElseThrow();
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
    }

    public MemberDto create(MemberForm memberForm){
        Member member = Member.builder()
                .name(memberForm.getName())
                .password(passwordEncoder.encode(memberForm.getPassword()))
                .email(memberForm.getEmail()).build();
        memberRepository.save(member);
        return mapToMemberDto(member);
    }

    // [수정됨] 메서드 참조 대신 명시적 람다 사용
    public Optional<MemberDto> findByEmail(String email){
        return memberRepository.findByEmail(email)
                .map((Member member) -> mapToMemberDto(member));
    }

    // [수정됨] 메서드 참조 대신 명시적 람다 사용
    public MemberDto findById(Long id){
        return memberRepository.findById(id)
                .map((Member member) -> mapToMemberDto(member))
                .orElseThrow();
    }

    private MemberDto mapToMemberDto(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}