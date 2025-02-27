package com.alom.reeltalkbe.review.service;


import com.alom.reeltalkbe.comment.domain.Comment;
import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.image.domain.Image;
import com.alom.reeltalkbe.review.domain.Review;
import com.alom.reeltalkbe.review.domain.reviewLike.LikeType;
import com.alom.reeltalkbe.review.domain.reviewLike.ReviewLike;
import com.alom.reeltalkbe.review.dto.ReviewRequestDto;
import com.alom.reeltalkbe.review.dto.response.CommentListResponseDto;
import com.alom.reeltalkbe.review.dto.response.ReviewListResponseDto;
import com.alom.reeltalkbe.review.dto.response.ReviewResponseDto;
import com.alom.reeltalkbe.review.dto.response.summary.CommentSummaryDto;
import com.alom.reeltalkbe.review.dto.response.summary.ReviewSummaryDto;
import com.alom.reeltalkbe.review.repository.ReviewLikeRepository;
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
    private final ReviewLikeRepository reviewLikeRepository;



    public ReviewResponseDto registerReview(Long userId, ReviewRequestDto requestDto) {

        Content content = contentRepository.findById(requestDto.getContentId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));




        try {
            Image image = Image.builder()
                    .url(requestDto.getImageUrl())
                    .build();  //imageRepository.save() 필요 없음
            Review review = Review.builder()
                    .content(content)
                    .user(user)
                    .image(image)
                    .overview(requestDto.getOverview())
                    .videoPath(requestDto.getVideoPath())
                    .duration(requestDto.getDuration())
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
                review.getUser().getUsername(),
                review.getUser().getId(),
                review.getOverview(),
                review.getVideoPath(),
                review.getCreatedAt().toString(),
                review.getDuration(),
                review.getImage().getUrl(),
                review.getLikeCount(),
                review.getHateCount(),
                commentList  //  댓글 리스트 포함
        );
    }


    /**
        리뷰 좋아요,싫어요 기능
     */
    public void rateReview(Long userId, Long reviewId, LikeType likeType) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        Optional<ReviewLike> existingLike = reviewLikeRepository.findByUserAndReview(user, review);

        if (existingLike.isPresent()) {
            ReviewLike reviewLike = existingLike.get();
            reviewLike.changeLikeType(likeType);  //  좋아요/싫어요 타입 변경
        } else {
            ReviewLike newLike = ReviewLike.builder()
                    .user(user)
                    .review(review)
                    .likeType(likeType)
                    .build();
            reviewLikeRepository.save(newLike);  //  처음 누를 때는 새로 추가
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
            //이미지 url 업데이트
            review.getImage().updateIfPresent(requestDTO.getImageUrl());
            // 리뷰 내용 및 영상 경로 업데이트 (엔티티에서 처리)
            review.updateIfPresent(requestDTO.getVideoPath(), requestDTO.getOverview());

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
                review.getUser().getId(),
                review.getVideoPath(),
                review.getOverview(),
                review.getDuration(),
                review.getImage().getUrl(),
                review.getLikeCount(),
                review.getHateCount()
        );
    }

    private ReviewSummaryDto convertToReviewSummaryDto(Review review) {
        return new ReviewSummaryDto(
                review.getId(),
                review.getUser().getId(),
                review.getImage().getUrl(),
                review.getCreatedAt().toString(),
                review.getUpdatedAt().toString()
        );
    }

    private CommentSummaryDto convertToCommentSummaryDto(Comment comment) {
        return new CommentSummaryDto(
                comment.getId(),
                comment.getUser().getUsername(),
                comment.getUser().getImageUrl(),
                comment.getCreatedAt().toString(),
                comment.getLikeCount()
        );
    }



}
