package com.alom.reeltalkbe.review.service;


import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponse;
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
import com.alom.reeltalkbe.review.repository.ReviewLikeRepository;
import com.alom.reeltalkbe.review.repository.ReviewRepository;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
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
    private final YouTubeService youTubeService;
    private final Random random =new Random();


    public ReviewResponseDto registerReview(Long userId, ReviewRequestDto requestDto) {

        Content content = contentRepository.findById(requestDto.getContentId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));


        try {
            // YouTube API를 활용하여 제목과 썸네일 가져오기
            String title = youTubeService.getVideoTitle(requestDto.getVideoPath());
            String thumbnailUrl = (requestDto.getThumbnail() == null || requestDto.getThumbnail().trim().isEmpty())
                    ? youTubeService.getThumbnailUrl(requestDto.getVideoPath()) // YouTube 썸네일 사용
                    : requestDto.getThumbnail(); // 사용자가 입력한 썸네일 사용

            Image image = Image.builder().url(thumbnailUrl).build();

            Review review = Review.builder()
                    .content(content)
                    .user(user)
                    .image(image)
                    .overview(requestDto.getOverview())
                    .videoPath(requestDto.getVideoPath())
                    .title(title) //  YouTube API에서 가져온 제목
                    .build();

            return ReviewResponseDto.fromEntity(reviewRepository.save(review));
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

        Page<ReviewResponseDto> reviewPage = reviewRepository.findByContentId(contentId, pageable)
                .map(ReviewResponseDto::fromEntity); // DTO 변환을 직접 처리

        return ReviewListResponseDto.fromEntity(content, reviewPage);
    }

    /**
     * 좋아요가 가장 많은 상위 10개 리뷰 조회
     */
    public List<ReviewResponseDto> getTopReviews() {
        Pageable pageable = PageRequest.of(0, 10); // 상위 10개 리뷰만 조회
        List<Review> topReviews = reviewRepository.findTopReviews(pageable);

        return topReviews.stream()
                .map(ReviewResponseDto::fromEntity) // DTO 변환
                .collect(Collectors.toList());
    }
    /**
     * 특정 리뷰 조회 (DTO 없이 reviewId만 사용)
     */
    @Transactional(readOnly = true)
    public CommentListResponseDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW)); // 4xx

        return CommentListResponseDto.fromEntity(review, review.getComments());
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
            // 기존 데이터 가져오기
            String currentVideoPath = review.getVideoPath();
            String newVideoPath = requestDTO.getVideoPath();
            boolean isVideoChanged = newVideoPath != null && !newVideoPath.equals(currentVideoPath);

            // 기본값 설정 (변경되지 않으면 기존 값 유지)
            String newOverview = requestDTO.getOverview() != null ? requestDTO.getOverview() : review.getOverview();
            String newThumbnail = review.getImage().getUrl(); // 기본값: 기존 썸네일
            String newTitle = review.getTitle(); // 기본값: 기존 제목

            // thumbnail이 변경되었는지 확인
            boolean isThumbnailChanged = requestDTO.getThumbnail() != null && !requestDTO.getThumbnail().trim().isEmpty();

            // videoPath가 변경되었고, 사용자가 새로운 썸네일을 제공하지 않았을 경우 → YouTube 썸네일 적용
            if (isVideoChanged && !isThumbnailChanged) {
                newTitle = youTubeService.getVideoTitle(newVideoPath);
                newThumbnail = youTubeService.getThumbnailUrl(newVideoPath);
            }
            // 사용자가 새로운 썸네일을 입력했을 경우 → 입력한 값 사용
            else if (isThumbnailChanged) {
                newThumbnail = requestDTO.getThumbnail();
            }

            // 필드 업데이트
            review.updateVideoAndTitle(newVideoPath, newTitle);
            review.updateOverview(newOverview);
            review.getImage().updateIfPresent(newThumbnail);

            return ReviewResponseDto.fromEntity(reviewRepository.save(review));
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FAIL_REVIEW_POST); // 5xx
        }
    }


    /**
     * 리뷰 삭제 (DTO 없이 reviewId만 사용)
     */
    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW)); // 4xx

        // 현재 로그인한 사용자가 리뷰 작성자인지 확인
        if (!review.getUser().getId().equals(userId)) {
            throw new BaseException(BaseResponseStatus.INVALID_MEMBER); // 4Xx
        }

        try {
            reviewRepository.delete(review);
        } catch (Exception e) {
            log.error("리뷰 삭제 중 오류 발생: {}", e.getMessage());
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR); // 5xx
        }
    }
    @Transactional(readOnly = true)
    // 전체 리뷰 개수 반환
    public long getTotalReviewCount() {
        return reviewRepository.countAllReviews();
    }


    //샘플
    @Transactional(readOnly = true)
    public List<ReviewListResponseDto> reviewSample() {
        // 특정 ID(238, 278, 13)의 콘텐츠 조회
        Content content1 = contentRepository.findById(278L)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));
        Content content2 = contentRepository.findById(13L)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));
        Content content3 = contentRepository.findById(238L)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));

        Pageable pageable = PageRequest.of(0, 10); // 상위 10개 리뷰만 조회

        // 리뷰 가져오기 및 DTO 변환
        Page<ReviewResponseDto> reviews1 = reviewRepository.findByContentId(content1.getId(), pageable)
                .map(ReviewResponseDto::fromEntity);
        Page<ReviewResponseDto> reviews2 = reviewRepository.findByContentId(content2.getId(), pageable)
                .map(ReviewResponseDto::fromEntity);
        Page<ReviewResponseDto> reviews3 = reviewRepository.findByContentId(content3.getId(), pageable)
                .map(ReviewResponseDto::fromEntity);



        // ReviewListResponseDto 리스트 생성
        return List.of(
                ReviewListResponseDto.fromEntity(content1, reviews1),

                ReviewListResponseDto.fromEntity(content1, reviews2),

                ReviewListResponseDto.fromEntity(content1, reviews3)
        );
    }


    //리뷰 20개 뽑기(샘플)
    public List<ReviewResponseDto> randomReview(){
        List<Review> allReviews = reviewRepository.findAll(); // 모든 리뷰 가져오기

        if (allReviews.size() <= 20) {
            // 리뷰 개수가 20개 이하이면 그냥 전체 반환
            return allReviews.stream()
                    .map(ReviewResponseDto::fromEntity)
                    .collect(Collectors.toList());
        }

        // 리뷰 랜덤으로 20개 선택
        return random.ints(0, allReviews.size()) // 0부터 리뷰 개수까지 랜덤한 인덱스 생성
                .distinct() // 중복 제거
                .limit(20) // 20개 선택
                .mapToObj(allReviews::get) // 해당 인덱스의 리뷰 가져오기
                .map(ReviewResponseDto::fromEntity) // DTO로 변환
                .collect(Collectors.toList()); // 리스트로 반환
    }
}
