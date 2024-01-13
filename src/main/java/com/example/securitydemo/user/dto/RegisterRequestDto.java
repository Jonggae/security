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
    private PasswordEncoder passwordEncoder;

    public RegisterRequestDto encodePassword() {
        return RegisterRequestDto.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .passwordEncoder(passwordEncoder)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
    }

    public RegisterRequestDto passwordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder=passwordEncoder;
        return this;
    }
}
