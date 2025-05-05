package com.alom.reeltalkbe.domain.user.repository;

import com.alom.reeltalkbe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // 이메일로 회원을 찾을 수 있음
    Optional<User> findByUsername(String username);  // 닉네임으로 회원 찾을 수 있음
}
