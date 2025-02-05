package com.alom.reeltalkbe.content.service;

import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.content.domain.Content;
import com.alom.reeltalkbe.content.domain.Rating;
import com.alom.reeltalkbe.content.dto.RatingDto;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.content.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final RatingRepository ratingRepository;

    // 포스터 이미지와 링크만 가져오기?
    public List<Content> findPopularContentImages() {
        //
        return contentRepository.findAll();
    }

    public Content findContentById(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REQUEST));
    }

    public Content addRating(Long content_id, RatingDto ratingDto) {
        Content content = contentRepository.findById(content_id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));

        Rating rating = Rating.builder()
                // todo : 빌더패턴 user 추가
                .content(content)
                .ratingValue(ratingDto.getRating())
                .build();

        // 영속성 컨텍스트가 객체값 변경시 자동 변경사항 반영
        content.updateRating(rating);
        ratingRepository.save(rating);
        return content;
    }

    public Content deleteRating(Long content_id, Long user_id) {
        Content content = contentRepository.findById(content_id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND));
        Rating rating = ratingRepository.findRatingByContentAndUser(content_id, user_id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.EXIST_RATING));

        content.deleteRating(rating);
        ratingRepository.delete(rating);
        return content;
    }
}