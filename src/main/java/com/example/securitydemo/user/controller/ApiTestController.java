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

/*
 * view 페이지가 없으므로 postmans 같은 테스트 어플을 이용하기 위한 컨트롤러
 * api 엔드포인트에 따라 접근 로직이 다르다.
 * */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiTestController {

    /*의존성 주입*/
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /*회원가입 api 접근
    UserDto 를 받아와서 userService.register 메서드로 전달하여 저장 진행.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto requestDto) {
        return ResponseEntity.ok(userService.register(requestDto));
    }

    /*jwt 토큰 발급
     * todo : authenticate -> login 으로 변경하면 좋을 것 같음.
     * 아니면 로그인 과정을 다시 개발하여?? (생각 필요)
     * TODO : 변수명 변경, 메서드 분리 등
     * 코멘트 :
     * 해당 /authenticate api 는 provider 에서 구현해주세요.
     * 추가로 SecurityContextHolder 는 로그인시에 인증정보를 저장하지 않아도 상관없습니다.
     * SecurityContextHolder 의 역할을 조금더 찾아보시고 고민해 보시며 좋을 것 같습니다.
     */

    /*로그인 로직 설명 시 내용 추가*/
    /*
    * 로그인 (authenticate)과정
    * */
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    /*
    * 인증 여부에 따른 접근 제한
    * todo : private -> user 로 변경
    * USER 는 자신의, ADMIN 은 특정 유저 개인 정보는 확인할 수 있음.
    */
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