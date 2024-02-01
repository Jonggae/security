package com.example.securitydemo.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
/*
* JWT 를 생성하는 주된 로직들
* 로그 작성을 위한 Logger
* 권한 정보를 추출할 때 사용하는 AUTHORITIES_KEY = "auth"
* JWT 가 변조되지 않았음을 검증하는 secret (key 값)
* 토큰 만료시간 tokenExpirationTime 등 변수 설정
* */
@Component
public class TokenProvider implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenExpirationTime;
    private Key key;

    /*
    * jwt key 같은 정보는 유출되지 않도록 다른 파일에 저장하여 사용
    */
    public TokenProvider(
            @Value("${jwt.secret.key}") String secret,
            @Value("${jwt.expiration_time}") long tokenExpirationTime) {
        this.secret = secret;
        this.tokenExpirationTime = tokenExpirationTime * 1000;
    }
    /*
    * tokenProvider 빈이 초기화 될때, 설정된 secret 값을 디코딩하여
    * 해당 값을 JWT 서명에 사용할 수 있도록 디코딩한다.
    * HMAC-SHA (Hash-based Message Authentication Code with Secure Hash Algorithm)
    * 빈이 사용될 때 설정하는 역할
    */
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /*
    * 실제로 jwt 를 생성하는 메서드
    * 변수로 받는 Authentication 객체는 SpringSecurity 에서 사용자의 인증정보를 나타낸다.
    * 사용자의 식별 정보(username, password 따위), 자격 증명, 권한이 포함된다
    * 일반적으로 Principal, Authorities를 갖고있음.
    */

    /*
    * Principal
    * 사용자를 식별하는 정보, UserDetails 객체가 주로 이 역할을 한다.
    *
    * Authorities
    * 사용자에게 부여된 권한 정보. GrantedAuthority 인터페이스를 구현한 객체들의 컬렉션으로 표현
    * 일반적으로 username, password 로 로그인 하는 경우 Authentication 객체는 사용자의 아이디를
    * Principal 로, 사용자에게 부여된 권한 정보를 Authorities 로 갖고있음.
    * */

    /*
    * createToken 메서드를 통해 이러한 정보들을 가져온다.
    * 유저 정보와 권한, 생성 일시, 필요한 정보들을 넣고 JWT 객체를 생성함
    * builder 로 작성된 부분이
    * JWT 의 주제 (주로 username, 아이디 따위)
    * 사용자의 권한 정보 claim(Au~)
    * JWT 서명 (signWith~)
    * 만료 시간
    * 문자열로 압축 을 뜻한다.
    * */
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenExpirationTime);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();
    }

    /*
    * JWT 내의 클레임(정보)를 이용하는 메서드
    * 토큰을 파싱하여 클레임을 얻어온 후
    * 클레임 내의 권한 정보를 가져와 각 권한을 SimpleGrantedAuthority 로 변환하여 컬레션으로 만듬
    * 이때 AUTHORITIES_KEY 는 권한 정보를 추출할 때 사용
    *
    * JWT 의 사용자 정보로 User 객체를 생성함
    * 최종적으로 UsernamePasswordAuthenticationToken 을 반환한다. */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /*
    * JwtFilter 클래스 내의  doFilter 메서드에서 넘어옴
    * T, F 값을 리턴하며 유효성을 검증한다
    * JWT 를 서명하는데 사용되는 key 를 설정하고
    * 서명이 유효한 경우 Claim 을 반환함  -> True 로 넘겨줌
    * */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
