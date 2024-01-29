package com.example.securitydemo.user.repository;

import com.example.securitydemo.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
* JpaRepository 를 함께 사용하는 레포지토리
*
* 데이터베이스에 접근하고 저장, 조회, 수정, 삭제하는데 필요한 메서드를 제공
* 자세한 내용은 알아서 더 찾아보기 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
/*
* sql 쿼리들을 사용한다. 읽어보면 어느정도 파악 가능.*/
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
