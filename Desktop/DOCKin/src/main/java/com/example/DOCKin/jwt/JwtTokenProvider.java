package com.example.DOCKin.jwt;

import com.example.DOCKin.service.MemberService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    private final Key key;
    private final long tokenValidityInMilliseconds;
    private final MemberService memberService;

    public JwtTokenProvider(
    @Value("${jwt.secret-key}") String secretKey,
    @Value("${jwt.token-validity-in-milliseconds}") long tokenValidityInMilliseconds,
    MemberService memberService){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
        this.memberService = memberService;
    }

    //Access Token 생성
    public String createToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now+this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();
    }

    //Jwt 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends  GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 1. UserDetails 객체를 가져옵니다.
        UserDetails principal = memberService.loadUserByUsername(claims.getSubject());

        // 2. Authentication 객체 생성 시, principal 객체를 사용합니다.
        // 두 번째 인자 (credentials)는 토큰 사용 후 필요 없으므로 null을 사용하거나, 관례적으로 토큰 자체를 넣습니다.
        // 하지만 principal을 사용해야 @AuthenticationPrincipal MemberUserDetails userDetails가 제대로 주입됩니다.
        return new UsernamePasswordAuthenticationToken(principal, null, authorities); // ⭐ principal 사용
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다.", e); // 💡 로그 추가
        } catch (ExpiredJwtException e){
            log.info("만료된 JWT 토큰입니다.", e); // 💡 로그 추가
        } catch (UnsupportedJwtException e){
            log.info("지원되지 않는 JWT 토큰입니다.", e); // 💡 로그 추가
        } catch (IllegalArgumentException e){
            log.info("JWT 토큰이 잘못되었습니다.", e); // 💡 로그 추가 (null이거나 빈 문자열)
        }
        return false; // 💡 예외가 발생하면 false 반환
    }
    //Request Header에서 토큰 정보 추출 메소드
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        System.out.println("DEBUG: Authorization Header = " + bearerToken); // 💡 추가
        if(bearerToken !=null && bearerToken.startsWith("Bearer ")){
            String token = bearerToken.substring(7);
            System.out.println("DEBUG: Extracted Token = " + token); // 💡 추가
            return token;
        }
        return null;
    }
}
