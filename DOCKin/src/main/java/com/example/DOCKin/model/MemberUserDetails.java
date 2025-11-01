package com.example.DOCKin.model;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

//스프링 시큐리티에서 사용자 인증과 권한을 확인하기 위해 UserDetails 객체 작성
@Data
public class MemberUserDetails {
    //User Details 디폴트 구현 getUsername(), getPassword(),getAuthorities()
        private String username;
        private String password;
        private List<SimpleGrantedAuthority> authorities;
        private String displayName;
        private Long member_id;

        public MemberUserDetails(Member member, List<Authority> authorities){
            this.username = String.valueOf(member.getId());  //사원번호를 username으로
            this.displayName=member.getName();
            this.password=member.getPassword();
            this.member_id=member.getId();
            this.authorities=authorities.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                    .toList();
        }

}
