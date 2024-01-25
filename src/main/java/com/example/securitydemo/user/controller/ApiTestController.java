package com.example.securitydemo.user.controller;

import com.example.securitydemo.common.dto.TokenDto;
import com.example.securitydemo.common.jwt.JwtFilter;
import com.example.securitydemo.common.jwt.TokenProvider;
import com.example.securitydemo.user.dto.LoginRequestDto;
import com.example.securitydemo.user.dto.UserDto;
import com.example.securitydemo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiTestController {

    private final UserService userService;

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // Postman으로 테스트 하기 위함
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@Valid @RequestBody UserDto requestDto) {
//        userService.register(requestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입 완료");
//    }
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto requestDto) {
        return ResponseEntity.ok(userService.register(requestDto));

    }

    // jwt 토큰 발급
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    // 인증되지 않은 (로그인하지 않은) 사용자 접근 시 표시
    @GetMapping("/private")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }
}