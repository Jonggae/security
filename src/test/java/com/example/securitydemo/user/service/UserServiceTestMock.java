//package com.example.securitydemo.user.service;
//
//import com.example.securitydemo.user.dto.RegisterRequestDto;
//import com.example.securitydemo.user.repository.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.verify;
//
//@SpringBootTest
//@Transactional
//public class UserServiceTestMock {
//    @InjectMocks
//    private UserService userService;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    @DisplayName("Mock 을 이용한 회원가입 테스트")
//    void registerTest() {
//        //given
//        RegisterRequestDto requestDto = registerRequestDto();
//        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.empty());
//        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
//        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");
//
//        //when
//
//        userService.register(requestDto);
//        //then
//
//        verify(userRepository, Mockito.times(1)).save(any());
//
//    }
//
//    private RegisterRequestDto registerRequestDto() {
//        return RegisterRequestDto.builder()
//                .username("CJW")
//                .password("1234")
//                .email("CJW@mail.com")
//                .build();
//    }
//}
