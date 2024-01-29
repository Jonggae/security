package com.example.securitydemo.user.service;

import com.example.securitydemo.common.exception.NotFoundMemberException;
import com.example.securitydemo.common.security.SecurityUtil;
import com.example.securitydemo.user.domain.Authority;
import com.example.securitydemo.user.domain.User;
import com.example.securitydemo.user.dto.UserDto;
import com.example.securitydemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
/*
* UserService 클래스

* 1. register : 회원 가입 메서드
* 회원 가입 시 필요한 로직들을 작성
* checkUserinfo 를 이용하여 username, email 이 이미 등록된 정보인지 확인한 후 회원가입 로직 진행
* 아래에서 계속
*/

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto register(UserDto userDto) {
        checkUserinfo(userDto.getUsername(), userDto.getEmail());

        /*
        * Authority 클래스에서는 회원가입시 권한이 ROLE_USER 를 갖는 객체를 생성함
        */
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER").build();
        /*먼저 ROLE_USER 라는 정보를 authority 변수에 추가함 */

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .authorities(Collections.singleton(authority))
                .build();
        /*
        Dto 를 통해 User 정보를 입력받고 user 객체를 생성함
        * username, password(암호화 됨), email, 위에서 만든 authority 까지 추가하여 user 객체를 생성하였다.
        */

        return UserDto.from(userRepository.save(user));
        /*
        * from 메서드를 이용해 생성된 객체를 Repository 에서 저장함
        * from 메서드에서 자세한 내용 확인하기
        */
    }
/*이하 유저의 정보를 확인하는 로직 (메서드 분리) -> 필요없을듯? */
    public void checkUserinfo(String username, String email) {
        checkUsername(username);
        checkEmail(email);
    }

    public void checkUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다");
        }
    }

    public void checkEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다");
        }
    }

    /*로그인 (인증)후 필요한 로직들. 이후 해당 내용에서 설명 추가함*/


    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("회원을 찾을 수 없습니다"))
        );
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

}
