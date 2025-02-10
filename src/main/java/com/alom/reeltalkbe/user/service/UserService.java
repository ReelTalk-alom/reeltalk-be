package com.alom.reeltalkbe.user.service;


import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.image.service.ImageUploadService;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import com.alom.reeltalkbe.user.dto.JoinDto;
import com.alom.reeltalkbe.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageUploadService imageUploadService;

    @Autowired
    public UserService(UserRepository userRepository, ImageUploadService imageUploadService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.imageUploadService = imageUploadService;
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

//        User user = new User();
//        user.setEmail();
//        user.setUsername);
//        user.setPassword();
//        user.setRole("ROLE_ADMIN");

        User user = User.builder()
                .email(joinDto.getEmail())
                .password(bCryptPasswordEncoder.encode(joinDto.getPassword()))
                .username(joinDto.getUsername())
                .role("ROLE_ADMIN")
                .build();

        // 회원 저장
        return userRepository.save(user);
    }

    public String uploadProfileImage(MultipartFile multipartFile) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            String username = ((CustomUserDetails) principal).getUsername();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            String imageUrl = imageUploadService.uploadFile(multipartFile);
            user.setImageUrl(imageUrl);
            userRepository.save(user);

            return imageUrl;
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }

    public void deleteProfileImage() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            String username = ((CustomUserDetails) principal).getUsername();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            imageUploadService.deleteFile(user.getImageUrl());
            user.setImageUrl(null);
            userRepository.save(user);
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }

    public String getProfileImage() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            String username = ((CustomUserDetails) principal).getUsername();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            return user.getImageUrl();
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }
}
