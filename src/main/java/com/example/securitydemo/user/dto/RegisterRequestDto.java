package com.example.securitydemo.user.dto;

import com.example.securitydemo.user.domain.User;
import com.example.securitydemo.user.role.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    private String username;
    private String password;
    private String email;

    @Builder.Default
    private UserRoleEnum role = UserRoleEnum.USER;


    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .build();
    }

    public void setPassword(String password) {
        this.password = password;

    }


}
