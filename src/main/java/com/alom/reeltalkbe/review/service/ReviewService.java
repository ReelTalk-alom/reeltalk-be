package com.alom.reeltalkbe.review.service;

import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.image.domain.Image;
import com.alom.reeltalkbe.image.repository.ImageRepository;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.dto.*;
import com.alom.reeltalkbe.review.dto.request.ReviewUpdateRequestDto;
import com.alom.reeltalkbe.review.dto.response.ReviewListResponseDto;
import com.alom.reeltalkbe.review.dto.request.ReviewRegisterRequestDto;
import com.alom.reeltalkbe.review.dto.response.ReviewResponseDto;
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


    public ReviewResponseDto registerReview(ReviewRegisterRequestDto requestDto) {

        Content content = contentRepository.findById(requestDto.getContentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠입니다."));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Image image = imageRepository.findByUrl(requestDto.getImageUrl())
                .orElse(null); // 이미지가 존재하지 않으면 null 허용

        Review review = Review.builder()
                .content(content)
                .user(user)
                .image(image)
                .description(requestDto.getDescription())
                .url(requestDto.getUrl())
                .build();

        return convertToDto(reviewRepository.save(review));
    }

    /**
     * 특정 콘텐츠의 리뷰 목록 조회 (DTO 없이 contentId만 사용)
     */
    @Transactional(readOnly = true)
    public ReviewListResponseDto getReviewsByContentId(Long contentId) {
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
                .orElseThrow(() -> new IllegalArgumentException(BaseResponseStatus.INVALID_REQUEST.getMessage()));
        return convertToSummaryDto(review);
    }

    /**
     * 리뷰 수정
     */
    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateRequestDto requestDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException(BaseResponseStatus.INVALID_REQUEST.getMessage()));

        review.updateReview(requestDTO.getUrl(), requestDTO.getDescription());


        return new ReviewResponseDto(
                review.getId(),
                review.getContent().getId(),
                review.getImage() != null ? review.getImage().getId() : null,
                review.getUser().getUserId(),
                review.getUrl(),
                review.getDescription(),
                review.getRatingAverage()
        );
    }


    /**
     * 리뷰 삭제 (DTO 없이 reviewId만 사용)
     */
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException(BaseResponseStatus.INVALID_REQUEST.getMessage()));

        reviewRepository.delete(review);
    }

    private ReviewResponseDto convertToDto(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getContent().getId(),
                review.getImage() != null ? review.getImage().getId() : null,
                review.getUser().getUserId(),
                review.getUrl(),
                review.getDescription(),
                review.getRatingAverage()
        );
    }

    private ReviewSummaryDto convertToSummaryDto(Review review) {
        return new ReviewSummaryDto(
                review.getId(),
                review.getContent().getId(),
                review.getUser().getUserId(),
                review.getImage() != null ? review.getImage().getId() : null,
                review.getRatingAverage(),
                review.getCreatedAt().toString(),
                review.getUpdatedAt().toString()
        );
    }
}
