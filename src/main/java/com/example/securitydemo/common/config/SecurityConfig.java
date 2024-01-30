package com.example.securitydemo.common.config;

import com.example.securitydemo.common.jwt.JwtAccessDeniedHandler;
import com.example.securitydemo.common.jwt.JwtAuthenticationEntryPoint;
import com.example.securitydemo.common.jwt.JwtSecurityConfig;
import com.example.securitydemo.common.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/*
 * SpringSecurity 의 설정들을 모아놓은 클래스
 * 어노테이션의 사용으로 쉽게 지정할 수 있음.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    /*
     * jwt 생성용 TokenProvider
     * CORS 설정을 위한 CorsFilter
     * 유효하지 않은 인증을 진행하면 401 에러를 반환하는 JwtAuthenticationEntryPoint
     * 필요한 권한이 없이 접근할 때 JwtAccessDeniedHandler
     * todo : 아래 2개 클래스는 클래스명 변경을 하여도 좋을 것 같음
     */
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /*
     * 회원 가입시 작성한 비밀번호 암호화, 복호화를 위한 PasswordEncoder
     * @Bean 을 이용하여 빈으로 등록하여 사용하여야 함
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * 다양한 filter 들을 관리하는 SecurityFilterChain
     * 여러 설정들을 이 빈에서 진행한다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
         * CSRF 비활성화
         * Cross-Site Request Forgery 는 악의적인 웹사이트 공격으로 사용자의 인증정보를 사용하여
         * 해당 사용자의 권한으로 요청을 보내는 공격이다. -> 해킹이네
         * 단순한 웹 페이지에서는 CSRF 비활성화하는 편*/
        http.csrf(AbstractHttpConfigurer::disable);

        /*
         * addFilterBefore 를 이용하여 특정한 필터를 다른 필터보다 먼저 실행되도록 등록함
         * 발생할 수 있는 예외도 등록하여 처리함 ( jwtAccessDeniedHandler, jwtAuthenticationEntryPoint)
         */
        http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint));

        /*h2 console 디스플레이 설정*/
        http.headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        /*
         * 인증이 필요한 엔드포인트들을 설정하는 부분
         * 회원가입, 로그인 페이지들은 인증없이 접근이 가능해야하기 때문에 permitAll 로 열어준다.
         * 나머지 페이지들은 인증 과정이 필요하기에 그 외 요청은 authenticated() 를 걸어준다.
         * */
        http.authorizeHttpRequests((authorizeHttpRequest ->
                authorizeHttpRequest
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/api/authenticate", "/api/register").permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated())
        );

        /*jwt를 사용하므로 세션은 사용하지 않음으로 설정*/
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /*jwt 인증 과정 추가 */
        http.with(new JwtSecurityConfig(tokenProvider), customizer -> {
        });


        return http.build();
    }
}
