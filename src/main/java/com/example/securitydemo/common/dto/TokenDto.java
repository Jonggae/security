package com.example.securitydemo.common.dto;

import lombok.*;
/*
* jwt 토큰을 전달하는 Dto */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String token;
}
