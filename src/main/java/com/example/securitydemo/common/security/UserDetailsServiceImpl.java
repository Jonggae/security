package com.example.securitydemo.common.security;

import com.example.securitydemo.user.domain.User;
import com.example.securitydemo.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/*
* 사용자 정보를 로드하는 인터페이스 UserDetailsService 를 구현하여 사용*/

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /*
    * loadUserByUsername 메서드는 DB의 사용자 정보를 조회하여 반환한다.
    * 이 정보를 기반으로 Security 에서 사용자를 인증하고 권한을 부여한다.
    *  */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    /*
    * User 엔티티 객체에서 UserDetails 를 구현하는 객체 생성
    * Security의 *User 클래스와 프로젝트 내의 *User 클래스를 혼동하지 않도록 주의
    * GrantedAuthority 로 권한정보를 추출하고 SimpleGrantedAuthority 객체로 매핑
    * 이후 UserDetails 객체 생성 (return 이후)
    * getUsername, getPassword로 UserDetails 에 사용할 정보들을 채워준다. (권한, 사용자 정보)*/
    private org.springframework.security.core.userdetails.User createUser(String username, User user) {
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
}

