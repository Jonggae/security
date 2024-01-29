package com.example.securitydemo.user.dto;

import com.example.securitydemo.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;
/*
 * UserDto 클래스
 * Data Transfer Object 인 DTO 를 만듦
 * User 엔티티의 객체를 UserDto 로 변환하기 위함.
 * User 엔티티를 구성하는 정보는 사용자로부터 입력받음*/

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;

    /*이상 입력받을 데이터들*/
    private Set<AuthorityDto> authorityDtoSet;

    /*
     * 유저의 권한을 정하고 저장하는 DTO. AuthorityDto 를 통해 Set 으로 저장함
     * 클래스 내에는 AuthorityName 정보가 들어있다. (ADMIN, USER 중 하나가 될것)
     */

    public static UserDto from(User user) {
        if (user == null) return null;

        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
    /*
     * from 메서드의 기능
     * User 객체를 입력으로 받음. 간단히 비어있는 값이 오면 null 을 반환
     * User 객체에서 필요한 정보를 추출하여 UserDto 객체를 생성함
     * UserDto 에서는 빌더 패턴을 이용해 객체를 생성
     * UserDto 에 포함되는 내용은 username, email, AuthorityDto (권한) set
     * getAuthorites()를 이용하여 User 엔티티에 연결된 Authority 객체들을 AuthorityDto로 변환하고
     * 이를 set 으로 수집함 */

}
