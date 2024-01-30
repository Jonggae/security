package com.example.securitydemo.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
/*
 * jwt 인증을 처리하는 필터
 * JWT 를 처리하여 인증이 유효한 경우 SecurityContextHolder 에 인증 정보를 저장한다.
 * */

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /*
     * GenericFilterBean 을 확장하여 사용하기 때문에
     * doFilter 재정의 하여 사용한다. 서블릿 요청이나 응답이 발생할때 호출되며 JWT 인증을 처리함
     * 메서드 동작 과정을 잘 살펴보자.
     */

    /*
     * resolveToken 을 이용해 토큰을 추출하고  저장함. [jwt]
     * 요청이 온 URI 도 저장함 [requestURI]
     * tokenProvider.validateToken 메서드를 이용하여 검증한 토큰이 올바르고 (T,F로 전달)
     * 요청에 추출한 JWT 모두 존재하면
     * -> 즉 유효한 토큰이면,
     * 토큰에 담겨온 인증정보 authentication 을 ContextHolder 에 저장함
     * getContext().setAuthentication() 메서드
     * */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /*
     * HTTP 요청에서 Bearer 로 붙어 전달되어오는 JWT 추출
     * */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
