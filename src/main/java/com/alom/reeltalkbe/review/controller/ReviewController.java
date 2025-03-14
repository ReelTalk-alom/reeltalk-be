package com.alom.reeltalkbe.review.controller;

import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.review.domain.reviewLike.LikeType;
import com.alom.reeltalkbe.review.dto.*;
import com.alom.reeltalkbe.review.dto.response.CommentListResponseDto;
import com.alom.reeltalkbe.review.dto.response.ReviewListResponseDto;
import com.alom.reeltalkbe.review.dto.response.ReviewResponseDto;
import com.alom.reeltalkbe.review.service.ReviewService;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 등록 API
     */
    @PostMapping
    public BaseResponse<ReviewResponseDto> registerReview(@RequestBody ReviewRequestDto requestDTO,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            log.info("토큰 인증 실패");
            throw new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);
        }

        Long userId = userDetails.getUserId();
        ReviewResponseDto responseDTO = reviewService.registerReview(userId, requestDTO);
        return new BaseResponse<>(responseDTO);
    }

    /**
     * 특정 콘텐츠의 리뷰 목록 조회 API (DTO 없이 contentId만 사용)
     */
    @GetMapping
    public BaseResponse<ReviewListResponseDto> getReviewsByContentId(@RequestParam Long contentId,
                                                                     @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("contentId" + contentId);
        ReviewListResponseDto listResponseDTO = reviewService.getReviewsByContentId(contentId, pageable);
        return new BaseResponse<>(listResponseDTO);
    }

    @GetMapping(params = "sort=top-review")
    public BaseResponse<List<ReviewResponseDto>> getTopReviews() {
        return new BaseResponse<>(reviewService.getTopReviews());
    }



    /**
     * 특정 리뷰 조회 API (DTO 없이 reviewId만 사용)
     */
    @GetMapping("/{reviewId}")
    public BaseResponse<CommentListResponseDto> getReviewById(@PathVariable Long reviewId) {
        return new BaseResponse<>(reviewService.getReviewById(reviewId));
    }



    /**
     * 리뷰 수정 API
     */
    @PutMapping("/{reviewId}")
    public BaseResponse<ReviewResponseDto> updateReview(@PathVariable Long reviewId,
                                                        @RequestBody ReviewRequestDto requestDTO,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            log.info("토큰 인증 실패");
            throw new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);
        }
        ReviewResponseDto responseDTO = reviewService.updateReview(userDetails.getUserId(), reviewId, requestDTO);
        return new BaseResponse<>(responseDTO);
    }

    /**
     * 리뷰 삭제 API (DTO 없이 reviewId만 사용)
     */
    @DeleteMapping("/{reviewId}")
    public BaseResponse<String> deleteReview(@PathVariable Long reviewId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            log.info("토큰 인증 실패");
            throw new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);
        }
        reviewService.deleteReview(userDetails.getUserId(), reviewId);
        return new BaseResponse<>("삭제완료");
    }

    @PostMapping("/{reviewId}")
    public BaseResponse<String> reactToReview(@PathVariable Long reviewId,
                                              @RequestParam LikeType likeType,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            log.info("토큰 인증 실패");
            throw new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);
        }
        reviewService.rateReview(userDetails.getUserId(), reviewId, likeType);
        return new BaseResponse<>(likeType == LikeType.LIKE ? "좋아요 처리 완료" : " 싫어요 처리 완료");
    }

    // 전체 리뷰 개수 조회 API
    @GetMapping("/count")
    public BaseResponse<Long> getTotalReviewCount() {
        long count = reviewService.getTotalReviewCount();
        return new BaseResponse<>(count);
    }

    //샘플 화면?
    @GetMapping("/sample")
    public BaseResponse<List<ReviewListResponseDto>> getSampleReviews(){
        List<ReviewListResponseDto> reviewListResponseDtos = reviewService.reviewSample();
        return new BaseResponse<>(reviewListResponseDtos);
    }
    @GetMapping("/random")
    public BaseResponse<List<ReviewResponseDto>> getRandomReviews(){
        List<ReviewResponseDto> reviewResponseDtos = reviewService.randomReview();
        return new BaseResponse<>(reviewResponseDtos);
    }

}
