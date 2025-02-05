package com.alom.reeltalkbe.service;


import com.alom.reeltalkbe.domain.User;
import com.alom.reeltalkbe.dto.JoinDto;
import com.alom.reeltalkbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    // 회원가입 기능
    public User registerUser(JoinDto joinDto) {
        // 이메일 중복 체크
        Optional<User> existingUser = userRepository.findByEmail(joinDto.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // 닉네임 중복 체크
        Optional<User> existingNickname = userRepository.findByUsername(joinDto.getUsername());
        if (existingNickname.isPresent()) {
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
        }

        User user = new User();
        user.setEmail(joinDto.getEmail());
        user.setUsername(joinDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword()));
        user.setRole("ROLE_ADMIN");

        // 회원 저장
        return userRepository.save(user);
    }
}
