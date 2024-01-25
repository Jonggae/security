//package com.example.securitydemo.user.service;
//
//import com.example.securitydemo.user.dto.RegisterRequestDto;
//import com.example.securitydemo.user.repository.UserRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//
//@SpringBootTest
//@Transactional
//class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//    @Mock
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder; // Mock이 아닌 Autowired 를 해주자 ... 뭔가 바뀐다.
//
//
//    @Test
//    @Disabled
//    @DisplayName("비밀번호 암호화 테스트")
//    void encryptPassword() {
//        //given
//        String rawPassword = "1234";
//
//        //when
//        String encodedPassword = passwordEncoder.encode(rawPassword);
//
//        //then
//        Assertions.assertAll(
//                () -> Assertions.assertNotEquals(rawPassword, encodedPassword),
//                () -> Assertions.assertTrue(passwordEncoder.matches(rawPassword, encodedPassword)));
//    }
//
//    @Test
//    @DisplayName("유저 회원 가입 테스트")
//    void registerUser() {
//        //given
//        RegisterRequestDto requestDto = registerRequestDto();
//        String rawPassword = requestDto.getPassword();
//
//        //when
//        userService.register(requestDto);
//        //then
//        userRepository.findByEmail(requestDto.getEmail()).ifPresent(user -> {
//            String encodedPassword = user.getPassword();
//            System.out.println(encodedPassword);
//
//            Assertions.assertAll(
//                    () -> Assertions.assertNotEquals(rawPassword, encodedPassword),
//                    () -> Assertions.assertTrue(passwordEncoder.matches(rawPassword, encodedPassword))
//            );
//        });
//    }
//
//        private RegisterRequestDto registerRequestDto () {
//            return RegisterRequestDto.builder()
//                    .username("CJW")
//                    .password("1234")
//                    .email("CJW@mail.com")
//                    .build();
//        }
//
//
//    }