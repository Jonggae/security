package com.example.securitydemo.user.controller;

import com.example.securitydemo.common.security.UserDetailsImpl;
import com.example.securitydemo.user.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/*
* 프론트페이지를 연결할 컨트롤러
* TODO : 추후 개발 */

@Controller
public class ViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "login-page";
    }

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @GetMapping("/homepage")
    public String showHomepage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        System.out.println("user.getUsername() = " + user.getUsername());
        return "redirect:/homepage";
    }


}
