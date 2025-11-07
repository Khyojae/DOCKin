package com.example.DOCKin.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class LoginUtil{
    public static boolean isManger(){

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            return false;
        }

        Object principal = authentication.getPrincipal();
        if((principal instanceof UserDetails)){
            return authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(auth ->"ROLE_ADMIN".equals(auth.getAuthority()));
        }

        if(principal instanceof  String){
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(auth->"ROLE_ADMIN".equals(auth.getAuthority()));

    }
}


