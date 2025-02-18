package com.alom.reeltalkbe.user.controller;

import com.alom.reeltalkbe.review.dto.response.ReviewResponseDto;
import com.alom.reeltalkbe.review.dto.response.ReviewSummaryDto;
import com.alom.reeltalkbe.review.service.ReviewService;
import com.alom.reeltalkbe.user.dto.UserDto;
import com.alom.reeltalkbe.user.service.UserService;
import com.alom.reeltalkbe.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserService userService;
    private final ReviewService reviewService;

    /**
     * 마이페이지 정보 조회 (유저 프로필 + 리뷰 목록 + 베스트 리뷰)
     */
    @GetMapping("/{userId}")
    public BaseResponse<Map<String, Object>> getMyPage(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();

        // 1. 유저 프로필 정보
        UserDto userProfile = userService.getUserProfile(userId);
        response.put("users", List.of(userProfile));

        // 2. 유저가 작성한 리뷰 목록 (영상 리뷰 형식으로 변환)
        List<ReviewSummaryDto> userReviews = reviewService.getUserReviewSummary(userId);
        response.put("review_videos", userReviews);

        // 3. 유저의 베스트 리뷰 (가장 높은 평점 리뷰)
        ReviewSummaryDto bestReview = reviewService.getBestReviewSummary(userId);
        response.put("best_review", bestReview);

        return new BaseResponse<>(response);
    }

    @PutMapping("/{userId}")
    public BaseResponse<String> updateUserProfile(@PathVariable Long userId, @RequestBody UserDto userDto) {
        userService.updateUserProfile(userId, userDto);
        return new BaseResponse<>("Profile updated successfully");
    }

    @GetMapping("/{userId}/reviews")
    public BaseResponse<List<ReviewSummaryDto>> getUserReviews(@PathVariable Long userId) {
        List<ReviewSummaryDto> reviews = reviewService.getUserReviewSummary(userId);
        return new BaseResponse<>(reviews);
    }

    @GetMapping("/{userId}/best-review")
    public BaseResponse<ReviewSummaryDto> getBestReview(@PathVariable Long userId) {
        ReviewSummaryDto bestReview = reviewService.getBestReviewSummary(userId);
        return new BaseResponse<>(bestReview);
    }
}
