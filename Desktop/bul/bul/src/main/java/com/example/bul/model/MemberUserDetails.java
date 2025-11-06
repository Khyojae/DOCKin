package com.example.bul.model;


import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
public class MemberUserDetails implements UserDetails {

        private String username;
        private String password;
        private List<SimpleGrantedAuthority> authorities;

        private String displayName;
        private Long memberId;

        public MemberUserDetails(Member member, List<Authority> authorities){
            this.username = member.getEmail();
            this.displayName = member.getName();
            this.password = member.getPassword();
            this.memberId = member.getId();
            this.authorities = authorities.stream()
                    .map(authority-> new SimpleGrantedAuthority(authority.
                            getAuthority())).toList();
        }
}
