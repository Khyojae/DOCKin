package com.example.DOCKin.config;

import com.example.DOCKin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity // 💡 WebSecurity 활성화
public class SecurityConfiguration {
    @Autowired
   private MemberService memberService;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .userDetailsService(memberService)
                // API 환경 테스트를 위해 CSRF 비활성화 (필요에 따라 withDefaults()로 활성화 가능)
                .csrf(AbstractHttpConfigurer::disable)

                // 인가(Authorization) 설정 시작
                .authorizeHttpRequests(authorize -> authorize
                        // 1. 모두 접근 가능한 경로 설정 (로그인 화면, 회원가입 등)
                        .requestMatchers("/", "/signup", "/login").permitAll()

                        // 2. 작업 일지 목록/내용 조회 (일반 사용자도 자신의 것은 볼 수 있어야 하므로 인증만 요구)
                        // 회원 자신만 보게 하는 필터링은 Service 계층에서 처리합니다.
                        .requestMatchers("/Work_logs/list", "/WorkLogs/content").authenticated()

                        // 3. ADMIN 권한이 필요한 경로 설정
                        .requestMatchers("/member/**", "/admin/**").hasRole("ADMIN")

                        // 4. 그 외 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                )

                // 폼 로그인 설정
                .formLogin(form -> form
                        // 커스텀 로그인 페이지 URL (HTML 페이지를 만들 경우)
                        // API 환경에서는 실제로 페이지를 보여주지 않고 인증 요청을 처리합니다.
                        .loginPage("/login")
                        .loginProcessingUrl("/login") // 로그인 처리 POST 요청 URL (이 URL로 요청을 보냄)
                        .usernameParameter("user_id")
                        .defaultSuccessUrl("/",true) // 로그인 성공 시 이동할 URL
                        .permitAll() // loginPage와 관련된 리소스에 모두 접근 허용
                )


                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout") // /logout POST 요청으로 처리 (기본)
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}