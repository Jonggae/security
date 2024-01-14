package com.example.securitydemo.user.dto;

import com.example.securitydemo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    private String username;
    private String password;
    private String email;


    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(username)
                .password(encodePassword(passwordEncoder))
                .email(email)
                .build();
    }

    private String encodePassword(PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }

}
