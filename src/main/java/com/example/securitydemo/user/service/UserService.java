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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

//    public void register(RegisterRequestDto requestDto) {
//        checkUserinfo(requestDto.getUsername(), requestDto.getEmail());
//        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
//        requestDto.setPassword(encodedPassword);
//        userRepository.save(requestDto.toEntity());
//    }

    public UserDto register(UserDto userDto) {
        checkUserinfo(userDto.getUsername(), userDto.getEmail());

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER").build();

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .authorities(Collections.singleton(authority))
                .build();

        return UserDto.from(userRepository.save(user));
    }

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
