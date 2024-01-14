package com.example.securitydemo.user.controller;

import com.example.securitydemo.user.dto.RegisterRequestDto;
import com.example.securitydemo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto requestDto) {
        userService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입 성공");
    }
}
