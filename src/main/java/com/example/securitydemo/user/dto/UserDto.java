package com.example.securitydemo.user.dto;

import com.example.securitydemo.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;

    private Set<AuthorityDto> authorityDtoSet;

    public static UserDto from(User user){
        if (user == null) return null;

        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();

    }

}
