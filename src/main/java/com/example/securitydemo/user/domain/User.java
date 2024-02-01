package com.example.securitydemo.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Set;

/*
 * 중요한 User 엔티티

 * */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
/*
 * sql 예약어로 user 가 존재하기 때문에 테이블의 이름은 users 와 같이 다른 단어를 사용하거나
 * 백틱 ` 을 사용하여 감싸준다. 백틱은 눈에 잘 안띄어서 users 로 설정함.*/
public class User {

    /*
     * DB상 컬럼의 이름을 지정하고 기타 설정들을 할 수있음.
     * Id는 등록된 유저가 늘어날 때 마다 하나씩 증가하게 됨. (숫자)
     * username, password, email 데이터들
     * password 는 암호화되어 들어오기때문에 DB 내에서도 확인할 수 없음.
     */

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

//    @Enumerated(value = EnumType.STRING)
//    private UserRoleEnum role;
    /* RoleEnum 은 사용하지 않음*/

    @ManyToMany
    @JoinTable(name = "user_authority", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    /*
     * 사용자(User)와 권한(Authority)의 다대다 관계
     * user_authority 라는 테이블을 사용하여 user 와 authority 를 연결함.
     * user_authority 테이블을 중간 테이블로 사용하여 두 엔티티 간의 다대다 관계를 표현*/
}
