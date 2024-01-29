package com.example.securitydemo.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/*
* 회원가입 시 회원의 Authority 를 설정하는 클래스
* String 으로 ROLE_USER 인지, ROLE_ADMIN 인지를 생성자를 통해 생성할 때 정함. (builder 사용) */

@Entity
@Table(name = "authority")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;
}
