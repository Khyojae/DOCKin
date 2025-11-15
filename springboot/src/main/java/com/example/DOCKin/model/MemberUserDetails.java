package com.example.DOCKin.model;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//스프링 시큐리티에서 사용자 인증과 권한을 확인하기 위해 UserDetails 객체 작성
@Data
public class MemberUserDetails implements UserDetails {

    private String username;
    private String password;
    private List<SimpleGrantedAuthority> authorities;
    private String displayName;
    private String member_id;

    public MemberUserDetails(Member member, List<SimpleGrantedAuthority> authorities){
        this.username = (member.getUserId());  //사원번호를 username으로
        this.displayName=member.getName();
        this.password=member.getPassword();
        this.member_id=member.getUserId();
        this.authorities = authorities;
    }

    // 💡 필수 구현: getAuthorities()는 @Data가 생성하지만, 명시적으로 Collection 타입을 반환하는 것이 더 명확합니다.
    @Override
    public Collection<? extends SimpleGrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    // 💡 필수 구현: 만료되지 않았음을 의미합니다.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 💡 필수 구현: 잠기지 않았음을 의미합니다.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 💡 필수 구현: 자격 증명(비밀번호)이 만료되지 않았음을 의미합니다.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 💡 필수 구현: 활성화되었음을 의미합니다.
    @Override
    public boolean isEnabled() {
        return true;
    }

    // getUsername()과 getPassword()는 @Data가 생성합니다.
}