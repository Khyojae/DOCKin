package com.example.DOCKin.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="password")
public class Account {
private String id;
private String password;
private String auth;  // 권한
private List<GrantedAuthority> authorities; //권한관리
}

