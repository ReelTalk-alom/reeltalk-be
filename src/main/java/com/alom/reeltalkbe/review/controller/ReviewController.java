package com.alom.reeltalkbe.review.controller;

import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.review.dto.*;
import com.alom.reeltalkbe.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 등록 API
     */
    @PostMapping
    public BaseResponse<ReviewResponseDto> registerReview(@RequestBody ReviewRegisterRequestDto requestDTO) {
        ReviewResponseDto responseDTO = reviewService.registerReview(requestDTO);
        return new BaseResponse<>(responseDTO);
    }

    /**
     * 특정 콘텐츠의 리뷰 목록 조회 API (DTO 없이 contentId만 사용)
     */
    @GetMapping("/content/{contentId}")
    public BaseResponse<ReviewListResponseDto> getReviewsByContentId(@PathVariable Long contentId) {
        ReviewListResponseDto listResponseDTO = reviewService.getReviewsByContentId(contentId);
        return new BaseResponse<>(listResponseDTO);
    }

    /**
     * 특정 리뷰 조회 API (DTO 없이 reviewId만 사용)
     */
    @GetMapping("/{reviewId}")
    public BaseResponse<ReviewSummaryDto> getReviewById(@PathVariable Long reviewId) {
        return new BaseResponse<>(reviewService.getReviewById(reviewId));
    }

    /**
     * 리뷰 수정 API
     */
    @PutMapping
    public BaseResponse<ReviewResponseDto> updateReview(@RequestBody ReviewUpdateRequestDto requestDTO) {
        ReviewResponseDto responseDTO = reviewService.updateReview(requestDTO);
        return new BaseResponse<>(responseDTO);
    }

    /**
     * 리뷰 삭제 API (DTO 없이 reviewId만 사용)
     */
    @DeleteMapping("/{reviewId}")
    public BaseResponse<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new BaseResponse<>(null);
    }
}
