package com.example.securitydemo.user.dto;

import lombok.*;

/*
* User 엔티티를 설정할 때 필요한 권한 들을 담아오는 Dto
* ROLE_USER 와 ROLE_ADMIN 이 들어감
*/

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDto {
   private String authorityName;
}