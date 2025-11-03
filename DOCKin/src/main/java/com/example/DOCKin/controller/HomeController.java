package com.example.DOCKin.controller;

import com.example.DOCKin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberservice;

    @GetMapping("/")
    public String getHome(){
        return "redirect:/login";
    }

    @GetMapping("/admin/main")
    public String adminMain() {
        return "admin/main"; // templates/main.html을 찾습니다.
    }

    @GetMapping("/user/main")
    public String mainPage() {
        return "user/main"; // templates/main.html을 찾습니다.
    }

    @GetMapping("/login")
    public String getLogin(){
     return "login";
    }

    @GetMapping("/logout")
    public String getLogout(){
    return "logout";
    }
}
