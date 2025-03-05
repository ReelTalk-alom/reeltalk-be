package com.alom.reeltalkbe.user.service;


import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.image.service.ImageUploadService;
import com.alom.reeltalkbe.review.repository.ReviewRepository;
import com.alom.reeltalkbe.review.service.ReviewService;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.dto.*;
import com.alom.reeltalkbe.user.repository.RefreshRepository;
import com.alom.reeltalkbe.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImageUploadService imageUploadService;
    private final RefreshRepository refreshRepository;
    private final ReviewRepository reviewRepository; //추가1
    private final ReviewService reviewService;  // 추가2

    @Autowired
    public UserService(UserRepository userRepository,
                       ImageUploadService imageUploadService,
                       RefreshRepository refreshRepository,
                       ReviewRepository reviewRepository, ReviewService reviewService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.imageUploadService = imageUploadService;
        this.refreshRepository = refreshRepository;
        this.reviewRepository = reviewRepository; //추가
        this.reviewService = reviewService;  // 추가2
    }

    // 회원가입 기능

    //이메일 중복 체크
    public User registerUser(JoinDto joinDto) {
        Optional<User> existingUser = userRepository.findByEmail(joinDto.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }
        //닉네임 중복 체크
        Optional<User> existingNickname = userRepository.findByUsername(joinDto.getUsername());
        if (existingNickname.isPresent()) {
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
        }

        User user = User.builder()
                .email(joinDto.getEmail())
                .password(bCryptPasswordEncoder.encode(joinDto.getPassword()))
                .username(joinDto.getUsername())
                .role("ROLE_ADMIN")
                .build();

        // description이 없으면 기본 값 설정
        if (joinDto.getDescription() == null || joinDto.getDescription().isEmpty()) {
            user.setDescription("안녕하세요! 반갑습니다.");  // 기본값 설정
        } else {
            user.setDescription(joinDto.getDescription());
        }

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

    // 마이페이지 수정 API
    @Transactional
    public User updateMyPage(Long userId, MyPageUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 닉네임 변경이 있는 경우만 검사 & 업데이트
        if (requestDto.getUsername() != null && !requestDto.getUsername().isEmpty()) {
            if (!user.getUsername().equals(requestDto.getUsername())) {
                if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
                    throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
                }
                user.setUsername(requestDto.getUsername());  // 닉네임 변경 가능하게
            }
        }

        // 자기소개 변경 (null이면 기존 값 유지)
        if (requestDto.getDescription() != null) {
            user.setDescription(requestDto.getDescription());
        }

        return user;  //
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

    @Transactional(readOnly = true)
    public MyPageResponseDto getMyPageInfo(Long userId) {
        // 1) 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2) 좋아요 많은 순 리뷰 10개 가져오기
        Pageable top10Pageable = PageRequest.of(0, 10);
        List<MyPageReviewDto> bestReviews = reviewRepository.findTopReviewsByUserId(userId, top10Pageable);

        // 3) 최신순 리뷰 가져오기
        List<MyPageReviewDto> recentReviews = reviewRepository.findRecentReviewsByUserId(userId, top10Pageable);

        return new MyPageResponseDto(
                user.getId(),
                user.getUsername(),
                user.getDescription(),
                user.getEmail(),
                user.getImageUrl(),
                bestReviews,   // 좋아요 많은 순 리뷰
                recentReviews  // 최신순 리뷰
        );
    }
}

