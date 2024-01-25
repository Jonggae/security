package com.example.securitydemo.user.dto;

import com.example.securitydemo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    private String username;
    private String password;
    private String email;

//    @Builder.Default
//    private UserRoleEnum role = UserRoleEnum.USER;

    private Set<AuthorityDto> authorityDtoSet;


    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
    }

    public void setPassword(String password) {
        this.password = password;

    }


}
