package com.springboot.tutorial.config;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component // 해당 어노테이션은 웹 서버 동작 시에 빈으로 자동 주입된다.
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsService userDetailsService;

    @Value("${springboot.jwt.secret}")
    private String secretKey = "secretKey";
    private final long tokenValidMillisecond = 1000L * 60 * 60;

    /**
     * secretKey를 Base64 인코딩한다.
     */
    @PostConstruct // 해당 객체가 빈 객체로 주입된 이후 수행되는 메서드를 가리킨다.
    protected void init() {
        LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
        System.out.println("before : " + secretKey);
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        System.out.println("after :" + secretKey);
        LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
    }

    /**
     * JWT 토큰을 생성한다.
     *
     * @param userUid 사용자의 uuid
     * @param roles 사용자의 권한
     * @return JWT 토큰
     */
    public String createToken(String userUid, List<String> roles) {
        LOGGER.info("[createToken] 토큰 생성 시작");

        // JWT 토큰에 값을 넣기 위한 Claims 객체 생성
        Claims claims = Jwts.claims().setSubject(userUid);
        claims.put("roles", roles);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        LOGGER.info("[createToken] 토큰 생성 완료");

        return token;
    }

    /**
     * SecurityContextHolder에 저장할 Authentication을 생성한다.
     *
     * @param token JWT 토큰
     * @return 인증 정보
     */
    public Authentication getAuthentication(String token) {
        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 시작");

        // UsernamePasswordAuthenticationToken을 사용하려면 UserDetails 객체가 필요하다.
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));

        LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 완료. UserDetails UserName : {}", getUsername(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * secretKey를 설정하고 클레임을 추출해서 토큰을 생성하기 위해 필요한 subject 값을 반환한다.
     *
     * @param token JWT 토큰
     * @return 서브젝트? 정보
     */
    private String getUsername(String token) {
        LOGGER.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        LOGGER.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);

        return info;
    }

    /**
     * HttpServletRequest 객체에서 헤더값으로 전달된 "X-AUTH-TOKEN" 값을 가져와 반홯난다.
     *
     * @param request 클라이언트 요청
     * @return JWT 토큰
     */
    public String resolveToken(HttpServletRequest request) {
        LOGGER.info("[resolveToken] HTTP 헤더에서 Token 값 추출");

        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * JWT 토큰에 대한 클레임 유효기간을 확인한다.
     *
     * @param token JWT 토큰
     * @return true or false
     */
    public boolean validateToken(String token) {
        LOGGER.info("[validateToken] 토큰 유효 체크 시작");

        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());

        } catch (Exception e) {
            LOGGER.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
