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
private String auth;

private List<GrantedAuthority> authorities;
}

