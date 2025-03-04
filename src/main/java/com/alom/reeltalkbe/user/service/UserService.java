package com.alom.reeltalkbe.user.service;


import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.image.service.ImageUploadService;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import com.alom.reeltalkbe.user.dto.JoinDto;
import com.alom.reeltalkbe.user.repository.RefreshRepository;
import com.alom.reeltalkbe.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageUploadService imageUploadService;
    private final RefreshRepository refreshRepository;

    @Autowired
    public UserService(UserRepository userRepository, ImageUploadService imageUploadService, RefreshRepository refreshRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.imageUploadService = imageUploadService;
        this.refreshRepository = refreshRepository;
    }

    // 회원가입 기능
    public User registerUser(JoinDto joinDto) {
        // 이메일 중복 체크
        Optional<User> existingUser = userRepository.findByEmail(joinDto.getEmail());
        if (existingUser.isPresent()) {
            throw new BaseException(BaseResponseStatus.EXIST_EMAIL);
        }

        // 닉네임 중복 체크
        Optional<User> existingNickname = userRepository.findByUsername(joinDto.getUsername());
        if (existingNickname.isPresent()) {
            throw new BaseException(BaseResponseStatus.EXIST_USERNAME);
        }

        User user = User.builder()
                .email(joinDto.getEmail())
                .password(bCryptPasswordEncoder.encode(joinDto.getPassword()))
                .username(joinDto.getUsername())
                .role("ROLE_ADMIN")
                .build();

        // 회원 저장
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId, HttpServletResponse response) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        // 🔥 username을 기준으로 refresh token 삭제
        refreshRepository.deleteByUsername(user.getUsername());

        // 🔥 Refresh 토큰 쿠키 삭제
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 🔥 회원 데이터 삭제
        userRepository.delete(user);
    }


    public String uploadProfileImage(MultipartFile multipartFile) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            String username = ((CustomUserDetails) principal).getUsername();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

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
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

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
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

            return user.getImageUrl();
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }
}
