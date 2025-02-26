package com.alom.reeltalkbe.review.service;


import com.alom.reeltalkbe.comment.domain.Comment;
import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.image.domain.Image;
import com.alom.reeltalkbe.image.repository.ImageRepository;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.domain.ReviewRating;
import com.alom.reeltalkbe.review.dto.ReviewRatingDto;
import com.alom.reeltalkbe.review.dto.ReviewRequestDto;
import com.alom.reeltalkbe.review.dto.response.CommentListResponseDto;
import com.alom.reeltalkbe.review.dto.response.ReviewListResponseDto;
import com.alom.reeltalkbe.review.dto.response.ReviewResponseDto;
import com.alom.reeltalkbe.review.dto.response.summary.CommentSummaryDto;
import com.alom.reeltalkbe.review.dto.response.summary.ReviewSummaryDto;
import com.alom.reeltalkbe.review.repository.ReviewRatingRepository;
import com.alom.reeltalkbe.review.repository.ReviewRepository;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ReviewRatingRepository reviewRatingRepository;


    public ReviewResponseDto registerReview(Long userId, ReviewRequestDto requestDto) {

        Content content = contentRepository.findById(requestDto.getContentId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        Image image = imageRepository.findById(requestDto.getImageId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FAIL_IMAGE_CONVERT));

        // 같은 userId + contentId 조합의 리뷰가 이미 존재하는지 확인
//        boolean alreadyReviewed = reviewRepository.existsByUserIdAndContentId(userId, requestDto.getContentId());
//        if (alreadyReviewed) {
//            throw new BaseException(BaseResponseStatus.EXIST_REVIEW); // 409 Conflict
//        }


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
    public ReviewListResponseDto getReviewsByContentId(Long contentId, Pageable pageable) {

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));

        Page<ReviewSummaryDto> reviewPage = reviewRepository.findByContentId(contentId, pageable)
                .map(this::convertToReviewSummaryDto);

        return new ReviewListResponseDto(
                content.getId(),
                content.getEnTitle(),           // 위승재 : content 엔티티 변경으로 인한 오류 해결
                content.getGenres().toString(),
                content.getCountry(),
                content.getReleaseDate(),
                content.getRatingAverage(),
                reviewPage.getContent(),  // 페이지 내부 데이터 리스트
                reviewPage.getTotalPages(),  // 전체 페이지 개수
                reviewPage.getTotalElements(),  // 전체 데이터 개수
                reviewPage.getNumber()  // 현재 페이지 번호
        );
    }


    /**
     * 특정 리뷰 조회 (DTO 없이 reviewId만 사용)
     */
    @Transactional(readOnly = true)
    public CommentListResponseDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW)); // 4xx

        List<CommentSummaryDto> commentList = review.getComments().stream()
                .map(this::convertToCommentSummaryDto)
                .collect(Collectors.toList());

        return new CommentListResponseDto(
                review.getId(),
                review.getContent().getId(),
                (review.getImage() != null) ? review.getImage().getId() : null,
                review.getUser().getId(),
                review.getUrl(),
                review.getDescription(),
                review.getRatingAverage(),
                commentList  //  댓글 리스트 포함
        );
    }


    /**
     * 리뷰 평점 등록 or 수정
     */
    @Transactional
    public ReviewResponseDto rateReview(Long userId, Long reviewId, ReviewRatingDto reviewRatingDto) {

        int ratingValue = reviewRatingDto.getRating();

        // 유저 & 리뷰 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW)); // 4xx

        // 기존 평점이 존재하는지 확인
        Optional<ReviewRating> existingRating = reviewRatingRepository.findByUserAndReview(user, review);

        try {
            if (existingRating.isPresent()) {
                // 기존 평점 수정 (기존 점수 제거 후 새로운 점수 추가)
                ReviewRating rating = existingRating.get();
                review.removeRating(rating.getRatingValue());
                rating.setRatingValue(ratingValue);
                review.addRating(rating.getRatingValue());
            } else {
                // 새로운 평점 추가
                ReviewRating newRating = ReviewRating.builder()
                        .user(user)
                        .review(review)
                        .ratingValue(ratingValue)
                        .build();
                reviewRatingRepository.save(newRating);
                review.addRating(newRating.getRatingValue());
            }
            return convertToDto(review);
        }catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR); // 5xx
        }

    }

    /**
     * 리뷰 수정
     */
    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewRequestDto requestDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW)); // 4xx

        // 현재 로그인한 사용자가 리뷰 작성자인지 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new BaseException(BaseResponseStatus.INVALID_MEMBER); // 4xx
        }

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
    public ReviewResponseDto deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW)); // 4xx

        // 현재 로그인한 사용자가 리뷰 작성자인지 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new BaseException(BaseResponseStatus.INVALID_MEMBER); // 4Xx
        }

        try {
            reviewRepository.delete(review);
            return convertToDto(review);
        } catch (Exception e) {
            log.error("리뷰 삭제 중 오류 발생: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR); // 5xx
        }
    }

    private ReviewResponseDto convertToDto(Review review) {

        return new ReviewResponseDto(
                review.getId(),
                review.getContent().getId(),
                (review.getImage() != null) ? review.getImage().getId() : null,
                review.getUser().getId(),
                review.getUrl(),
                review.getDescription(),
                review.getRatingAverage()
        );
    }

    private ReviewSummaryDto convertToReviewSummaryDto(Review review) {
        return new ReviewSummaryDto(
                review.getId(),
                review.getUser().getId(),
                review.getImage() != null ? review.getImage().getId() : null,
                review.getRatingAverage(),
                review.getCreatedAt().toString(),
                review.getUpdatedAt().toString()
        );
    }

    private CommentSummaryDto convertToCommentSummaryDto(Comment comment) {
        return new CommentSummaryDto(
                comment.getId(),
                comment.getUser().getUsername(),
                comment.getUser().getImageUrl(),
                comment.getContent(),
                comment.getCreatedAt().toString()
        );
    }

    /**
     * 추가
     */

}
