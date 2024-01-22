package com.example.securitydemo.common.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

//JWT 관련 메서드
@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpireTime;

    public JwtUtil(
            @Value("${jwt.secret.key}") String secretKey,
            @Value("${jwt.expiration_time}") long accessTokenExpireTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpireTime = accessTokenExpireTime;
    }
}
