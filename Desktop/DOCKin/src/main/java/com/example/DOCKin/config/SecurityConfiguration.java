package com.example.DOCKin.config;

import com.example.DOCKin.jwt.JwtAuthenticationFilter;
import com.example.DOCKin.jwt.JwtTokenProvider;
import com.example.DOCKin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                .cors(Customizer.withDefaults())
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
                        .requestMatchers("/api/notifications/**").hasAnyRole("USER", "ADMIN")

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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ⭐ 필요에 따라 설정 변경 (예: 프론트엔드 URL, 허용 메서드 등)
        configuration.addAllowedOriginPattern("*"); // 모든 출처 허용 (보안에 주의하여 특정 출처로 제한 권장)
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 (GET, POST, PUT, DELETE 등) 허용
        configuration.addAllowedHeader("*"); // 모든 헤더 허용 (Authorization 등)
        configuration.setAllowCredentials(true); // 자격 증명(쿠키, 인증 헤더) 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로 (/**)에 대해 위의 설정 적용
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
