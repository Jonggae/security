package com.example.securitydemo.user.service;

import com.example.securitydemo.user.dto.RegisterRequestDto;
import com.example.securitydemo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("유저 회원 가입 테스트")
    void registerUser() {
        //given
        RegisterRequestDto requestDto = registerRequestDto(passwordEncoder);

        //when & then
        userService.register(requestDto);

        verify(userRepository, times(1)).save(any());

    }


    private RegisterRequestDto registerRequestDto(PasswordEncoder passwordEncoder) {
        return RegisterRequestDto.builder()
                .username("username")
                .password(passwordEncoder.encode("password"))
                .email("username@mail.com")
                .passwordEncoder(passwordEncoder)
                .build();
    }

}