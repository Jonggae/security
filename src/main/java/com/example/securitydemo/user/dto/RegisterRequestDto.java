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

    private PasswordEncoder passwordEncoder;

    private String username;
    private String password;
    private String email;

    /*
    * @builder를 사용하기때문에 들어가는 setter*/
    public RegisterRequestDto encodePassword() {
        return RegisterRequestDto.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
    }
}
