package com.example.DOCKin.service;

import com.example.DOCKin.mapper.UserMapper;
import com.example.DOCKin.model.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Service
public class AccountService implements UserDetailsService {
    private final UserMapper userMapper;

    public AccountService(UserMapper userMapper){
        this.userMapper=userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Account account = new Account();
        account.setId(username);
        account = userMapper.findUser(account);
        if(account!=null){
            List<GrantedAuthority> authorities = this.getAuthorities(userMapper.findAuthority(account).getAuth());
            return new User(account.getId(),account.getPassword(),authorities);
        }
        return null;
    }

    public List<GrantedAuthority> getAuthorities(String authorities){
        List<GrantedAuthority> list = new ArrayList<>();
        if(!"".equals(authorities) && authorities!= null){
            for(String str: Arrays.asList(authorities.split(","))){
                list.add(new SimpleGrantedAuthority((str.trim())));
            }
        }
        return list;
    }

}
