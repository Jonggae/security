package com.example.securitydemo.user.controller;

import com.example.securitydemo.user.dto.UserDto;
import com.example.securitydemo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/*
* ViewController 와 비슷한 역할을 하고있으므로 추후 삭제 예정
* TODO : 삭제 또는 수정 */

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequestDto requestDto) {
//        userService.register(requestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입 완료");
//    }
    @PostMapping("/register")
    public String register(@RequestBody UserDto requestDto) {
        userService.register(requestDto);
        return "redirect:/login";
    }
}
