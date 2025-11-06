package com.example.bul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // H2 콘솔 경로만 모두 허용
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // H2 콘솔 CSRF 예외 처리
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // H2 콘솔 프레임 허용
                .formLogin(withDefaults()); // 스프링 시큐리티의 기본 폼 로그인 사용
        return http.build();
    }
}