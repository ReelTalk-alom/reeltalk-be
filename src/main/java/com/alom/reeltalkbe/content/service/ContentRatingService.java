package com.alom.reeltalkbe.content.service;

import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.domain.ContentRating;
import com.alom.reeltalkbe.content.dto.RatingRequest;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.content.repository.RatingRepository;
import com.alom.reeltalkbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentRatingService {

    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final RatingRepository ratingRepository;

    public Content addRating(Long contentId, Long userId, RatingRequest ratingRequest) {
        // 이미 평가한 컨텐츠라면 예외 처리
        if (ratingRepository.findRatingByContentIdAndUserId(contentId, userId).isPresent()) {
            throw new BaseException(BaseResponseStatus.EXIST_RATING);
        }
        // content 조회
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));


        // user 조회 후 rating 빌더 패턴 사용
        ContentRating rating = ContentRating
                .builder()
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER)))
                .content(content)
                .ratingValue(ratingRequest.getRating())
                .build();

        // 컨텐츠 평균 평점 계산 후 rating 객체 DB에 저장
        content.updateRating(rating);
        ratingRepository.save(rating);
        return content;
    }

    public Content deleteRating(Long contentId, Long userId) {
        // content, rating 조회
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));
        ContentRating rating = ratingRepository.findRatingByContentIdAndUserId(contentId, userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.RATING_NOT_FOUND));

        content.deleteRating(rating);
        ratingRepository.delete(rating);
        return content;
    }
}
