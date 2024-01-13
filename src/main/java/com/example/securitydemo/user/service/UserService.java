package com.example.securitydemo.user.service;

import com.example.securitydemo.user.dto.RegisterRequestDto;
import com.example.securitydemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequestDto requestDto) {
        checkUserinfo(requestDto.getUsername(), requestDto.getEmail());
        RegisterRequestDto encodeDto = requestDto.encodePassword(passwordEncoder);
        userRepository.save(encodeDto.toEntity());

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

}
