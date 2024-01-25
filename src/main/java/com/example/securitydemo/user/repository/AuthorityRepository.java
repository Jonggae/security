package com.example.securitydemo.user.repository;

import com.example.securitydemo.user.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
