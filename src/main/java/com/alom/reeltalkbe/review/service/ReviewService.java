package com.alom.reeltalkbe.review.service;

import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.image.domain.Image;
import com.alom.reeltalkbe.image.repository.ImageRepository;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.dto.*;
import com.alom.reeltalkbe.review.dto.response.ReviewListResponseDto;
import com.alom.reeltalkbe.review.dto.response.ReviewResponseDto;
import com.alom.reeltalkbe.review.dto.response.ReviewSummaryDto;
import com.alom.reeltalkbe.review.repository.ReviewRepository;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;


    public ReviewResponseDto registerReview(ReviewRequestDto requestDto) {

        Content content = contentRepository.findById(requestDto.getContentId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        Image image = imageRepository.findByUrl(requestDto.getImageUrl())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FAIL_IMAGE_CONVERT));


        try {
            Review review = Review.builder()
                    .content(content)
                    .user(user)
                    .image(image)
                    .description(requestDto.getDescription())
                    .url(requestDto.getUrl())
                    .build();
            return convertToDto(reviewRepository.save(review));
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FAIL_REVIEW_POST); // 5xx (서버 문제)
        }
    }

    /**
     * 특정 콘텐츠의 리뷰 목록 조회 (DTO 없이 contentId만 사용)
     */
    @Transactional(readOnly = true)
    public ReviewListResponseDto getReviewsByContentId(Long contentId) {

        if (!contentRepository.existsById(contentId)) { // 404
            throw new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND);
        }

        List<ReviewSummaryDto> reviewList = reviewRepository.findByContentId(contentId)
                .stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());

        return new ReviewListResponseDto(reviewList);
    }


    /**
     * 특정 리뷰 조회 (DTO 없이 reviewId만 사용)
     */
    @Transactional(readOnly = true)
    public ReviewSummaryDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW)); // 4xx
        return convertToSummaryDto(review);
    }

    /**
     * 리뷰 수정
     */
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto requestDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW)); // 4xx

        try {
            review.updateReview(requestDTO.getUrl(), requestDTO.getDescription());
            return convertToDto(review);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FAIL_REVIEW_POST); // 5xx
        }
    }


    /**
     * 리뷰 삭제 (DTO 없이 reviewId만 사용)
     */
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW)); // 4xx
        try {
            reviewRepository.delete(review);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR); // 5xx
        }
    }

    /**
     * 추가
     */
    public List<ReviewResponseDto> getUserReviews(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);

        return reviews.stream()
                .map(review -> new ReviewResponseDto(
                        review.getId(),
                        review.getContent().getId(),
                        review.getImage() != null ? review.getImage().getId() : null,
                        review.getUser().getId(),
                        review.getUrl(),
                        review.getDescription(),
                        review.getRatingAverage()
                ))
                .collect(Collectors.toList());
    }

    public ReviewResponseDto getBestReview(Long userId) {
        Review bestReview = reviewRepository.findTopByUserIdOrderByRatingSumDesc(userId)
                .orElseThrow(() -> new RuntimeException("No reviews found"));

        return new ReviewResponseDto(
                bestReview.getId(),
                bestReview.getContent().getId(),
                bestReview.getImage() != null ? bestReview.getImage().getId() : null,
                bestReview.getUser().getId(),
                bestReview.getUrl(),
                bestReview.getDescription(),
                bestReview.getRatingAverage()
        );
    }

    @Transactional(readOnly = true)
    public List<ReviewSummaryDto> getUserReviewSummary(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);

        return reviews.stream()
                .map(review -> new ReviewSummaryDto(
                        review.getId(),
                        review.getContent().getTitle(),
                        review.getUser().getUsername(),
                        review.getUser().getId(),
                        review.getDescription(),
                        review.getUrl(),
                        review.getCreatedAt().toString(),
                        null,
                        review.getImage() != null ? review.getImage().getUrl() : null,
                        0,
                        review.getRatingAverage()
                ))
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public ReviewSummaryDto getBestReviewSummary(Long userId) {
        Review bestReview = reviewRepository.findTopByUserIdOrderByRatingSumDesc(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW)); // 예외 처리

        return new ReviewSummaryDto(
                bestReview.getId(),  // 리뷰 ID
                bestReview.getContent().getTitle(),  // 영화 제목
                bestReview.getUser().getUsername(),  // 작성자 이름
                bestReview.getUser().getId(),  // 작성자 ID
                bestReview.getDescription(),  // 리뷰 요약
                bestReview.getUrl(),  // 영상 주소
                bestReview.getCreatedAt().toString(),  // 작성 날짜
                null,  // 리뷰 길이 (미정)
                bestReview.getImage() != null ? bestReview.getImage().getUrl() : null, // 썸네일 이미지
                0,  // 좋아요 개수 (미정)
                bestReview.getRatingAverage()  // 별점 정보
        );
    }


    private ReviewResponseDto convertToDto(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getContent().getId(),
                review.getImage() != null ? review.getImage().getId() : null,
                review.getUser().getId(),
                review.getUrl(),
                review.getDescription(),
                review.getRatingAverage()
        );
    }

    private ReviewSummaryDto convertToSummaryDto(Review review) {
        return new ReviewSummaryDto(
                review.getId(),  // 리뷰 ID
                review.getContent().getTitle(),  // 영화 제목 (name)
                review.getUser().getUsername(),  // 작성자 이름 (author)
                review.getUser().getId(),  // 작성자 아이디 (author_id)
                review.getDescription(),  // 리뷰 요약 (overview)
                review.getUrl(),  // 리뷰 URL (video_path)
                review.getCreatedAt().toString(),  // 리뷰 작성 날짜 (published_at)
                null,  // 리뷰 길이 (미정)
                review.getImage() != null ? review.getImage().getUrl() : null,  // 썸네일 이미지 (thumbnail)
                0,  // 좋아요 개수 (미정)
                review.getRatingAverage()  // 별점 정보 (popularity)
        );
    }
}
