package com.example.securitydemo.user.controller;

import com.example.securitydemo.common.security.UserDetailsImpl;
import com.example.securitydemo.user.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
