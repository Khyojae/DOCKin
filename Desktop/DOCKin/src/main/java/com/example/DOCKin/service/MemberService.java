package com.example.DOCKin.service;

import com.example.DOCKin.dto.MemberDto;
import com.example.DOCKin.model.Member;
import com.example.DOCKin.model.MemberUserDetails;
import com.example.DOCKin.repository.MemberRepository;
import com.example.DOCKin.repository.Work_logsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final Work_logsRepository work_logsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
        String roleString = member.getRole();

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(roleString)
        );
        return new MemberUserDetails(member, authorities);
    }


    public MemberDto findById(String userId){

        return memberRepository.findByUserId(userId)
                .map(this::mapToMemberDto)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
    }

    private MemberDto mapToMemberDto(Member member){
        return MemberDto.builder()
                .userId(member.getUserId())
                .name(member.getName())
                .build();
    }



}
