package com.example.securitydemo.user.controller;

import com.example.securitydemo.user.dto.RegisterRequestDto;
import com.example.securitydemo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // 회원가입
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequestDto requestDto) {
//        userService.register(requestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입 성공");
//    }

    @PostMapping(value = "/register")
    public String register(RegisterRequestDto requestDto) {
        userService.register(requestDto);
        return "redirect:/login"; //회원가입 후 로그인 페이지로 이동
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}
