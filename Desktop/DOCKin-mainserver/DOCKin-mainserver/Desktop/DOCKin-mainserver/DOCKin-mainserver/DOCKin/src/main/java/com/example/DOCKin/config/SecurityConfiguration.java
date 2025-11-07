package com.example.DOCKin.config;

import com.example.DOCKin.jwt.JwtAuthenticationFilter;
import com.example.DOCKin.jwt.JwtTokenProvider;
import com.example.DOCKin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(memberService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider())

                // 인가(Authorization) 설정 시작
                .authorizeHttpRequests(authorize -> authorize

                        // 1. 인증이 필요 없는 공통 API (로그인, 회원가입)
                        .requestMatchers("/", "/signup", "/login", "/api/auth/**").permitAll()

                        // 2. 근로자/관리자 모두 접근 (조회 및 이수 처리)
                        .requestMatchers("/api/safety/courses/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/safety/enroll/**").hasAnyRole("USER", "ADMIN")

                        // ⭐ 누락된 근로 동의서 관련 경로 추가 (USER, ADMIN 모두 허용)
                        .requestMatchers("/api/safety/agreement/**").hasAnyRole("USER", "ADMIN")

                        // 3. 관리자 전용 (CRUD 및 현황 조회)
                        .requestMatchers("/api/safety/admin/**").hasRole("ADMIN")
                        .requestMatchers("/member/**", "/admin/**").hasRole("ADMIN")


                        // 4. 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }
}