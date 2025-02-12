package com.alom.reeltalkbe.review.service;

import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.domain.ReviewRating;
import com.alom.reeltalkbe.review.dto.ReviewRatingDto;
import com.alom.reeltalkbe.review.repository.ReviewRatingRepository;
import com.alom.reeltalkbe.review.repository.ReviewRepository;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReviewRatingService {

    private final ReviewRepository reviewRepository;
    private final ReviewRatingRepository reviewRatingRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰 평점 등록 or 수정
     */
    @Transactional
    public void rateReview(ReviewRatingDto reviewRatingDto) {
        Long userId = reviewRatingDto.getUserId();
        Long reviewId = reviewRatingDto.getReviewId();
        int ratingValue = reviewRatingDto.getRating();

        // 1~5점 범위 확인, BaseEntity 추가해서해야할듯
      /*  if (ratingValue < 1 || ratingValue > 5) {
            throw new BaseException();
        }*/

        // 유저 & 리뷰 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW)); // 4xx

        // 기존 평점이 존재하는지 확인
        Optional<ReviewRating> existingRating = reviewRatingRepository.findByUserAndReview(user, review);

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
    }
}
